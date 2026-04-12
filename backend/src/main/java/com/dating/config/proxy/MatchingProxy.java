package com.dating.config.proxy;

import com.dating.service.IDiscoveryService;
import com.dating.service.IMatchService;
import com.dating.service.ISwipeService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.*;

import static com.dating.config.proxy.ProfileMapper.*;

@Path("/grpc/MatchingService")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MatchingProxy {

    @Inject IDiscoveryService discoveryService;
    @Inject ISwipeService swipeService;
    @Inject IMatchService matchService;

    @POST
    @Path("/GetPotentialMatches")
    public Response getPotentialMatches(Map<String, Object> body) {
        var userId = uuid(body, "user_id");
        int limit = body.containsKey("limit") ? intVal(body, "limit") : 10;
        var profiles = discoveryService.getPotentialMatches(userId, limit);
        var cards = profiles.stream().map(ProfileMapper::toCard).toList();
        return Response.ok(Map.of("profiles", cards)).build();
    }

    @POST
    @Path("/RecordSwipe")
    public Response recordSwipe(Map<String, Object> body) {
        var result = swipeService.recordSwipe(
                uuid(body, "swiper_id"),
                uuid(body, "swiped_id"),
                str(body, "direction")
        );

        Map<String, Object> response = new HashMap<>();
        response.put("is_match", result.isMatch());
        if (result.matchId() != null) response.put("match_id", result.matchId().toString());
        if (result.matchedProfile() != null) response.put("matched_profile", toCard(result.matchedProfile()));
        return Response.ok(response).build();
    }

    @POST
    @Path("/GetMatches")
    public Response getMatches(Map<String, Object> body) {
        var userId = uuid(body, "user_id");
        var matches = matchService.getMatches(userId);
        var entries = matches.stream().map(m -> {
            Map<String, Object> entry = new HashMap<>();
            entry.put("match_id", m.matchId().toString());
            entry.put("matched_at", m.matchedAt());
            entry.put("profile", toCard(m.profile()));
            if (m.lastMessage() != null) {
                entry.put("last_message", Map.of(
                        "content", m.lastMessage().content(),
                        "sent_at", m.lastMessage().sentAt(),
                        "sender_id", m.lastMessage().fromMe() ? userId.toString() : "other"
                ));
            }
            entry.put("unread_count", m.unreadCount());
            return entry;
        }).toList();
        return Response.ok(Map.of("matches", entries, "total", entries.size())).build();
    }

    @POST
    @Path("/UnmatchUser")
    public Response unmatchUser(Map<String, Object> body) {
        matchService.unmatch(uuid(body, "user_id"), uuid(body, "match_id"));
        return Response.ok(Map.of("success", true)).build();
    }
}
