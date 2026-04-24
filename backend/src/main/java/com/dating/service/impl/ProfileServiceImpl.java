package com.dating.service.impl;

import com.dating.entity.UserProfile;
import com.dating.service.IProfileService;
import com.dating.service.ProfileUpdater;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class ProfileServiceImpl implements IProfileService {

    @Inject
    ProfileUpdater profileUpdater;

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
        profileUpdater.apply(user, fields);
        user.persist();
        return user;
    }

    @Override
    @Transactional
    public UserProfile updateLocation(UUID userId, double latitude, double longitude, String city) {
        UserProfile user = getById(userId);
        user.latitude = latitude;
        user.longitude = longitude;
        if (city != null && !city.isBlank()) user.city = city;
        user.persist();
        return user;
    }
}
