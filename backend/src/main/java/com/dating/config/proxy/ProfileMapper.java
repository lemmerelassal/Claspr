package com.dating.config.proxy;

import com.dating.entity.UserProfile;
import java.util.*;

/**
 * Maps UserProfile entities to JSON-compatible response maps.
 * Shared across proxy resources to avoid duplication.
 */
public final class ProfileMapper {

    private ProfileMapper() {}

    public static Map<String, Object> toMap(UserProfile u, double distance) {
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
        m.put("show_men", u.showMen);
        m.put("show_women", u.showWomen);
        m.put("show_mtf_trans", u.showMtfTrans);
        m.put("show_ftm_trans", u.showFtmTrans);
        m.put("show_non_binary", u.showNonBinary);
        return m;
    }

    public static Map<String, Object> toCard(com.dating.dto.UserDtos.ProfileResponse p) {
        return Map.of(
                "user_id", p.id().toString(),
                "display_name", p.displayName(),
                "age", p.age(),
                "bio", p.bio() != null ? p.bio() : "",
                "photo_urls", p.photoUrls() != null ? p.photoUrls() : List.of(),
                "distance_km", p.distanceKm(),
                "interests", p.interests() != null ? p.interests() : Set.of(),
                "location_city", p.city() != null ? p.city() : ""
        );
    }

    // ── Helpers for parsing request bodies ──────────────
    public static String str(Map<String, Object> m, String key) {
        Object v = m.get(key);
        return v != null ? v.toString() : "";
    }

    public static java.util.UUID uuid(Map<String, Object> m, String key) {
        return java.util.UUID.fromString(str(m, key));
    }

    public static int intVal(Map<String, Object> m, String key) {
        Object v = m.get(key);
        if (v instanceof Number n) return n.intValue();
        return Integer.parseInt(v.toString());
    }

    public static double dblVal(Map<String, Object> m, String key) {
        Object v = m.get(key);
        if (v instanceof Number n) return n.doubleValue();
        return Double.parseDouble(v.toString());
    }
}
