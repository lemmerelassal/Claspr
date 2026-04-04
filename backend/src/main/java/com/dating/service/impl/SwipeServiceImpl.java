package com.dating.service.impl;

import com.dating.dto.UserDtos.ProfileResponse;
import com.dating.dto.UserDtos.SwipeResponse;
import com.dating.entity.Match;
import com.dating.entity.Swipe;
import com.dating.entity.Swipe.SwipeDirection;
import com.dating.entity.UserProfile;
import com.dating.service.IProfileService;
import com.dating.service.ISwipeService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.UUID;

@ApplicationScoped
public class SwipeServiceImpl implements ISwipeService {

    @Inject
    IProfileService profileService;

    @Override
    @Transactional
    public SwipeResponse recordSwipe(UUID swiperId, UUID swipedId, String direction) {
        UserProfile swiper = profileService.getById(swiperId);
        UserProfile swiped = profileService.getById(swipedId);

        // Prevent duplicate swipes
        if (Swipe.findBySwipedPair(swiperId, swipedId) != null) {
            return new SwipeResponse(false, null, null);
        }

        var swipe = new Swipe();
        swipe.swiper = swiper;
        swipe.swiped = swiped;
        swipe.direction = SwipeDirection.valueOf(direction.toUpperCase());
        swipe.persist();

        // Check mutual match on RIGHT or SUPER_LIKE
        if (swipe.direction == SwipeDirection.RIGHT || swipe.direction == SwipeDirection.SUPER_LIKE) {
            Swipe reciprocal = Swipe.findBySwipedPair(swipedId, swiperId);
            if (reciprocal != null &&
                    (reciprocal.direction == SwipeDirection.RIGHT || reciprocal.direction == SwipeDirection.SUPER_LIKE)) {
                var match = new Match();
                match.user1 = swiper;
                match.user2 = swiped;
                match.persist();

                double distance = profileService.calculateDistance(swiper, swiped);
                var profile = new ProfileResponse(
                        swiped.id, swiped.displayName, swiped.getAge(), swiped.bio,
                        swiped.gender, swiped.photoUrls, swiped.interests,
                        swiped.city, Math.round(distance * 10.0) / 10.0
                );
                return new SwipeResponse(true, match.id, profile);
            }
        }

        return new SwipeResponse(false, null, null);
    }
}
