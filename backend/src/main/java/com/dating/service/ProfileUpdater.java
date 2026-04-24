package com.dating.service;

import com.dating.entity.UserProfile;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@ApplicationScoped
public class ProfileUpdater {

    public void apply(UserProfile user, Map<String, Object> fields) {
        set(fields, "display_name",          v -> user.displayName          = v.toString());
        set(fields, "bio",                   v -> user.bio                  = v.toString());
        set(fields, "max_distance_km",       v -> user.maxDistanceKm        = toInt(v));
        set(fields, "min_age_preference",    v -> user.minAgePreference     = toInt(v));
        set(fields, "max_age_preference",    v -> user.maxAgePreference     = toInt(v));
        set(fields, "weight",                v -> user.weight               = toDbl(v));
        set(fields, "weight_unit",           v -> user.weightUnit           = v.toString());
        set(fields, "height",                v -> user.height               = toDbl(v));
        set(fields, "height_unit",           v -> user.heightUnit           = v.toString());
        set(fields, "min_weight_preference", v -> user.minWeightPreference  = toDbl(v));
        set(fields, "max_weight_preference", v -> user.maxWeightPreference  = toDbl(v));
        set(fields, "min_height_preference", v -> user.minHeightPreference  = toDbl(v));
        set(fields, "max_height_preference", v -> user.maxHeightPreference  = toDbl(v));
        set(fields, "show_men",              v -> user.showMen              = toBool(v));
        set(fields, "show_women",            v -> user.showWomen            = toBool(v));
        set(fields, "show_mtf_trans",        v -> user.showMtfTrans         = toBool(v));
        set(fields, "show_ftm_trans",        v -> user.showFtmTrans         = toBool(v));
        set(fields, "show_non_binary",       v -> user.showNonBinary        = toBool(v));

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
    }

    private void set(Map<String, Object> fields, String key, Consumer<Object> setter) {
        if (!fields.containsKey(key)) return;
        Object val = fields.get(key);
        if (val == null || (val instanceof String s && s.isEmpty())) return;
        setter.accept(val);
    }

    private int toInt(Object v) {
        return v instanceof Number n ? n.intValue() : Integer.parseInt(v.toString());
    }

    private double toDbl(Object v) {
        return v instanceof Number n ? n.doubleValue() : Double.parseDouble(v.toString());
    }

    private boolean toBool(Object v) {
        return v instanceof Boolean b ? b : Boolean.parseBoolean(v.toString());
    }
}
