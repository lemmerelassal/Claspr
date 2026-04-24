package com.dating.service;

import com.dating.domain.Coordinates;
import com.dating.entity.UserProfile;

public interface IGeoService {

    double calculateDistance(Coordinates from, Coordinates to);

    default double calculateDistance(UserProfile viewer, UserProfile target) {
        Coordinates from = Coordinates.from(viewer);
        Coordinates to = Coordinates.from(target);
        if (from == null || to == null) return 0.0;
        return calculateDistance(from, to);
    }
}
