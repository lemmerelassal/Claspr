package com.dating.service.impl;

import com.dating.entity.Swipe;
import com.dating.entity.UserProfile;
import com.dating.service.ISwipeHistoryService;
import com.dating.service.IProfileService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class SwipeHistoryServiceImpl implements ISwipeHistoryService {

    @Inject
    EntityManager em;

    @Inject
    IProfileService profileService;

    @Override
    public List<Map<String, Object>> getSwipeHistory(UUID userId, String direction, String namePrefix, String interest) {
        // Build dynamic JPQL query
        StringBuilder jpql = new StringBuilder(
                "SELECT s FROM Swipe s JOIN FETCH s.swiped WHERE s.swiper.id = :uid");
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);

        // Filter by direction
        if (direction != null && !direction.isEmpty() && !direction.equalsIgnoreCase("ALL")) {
            if (direction.equalsIgnoreCase("RIGHT")) {
                jpql.append(" AND (s.direction = :dir1 OR s.direction = :dir2)");
                params.put("dir1", Swipe.SwipeDirection.RIGHT);
                params.put("dir2", Swipe.SwipeDirection.SUPER_LIKE);
            } else if (direction.equalsIgnoreCase("LEFT")) {
                jpql.append(" AND s.direction = :dir1");
                params.put("dir1", Swipe.SwipeDirection.LEFT);
            }
        }

        // Filter by name prefix using LOWER + LIKE (portable, same as starts_with)
        if (namePrefix != null && !namePrefix.isEmpty()) {
            jpql.append(" AND LOWER(s.swiped.displayName) LIKE :namePrefix");
            params.put("namePrefix", namePrefix.toLowerCase() + "%");
        }

        jpql.append(" ORDER BY s.createdAt DESC");

        var query = em.createQuery(jpql.toString(), Swipe.class);
        params.forEach(query::setParameter);
        List<Swipe> swipes = query.getResultList();

        UserProfile viewer = profileService.getById(userId);

        // Post-filter by interest (interests are a collection, hard to filter in JPQL)
        return swipes.stream()
                .filter(s -> {
                    if (interest == null || interest.isEmpty()) return true;
                    return s.swiped.interests != null &&
                            s.swiped.interests.stream()
                                    .anyMatch(i -> i.toLowerCase().contains(interest.toLowerCase()));
                })
                .map(s -> {
                    UserProfile p = s.swiped;
                    double distance = profileService.calculateDistance(viewer, p);
                    Map<String, Object> entry = new HashMap<>();
                    entry.put("swipe_id", s.id.toString());
                    entry.put("direction", s.direction.name());
                    entry.put("swiped_at", s.createdAt.toString());
                    entry.put("profile", Map.of(
                            "user_id", p.id.toString(),
                            "display_name", p.displayName,
                            "age", p.getAge(),
                            "bio", p.bio != null ? p.bio : "",
                            "photo_urls", p.photoUrls != null ? p.photoUrls : List.of(),
                            "interests", p.interests != null ? p.interests : Set.of(),
                            "location_city", p.city != null ? p.city : "",
                            "distance_km", Math.round(distance * 10.0) / 10.0,
                            "gender", p.gender != null ? p.gender.name() : "OTHER"
                    ));
                    return entry;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<String> autocompleteName(UUID userId, String prefix) {
        if (prefix == null || prefix.isEmpty()) return List.of();

        // Use native query for PostgreSQL starts_with function
        @SuppressWarnings("unchecked")
        List<String> names = em.createNativeQuery(
                        "SELECT DISTINCT up.display_name FROM user_profiles up " +
                                "INNER JOIN swipes s ON s.swiped_id = up.id " +
                                "WHERE s.swiper_id = :uid AND starts_with(LOWER(up.display_name), :prefix) " +
                                "ORDER BY up.display_name LIMIT 10")
                .setParameter("uid", userId)
                .setParameter("prefix", prefix.toLowerCase())
                .getResultList();

        return names;
    }
}
