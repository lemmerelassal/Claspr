package com.dating.service.impl;

import com.dating.domain.Coordinates;
import com.dating.service.IGeoService;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GeoServiceImpl implements IGeoService {

    private static final int EARTH_RADIUS_KM = 6371;

    @Override
    public double calculateDistance(Coordinates from, Coordinates to) {
        double dLat = Math.toRadians(to.latitude() - from.latitude());
        double dLon = Math.toRadians(to.longitude() - from.longitude());
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(from.latitude())) * Math.cos(Math.toRadians(to.latitude()))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return EARTH_RADIUS_KM * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
