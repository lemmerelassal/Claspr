package com.dating.domain;

import com.dating.entity.UserProfile;

public record Coordinates(double latitude, double longitude) {

    public static Coordinates from(UserProfile profile) {
        if (profile.latitude == null || profile.longitude == null) return null;
        return new Coordinates(profile.latitude, profile.longitude);
    }
}
