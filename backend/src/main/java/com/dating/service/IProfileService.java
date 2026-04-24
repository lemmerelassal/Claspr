package com.dating.service;

import com.dating.entity.UserProfile;
import java.util.Map;
import java.util.UUID;

public interface IProfileService {

    UserProfile getById(UUID userId);

    UserProfile update(UUID userId, Map<String, Object> fields);

    UserProfile updateLocation(UUID userId, double latitude, double longitude, String city);
}
