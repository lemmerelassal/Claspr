package com.dating.service.impl;

import com.dating.dto.UserDtos.ProfileResponse;
import com.dating.dto.UserDtos.SwipeResponse;
import com.dating.entity.Match;
import com.dating.entity.Swipe;
import com.dating.entity.Swipe.SwipeDirection;
import com.dating.entity.UserProfile;
import com.dating.service.IGeoService;
import com.dating.service.IProfileService;
import com.dating.service.ISwipeService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.UUID;

@ApplicationScoped
public class SwipeServiceImpl implements ISwipeService {

    @Inject IProfileService profileService;
    @Inject IGeoService geoService;

    @Override
    @Transactional
    public SwipeResponse recordSwipe(UUID swiperId, UUID swipedId, String direction) {
        UserProfile swiper = profileService.getById(swiperId);
        UserProfile swiped = profileService.getById(swipedId);

        if (Swipe.findBySwipedPair(swiperId, swipedId) != null) {
            return new SwipeResponse(false, null, null);
        }

        var swipe = new Swipe();
        swipe.swiper = swiper;
        swipe.swiped = swiped;
        swipe.direction = SwipeDirection.valueOf(direction.toUpperCase());
        swipe.persist();

        return switch (swipe.direction) {
            case RIGHT, SUPER_LIKE -> checkMutualMatch(swiper, swiped, swipedId, swiperId);
            case LEFT -> new SwipeResponse(false, null, null);
        };
    }

    private SwipeResponse checkMutualMatch(UserProfile swiper, UserProfile swiped,
                                           UUID swipedId, UUID swiperId) {
        Swipe reciprocal = Swipe.findBySwipedPair(swipedId, swiperId);
        if (reciprocal == null) return new SwipeResponse(false, null, null);
        return switch (reciprocal.direction) {
            case RIGHT, SUPER_LIKE -> createMatch(swiper, swiped);
            case LEFT -> new SwipeResponse(false, null, null);
        };
    }

    private SwipeResponse createMatch(UserProfile swiper, UserProfile swiped) {
        var match = new Match();
        match.user1 = swiper;
        match.user2 = swiped;
        match.persist();

        double distance = geoService.calculateDistance(swiper, swiped);
        var profile = new ProfileResponse(
                swiped.id, swiped.displayName, swiped.getAge(), swiped.bio,
                swiped.gender, swiped.photoUrls, swiped.interests,
                swiped.city, Math.round(distance * 10.0) / 10.0
        );
        return new SwipeResponse(true, match.id, profile);
    }
}
