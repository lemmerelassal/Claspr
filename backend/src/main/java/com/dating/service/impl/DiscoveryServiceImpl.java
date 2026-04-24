package com.dating.service.impl;

import com.dating.dto.UserDtos.ProfileResponse;
import com.dating.entity.Swipe;
import com.dating.entity.UserProfile;
import com.dating.service.GenderPreferenceFilter;
import com.dating.service.IDiscoveryService;
import com.dating.service.IGeoService;
import com.dating.service.IProfileService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class DiscoveryServiceImpl implements IDiscoveryService {

    @Inject IProfileService profileService;
    @Inject IGeoService geoService;
    @Inject GenderPreferenceFilter genderFilter;

    @Override
    public List<ProfileResponse> getPotentialMatches(UUID userId, int limit) {
        UserProfile user = profileService.getById(userId);

        List<UUID> alreadySwiped = Swipe.findAlreadySwipedIds(userId);
        alreadySwiped.add(userId);

        List<UserProfile.Gender> allowedGenders = genderFilter.getAllowedGenders(user);
        if (allowedGenders.isEmpty()) return List.of();

        List<UserProfile> candidates;
        if (alreadySwiped.size() == 1) {
            candidates = UserProfile.find(
                    "active = true AND dateOfBirth IS NOT NULL AND id != ?1 AND gender IN ?2",
                    userId, allowedGenders
            ).page(0, limit).list();
        } else {
            candidates = UserProfile.find(
                    "active = true AND dateOfBirth IS NOT NULL AND id NOT IN ?1 AND gender IN ?2",
                    alreadySwiped, allowedGenders
            ).page(0, limit).list();
        }

        return candidates.stream()
                .map(c -> toProfileResponse(c, user))
                .toList();
    }

    private ProfileResponse toProfileResponse(UserProfile profile, UserProfile viewer) {
        double distance = geoService.calculateDistance(viewer, profile);
        return new ProfileResponse(
                profile.id, profile.displayName, profile.getAge(), profile.bio,
                profile.gender, profile.photoUrls, profile.interests,
                profile.city, Math.round(distance * 10.0) / 10.0
        );
    }
}
