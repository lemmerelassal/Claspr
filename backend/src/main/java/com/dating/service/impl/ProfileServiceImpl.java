package com.dating.service.impl;

import com.dating.entity.UserProfile;
import com.dating.service.IProfileService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class ProfileServiceImpl implements IProfileService {

    @Override
    public UserProfile getById(UUID userId) {
        UserProfile user = UserProfile.findById(userId);
        if (user == null) throw new IllegalArgumentException("User not found");
        return user;
    }

    @Override
    @Transactional
    public UserProfile update(UUID userId, Map<String, Object> fields) {
        UserProfile user = getById(userId);

        updateIfPresent(fields, "display_name", v -> user.displayName = v.toString());
        updateIfPresent(fields, "bio", v -> user.bio = v.toString());
        updateIfPresent(fields, "max_distance_km", v -> user.maxDistanceKm = toInt(v));
        updateIfPresent(fields, "min_age_preference", v -> user.minAgePreference = toInt(v));
        updateIfPresent(fields, "max_age_preference", v -> user.maxAgePreference = toInt(v));
        updateIfPresent(fields, "weight", v -> user.weight = toDbl(v));
        updateIfPresent(fields, "weight_unit", v -> user.weightUnit = v.toString());
        updateIfPresent(fields, "height", v -> user.height = toDbl(v));
        updateIfPresent(fields, "height_unit", v -> user.heightUnit = v.toString());
        updateIfPresent(fields, "min_weight_preference", v -> user.minWeightPreference = toDbl(v));
        updateIfPresent(fields, "max_weight_preference", v -> user.maxWeightPreference = toDbl(v));
        updateIfPresent(fields, "min_height_preference", v -> user.minHeightPreference = toDbl(v));
        updateIfPresent(fields, "max_height_preference", v -> user.maxHeightPreference = toDbl(v));
        updateIfPresent(fields, "show_men", v -> user.showMen = toBool(v));
        updateIfPresent(fields, "show_women", v -> user.showWomen = toBool(v));
        updateIfPresent(fields, "show_mtf_trans", v -> user.showMtfTrans = toBool(v));
        updateIfPresent(fields, "show_ftm_trans", v -> user.showFtmTrans = toBool(v));
        updateIfPresent(fields, "show_non_binary", v -> user.showNonBinary = toBool(v));

        if (fields.containsKey("interests")) {
            user.interests.clear();
            @SuppressWarnings("unchecked")
            var list = (List<String>) fields.get("interests");
            if (list != null) user.interests.addAll(list);
        }
        if (fields.containsKey("photo_urls")) {
            user.photoUrls.clear();
            @SuppressWarnings("unchecked")
            var list = (List<String>) fields.get("photo_urls");
            if (list != null) user.photoUrls.addAll(list);
        }

        user.persist();
        return user;
    }

    @Override
    @Transactional
    public UserProfile updateLocation(UUID userId, double latitude, double longitude, String city) {
        UserProfile user = getById(userId);
        user.latitude = latitude;
        user.longitude = longitude;
        if (city != null && !city.isEmpty()) user.city = city;
        user.persist();
        return user;
    }

    @Override
    public double calculateDistance(UserProfile viewer, UserProfile target) {
        if (viewer.latitude == null || viewer.longitude == null ||
            target.latitude == null || target.longitude == null) return 0;
        final int R = 6371;
        double dLat = Math.toRadians(target.latitude - viewer.latitude);
        double dLon = Math.toRadians(target.longitude - viewer.longitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(viewer.latitude)) * Math.cos(Math.toRadians(target.latitude))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    private void updateIfPresent(Map<String, Object> fields, String key, java.util.function.Consumer<Object> setter) {
        if (fields.containsKey(key) && fields.get(key) != null) {
            Object val = fields.get(key);
            if (val instanceof String s && s.isEmpty()) return;
            setter.accept(val);
        }
    }

    private int toInt(Object v) { return v instanceof Number n ? n.intValue() : Integer.parseInt(v.toString()); }
    private double toDbl(Object v) { return v instanceof Number n ? n.doubleValue() : Double.parseDouble(v.toString()); }
    private boolean toBool(Object v) { return v instanceof Boolean b ? b : Boolean.parseBoolean(v.toString()); }
}
