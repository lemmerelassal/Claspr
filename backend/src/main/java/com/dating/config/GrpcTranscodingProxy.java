package com.dating.config;

import com.dating.service.AuthService;
import com.dating.service.ChatService;
import com.dating.service.MatchingService;
import com.dating.entity.UserProfile;
import com.dating.entity.ChatMessageEntity;
import com.dating.entity.Match;
import com.dating.dto.UserDtos.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.*;

/**
 * gRPC-JSON Transcoding Proxy
 *
 * Translates JSON HTTP requests from the Angular gRPC-Web client into
 * calls to the backend gRPC service layer. In production with binary
 * protobuf stubs and Envoy gRPC-Web filter, this proxy is bypassed —
 * the browser talks directly to gRPC via Envoy.
 *
 * For local development, this gives us the same API shape as gRPC
 * without requiring protoc-generated JS stubs.
 *
 * Route pattern: POST /grpc/{ServiceName}/{MethodName}
 */
@Path("/grpc")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GrpcTranscodingProxy {

    @Inject
    AuthService authService;

    @Inject
    MatchingService matchingService;

    @Inject
    ChatService chatService;

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // AuthService
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    @POST
    @Path("/AuthService/Login")
    public Response authLogin(Map<String, Object> body) {
        try {
            String email = str(body, "email");
            String password = str(body, "password");
            var result = authService.login(email, password);
            return Response.ok(Map.of(
                    "token", result.token(),
                    "user_id", result.userId().toString(),
                    "display_name", result.displayName()
            )).build();
        } catch (SecurityException e) {
            return Response.status(401).entity(Map.of("message", "Invalid credentials")).build();
        }
    }

    @POST
    @Path("/AuthService/Register")
    public Response authRegister(Map<String, Object> body) {
        try {
            String genderStr = str(body, "gender");
            UserProfile.Gender gender = genderStr != null && !genderStr.isEmpty()
                    ? UserProfile.Gender.valueOf(genderStr) : UserProfile.Gender.OTHER;

            var result = authService.register(
                    str(body, "email"), str(body, "password"),
                    str(body, "display_name"), str(body, "date_of_birth"), gender
            );
            return Response.ok(Map.of(
                    "token", result.token(),
                    "user_id", result.userId().toString(),
                    "display_name", result.displayName()
            )).build();
        } catch (IllegalArgumentException e) {
            return Response.status(409).entity(Map.of("message", e.getMessage())).build();
        }
    }

    @POST
    @Path("/AuthService/ValidateToken")
    public Response authValidate(Map<String, Object> body) {
        var result = authService.validateToken(str(body, "token"));
        return Response.ok(Map.of(
                "valid", result.valid(),
                "user_id", result.userId() != null ? result.userId() : ""
        )).build();
    }

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // ProfileService
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    @POST
    @Path("/ProfileService/GetMyProfile")
    public Response getMyProfile(Map<String, Object> body) {
        UUID userId = uuid(body, "user_id");
        UserProfile user = UserProfile.findById(userId);
        if (user == null) return Response.status(404).entity(Map.of("message", "Not found")).build();
        return Response.ok(profileToMap(user, 0)).build();
    }

    @POST
    @Path("/ProfileService/GetProfile")
    public Response getProfile(Map<String, Object> body) {
        UUID targetId = uuid(body, "target_user_id");
        UUID viewerId = uuid(body, "user_id");
        UserProfile target = UserProfile.findById(targetId);
        UserProfile viewer = UserProfile.findById(viewerId);
        if (target == null) return Response.status(404).build();
        double dist = (viewer != null && viewer.latitude != null && target.latitude != null)
                ? haversine(viewer.latitude, viewer.longitude, target.latitude, target.longitude) : 0;
        return Response.ok(profileToMap(target, dist)).build();
    }

    @POST
    @Path("/ProfileService/UpdateProfile")
    @jakarta.transaction.Transactional
    public Response updateProfile(Map<String, Object> body) {
        UUID userId = uuid(body, "user_id");
        UserProfile user = UserProfile.findById(userId);
        if (user == null) return Response.status(404).build();

        if (body.containsKey("display_name") && !str(body, "display_name").isEmpty())
            user.displayName = str(body, "display_name");
        if (body.containsKey("bio")) user.bio = str(body, "bio");
        if (body.containsKey("max_distance_km")) user.maxDistanceKm = intVal(body, "max_distance_km");
        if (body.containsKey("min_age_preference")) user.minAgePreference = intVal(body, "min_age_preference");
        if (body.containsKey("max_age_preference")) user.maxAgePreference = intVal(body, "max_age_preference");
        if (body.containsKey("weight")) user.weight = dblVal(body, "weight");
        if (body.containsKey("weight_unit")) user.weightUnit = str(body, "weight_unit");
        if (body.containsKey("height")) user.height = dblVal(body, "height");
        if (body.containsKey("height_unit")) user.heightUnit = str(body, "height_unit");
        if (body.containsKey("min_weight_preference")) user.minWeightPreference = dblVal(body, "min_weight_preference");
        if (body.containsKey("max_weight_preference")) user.maxWeightPreference = dblVal(body, "max_weight_preference");
        if (body.containsKey("min_height_preference")) user.minHeightPreference = dblVal(body, "min_height_preference");
        if (body.containsKey("max_height_preference")) user.maxHeightPreference = dblVal(body, "max_height_preference");
        if (body.containsKey("interests")) {
            user.interests.clear();
            @SuppressWarnings("unchecked")
            var list = (List<String>) body.get("interests");
            if (list != null) user.interests.addAll(list);
        }
        if (body.containsKey("photo_urls")) {
            user.photoUrls.clear();
            @SuppressWarnings("unchecked")
            var list = (List<String>) body.get("photo_urls");
            if (list != null) user.photoUrls.addAll(list);
        }
        user.persist();
        return Response.ok(profileToMap(user, 0)).build();
    }

    @POST
    @Path("/ProfileService/UpdateLocation")
    @jakarta.transaction.Transactional
    public Response updateLocation(Map<String, Object> body) {
        UUID userId = uuid(body, "user_id");
        UserProfile user = UserProfile.findById(userId);
        if (user == null) return Response.status(404).build();
        user.latitude = dblVal(body, "latitude");
        user.longitude = dblVal(body, "longitude");
        if (body.containsKey("city")) user.city = str(body, "city");
        user.persist();
        return Response.ok(profileToMap(user, 0)).build();
    }

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // MatchingService
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    @POST
    @Path("/MatchingService/GetPotentialMatches")
    public Response getPotentialMatches(Map<String, Object> body) {
        UUID userId = uuid(body, "user_id");
        int limit = body.containsKey("limit") ? intVal(body, "limit") : 10;
        var profiles = matchingService.getPotentialMatches(userId, limit);
        var cards = profiles.stream().map(p -> Map.of(
                "user_id", p.id().toString(),
                "display_name", p.displayName(),
                "age", p.age(),
                "bio", p.bio() != null ? p.bio() : "",
                "photo_urls", p.photoUrls() != null ? p.photoUrls() : List.of(),
                "distance_km", p.distanceKm(),
                "interests", p.interests() != null ? p.interests() : Set.of(),
                "location_city", p.city() != null ? p.city() : ""
        )).toList();
        return Response.ok(Map.of("profiles", cards)).build();
    }

    @POST
    @Path("/MatchingService/RecordSwipe")
    public Response recordSwipe(Map<String, Object> body) {
        UUID swiperId = uuid(body, "swiper_id");
        UUID swipedId = uuid(body, "swiped_id");
        String direction = str(body, "direction");
        var result = matchingService.recordSwipe(swiperId, swipedId, direction);

        Map<String, Object> response = new HashMap<>();
        response.put("is_match", result.isMatch());
        if (result.matchId() != null) response.put("match_id", result.matchId().toString());
        if (result.matchedProfile() != null) {
            var p = result.matchedProfile();
            response.put("matched_profile", Map.of(
                    "user_id", p.id().toString(),
                    "display_name", p.displayName(),
                    "age", p.age(),
                    "bio", p.bio() != null ? p.bio() : "",
                    "photo_urls", p.photoUrls() != null ? p.photoUrls() : List.of(),
                    "distance_km", p.distanceKm(),
                    "interests", p.interests() != null ? p.interests() : Set.of(),
                    "location_city", p.city() != null ? p.city() : ""
            ));
        }
        return Response.ok(response).build();
    }

    @POST
    @Path("/MatchingService/GetMatches")
    public Response getMatches(Map<String, Object> body) {
        UUID userId = uuid(body, "user_id");
        var matches = matchingService.getMatches(userId);
        var entries = matches.stream().map(m -> {
            Map<String, Object> entry = new HashMap<>();
            entry.put("match_id", m.matchId().toString());
            entry.put("matched_at", m.matchedAt());
            entry.put("profile", Map.of(
                    "user_id", m.profile().id().toString(),
                    "display_name", m.profile().displayName(),
                    "age", m.profile().age(),
                    "bio", m.profile().bio() != null ? m.profile().bio() : "",
                    "photo_urls", m.profile().photoUrls() != null ? m.profile().photoUrls() : List.of(),
                    "distance_km", m.profile().distanceKm(),
                    "interests", m.profile().interests() != null ? m.profile().interests() : Set.of(),
                    "location_city", m.profile().city() != null ? m.profile().city() : ""
            ));
            if (m.lastMessage() != null) {
                entry.put("last_message", Map.of(
                        "content", m.lastMessage().content(),
                        "sent_at", m.lastMessage().sentAt(),
                        "sender_id", m.lastMessage().fromMe() ? userId.toString() : "other"
                ));
            }
            return entry;
        }).toList();
        return Response.ok(Map.of("matches", entries, "total", entries.size())).build();
    }

    @POST
    @Path("/MatchingService/UnmatchUser")
    public Response unmatchUser(Map<String, Object> body) {
        UUID userId = uuid(body, "user_id");
        UUID matchId = uuid(body, "match_id");
        matchingService.unmatch(userId, matchId);
        return Response.ok(Map.of("success", true)).build();
    }

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // ChatService
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    @POST
    @Path("/ChatService/SendMessage")
    public Response sendMessage(Map<String, Object> body) {
        UUID matchId = uuid(body, "match_id");
        UUID senderId = uuid(body, "sender_id");
        String content = str(body, "content");
        String type = body.containsKey("type") ? str(body, "type") : "TEXT";
        var msg = chatService.sendMessage(matchId, senderId, content, type);
        return Response.ok(Map.of(
                "message_id", msg.id().toString(),
                "sent_at", msg.sentAt(),
                "delivered", true
        )).build();
    }

    @POST
    @Path("/ChatService/GetConversation")
    public Response getConversation(Map<String, Object> body) {
        UUID matchId = uuid(body, "match_id");
        UUID userId = uuid(body, "user_id");
        int page = body.containsKey("page") ? intVal(body, "page") : 0;
        int size = body.containsKey("size") ? intVal(body, "size") : 50;
        var messages = chatService.getConversation(matchId, userId, page, size);
        var msgList = messages.stream().map(m -> Map.of(
                "message_id", (Object) m.id().toString(),
                "sender_id", m.senderId().toString(),
                "content", m.content(),
                "type", m.type(),
                "sent_at", m.sentAt(),
                "read", m.read()
        )).toList();
        return Response.ok(Map.of("messages", msgList, "total", msgList.size())).build();
    }

    @POST
    @Path("/ChatService/MarkRead")
    public Response markRead(Map<String, Object> body) {
        UUID matchId = uuid(body, "match_id");
        UUID userId = uuid(body, "user_id");
        chatService.markAsRead(matchId, userId);
        return Response.ok(Map.of("marked_count", 1)).build();
    }

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // Helpers
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    private Map<String, Object> profileToMap(UserProfile u, double distance) {
        Map<String, Object> m = new HashMap<>();
        m.put("user_id", u.id.toString());
        m.put("display_name", u.displayName);
        m.put("age", u.getAge());
        m.put("bio", u.bio != null ? u.bio : "");
        m.put("gender", u.gender != null ? u.gender.name() : "OTHER");
        m.put("photo_urls", u.photoUrls != null ? u.photoUrls : List.of());
        m.put("interests", u.interests != null ? u.interests : Set.of());
        m.put("city", u.city != null ? u.city : "");
        m.put("distance_km", Math.round(distance * 10.0) / 10.0);
        m.put("latitude", u.latitude != null ? u.latitude : 0);
        m.put("longitude", u.longitude != null ? u.longitude : 0);
        m.put("max_distance_km", u.maxDistanceKm != null ? u.maxDistanceKm : 50);
        m.put("min_age_preference", u.minAgePreference != null ? u.minAgePreference : 18);
        m.put("max_age_preference", u.maxAgePreference != null ? u.maxAgePreference : 99);
        m.put("gender_preference", u.genderPreference != null ? u.genderPreference.name() : "");
        m.put("weight", u.weight != null ? u.weight : 0);
        m.put("weight_unit", u.weightUnit != null ? u.weightUnit : "kg");
        m.put("height", u.height != null ? u.height : 0);
        m.put("height_unit", u.heightUnit != null ? u.heightUnit : "cm");
        m.put("min_weight_preference", u.minWeightPreference != null ? u.minWeightPreference : 0);
        m.put("max_weight_preference", u.maxWeightPreference != null ? u.maxWeightPreference : 0);
        m.put("min_height_preference", u.minHeightPreference != null ? u.minHeightPreference : 0);
        m.put("max_height_preference", u.maxHeightPreference != null ? u.maxHeightPreference : 0);
        return m;
    }

    private String str(Map<String, Object> m, String key) {
        Object v = m.get(key);
        return v != null ? v.toString() : "";
    }

    private UUID uuid(Map<String, Object> m, String key) {
        return UUID.fromString(str(m, key));
    }

    private int intVal(Map<String, Object> m, String key) {
        Object v = m.get(key);
        if (v instanceof Number n) return n.intValue();
        return Integer.parseInt(v.toString());
    }

    private double dblVal(Map<String, Object> m, String key) {
        Object v = m.get(key);
        if (v instanceof Number n) return n.doubleValue();
        return Double.parseDouble(v.toString());
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
