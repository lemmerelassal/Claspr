package com.dating.service;

import com.dating.dto.UserDtos.*;
import com.dating.entity.*;
import com.dating.entity.Swipe.SwipeDirection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class MatchingService {

    @Inject
    EntityManager em;

    /**
     * Find potential matches for a user based on preferences and distance.
     * Excludes already-swiped profiles.
     */
    public List<ProfileResponse> getPotentialMatches(UUID userId, int limit) {
        UserProfile user = UserProfile.findById(userId);
        if (user == null) throw new IllegalArgumentException("User not found");

        List<UUID> alreadySwiped = Swipe.findAlreadySwipedIds(userId);
        alreadySwiped.add(userId); // exclude self

        // Native query for distance-based filtering using Haversine formula
        String sql = """
            SELECT u.* FROM user_profiles u
            WHERE u.id NOT IN (:excluded)
              AND u.active = true
              AND u.date_of_birth IS NOT NULL
              AND (
                6371 * acos(
                  cos(radians(:lat)) * cos(radians(u.latitude))
                  * cos(radians(u.longitude) - radians(:lon))
                  + sin(radians(:lat)) * sin(radians(u.latitude))
                )
              ) <= :maxDist
            ORDER BY random()
            LIMIT :lim
            """;

        @SuppressWarnings("unchecked")
        List<UserProfile> candidates = em.createNativeQuery(sql, UserProfile.class)
                .setParameter("excluded", alreadySwiped)
                .setParameter("lat", user.latitude != null ? user.latitude : 0)
                .setParameter("lon", user.longitude != null ? user.longitude : 0)
                .setParameter("maxDist", user.maxDistanceKm != null ? user.maxDistanceKm : 100)
                .setParameter("lim", limit)
                .getResultList();

        return candidates.stream()
                .map(c -> toProfileResponse(c, user))
                .collect(Collectors.toList());
    }

    /**
     * Record a swipe and check for mutual match.
     */
    @Transactional
    public SwipeResponse recordSwipe(UUID swiperId, UUID swipedId, String direction) {
        UserProfile swiper = UserProfile.findById(swiperId);
        UserProfile swiped = UserProfile.findById(swipedId);

        if (swiper == null || swiped == null) {
            throw new IllegalArgumentException("User not found");
        }

        // Prevent duplicate swipes
        Swipe existing = Swipe.findBySwipedPair(swiperId, swipedId);
        if (existing != null) {
            return new SwipeResponse(false, null, null);
        }

        var swipe = new Swipe();
        swipe.swiper = swiper;
        swipe.swiped = swiped;
        swipe.direction = SwipeDirection.valueOf(direction.toUpperCase());
        swipe.persist();

        // Check for mutual match (only on RIGHT or SUPER_LIKE)
        if (swipe.direction == SwipeDirection.RIGHT || swipe.direction == SwipeDirection.SUPER_LIKE) {
            Swipe reciprocal = Swipe.findBySwipedPair(swipedId, swiperId);
            if (reciprocal != null &&
                    (reciprocal.direction == SwipeDirection.RIGHT || reciprocal.direction == SwipeDirection.SUPER_LIKE)) {
                // It's a match!
                var match = new Match();
                match.user1 = swiper;
                match.user2 = swiped;
                match.persist();

                return new SwipeResponse(true, match.id, toProfileResponse(swiped, swiper));
            }
        }

        return new SwipeResponse(false, null, null);
    }

    /**
     * Get all active matches for a user.
     */
    public List<MatchResponse> getMatches(UUID userId) {
        List<Match> matches = Match.findByUserId(userId);

        return matches.stream().map(m -> {
            UserProfile other = m.getOtherUser(userId);
            UserProfile me = UserProfile.findById(userId);
            ChatMessageEntity lastMsg = ChatMessageEntity.findLastByMatchId(m.id);

            LastMessageDto lastMessageDto = null;
            if (lastMsg != null) {
                lastMessageDto = new LastMessageDto(
                        lastMsg.content,
                        lastMsg.sentAt.toString(),
                        lastMsg.sender.id.equals(userId)
                );
            }

            return new MatchResponse(
                    m.id,
                    toProfileResponse(other, me),
                    m.matchedAt.toString(),
                    lastMessageDto
            );
        }).collect(Collectors.toList());
    }

    @Transactional
    public void unmatch(UUID userId, UUID matchId) {
        Match match = Match.findById(matchId);
        if (match == null) throw new IllegalArgumentException("Match not found");
        if (!match.user1.id.equals(userId) && !match.user2.id.equals(userId)) {
            throw new SecurityException("Not authorized");
        }
        match.active = false;
        match.persist();
    }

    private ProfileResponse toProfileResponse(UserProfile profile, UserProfile viewer) {
        double distance = calculateDistance(
                viewer.latitude, viewer.longitude,
                profile.latitude, profile.longitude
        );
        return new ProfileResponse(
                profile.id,
                profile.displayName,
                profile.getAge(),
                profile.bio,
                profile.gender,
                profile.photoUrls,
                profile.interests,
                profile.city,
                Math.round(distance * 10.0) / 10.0
        );
    }

    private double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        if (lat1 == null || lon1 == null || lat2 == null || lon2 == null) return 0;
        final int R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
