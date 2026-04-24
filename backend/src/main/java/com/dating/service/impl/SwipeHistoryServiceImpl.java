package com.dating.service.impl;

import com.dating.entity.Swipe;
import com.dating.entity.UserProfile;
import com.dating.service.IGeoService;
import com.dating.service.IProfileService;
import com.dating.service.ISwipeHistoryService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@ApplicationScoped
public class SwipeHistoryServiceImpl implements ISwipeHistoryService {

    @Inject EntityManager em;
    @Inject IProfileService profileService;
    @Inject IGeoService geoService;

    @Override
    public List<Map<String, Object>> getSwipeHistory(UUID userId, String direction,
                                                      String namePrefix, String interest) {
        var jpql = new StringBuilder(
                "SELECT s FROM Swipe s JOIN FETCH s.swiped WHERE s.swiper.id = :uid");
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);

        if (direction != null && !direction.isBlank() && !direction.equalsIgnoreCase("ALL")) {
            switch (direction.toUpperCase()) {
                case "RIGHT" -> {
                    jpql.append(" AND (s.direction = :dir1 OR s.direction = :dir2)");
                    params.put("dir1", Swipe.SwipeDirection.RIGHT);
                    params.put("dir2", Swipe.SwipeDirection.SUPER_LIKE);
                }
                case "LEFT" -> {
                    jpql.append(" AND s.direction = :dir1");
                    params.put("dir1", Swipe.SwipeDirection.LEFT);
                }
            }
        }

        if (namePrefix != null && !namePrefix.isBlank()) {
            jpql.append(" AND LOWER(s.swiped.displayName) LIKE :namePrefix");
            params.put("namePrefix", namePrefix.toLowerCase() + "%");
        }

        if (interest != null && !interest.isBlank()) {
            jpql.append(" AND EXISTS (SELECT i FROM s.swiped.interests i WHERE LOWER(i) LIKE :interest)");
            params.put("interest", "%" + interest.toLowerCase() + "%");
        }

        jpql.append(" ORDER BY s.createdAt DESC");

        var query = em.createQuery(jpql.toString(), Swipe.class);
        params.forEach(query::setParameter);

        UserProfile viewer = profileService.getById(userId);
        return query.getResultList().stream()
                .map(s -> toEntry(s, viewer))
                .toList();
    }

    @Override
    public List<String> autocompleteName(UUID userId, String prefix) {
        if (prefix == null || prefix.isBlank()) return List.of();

        @SuppressWarnings("unchecked")
        List<String> names = em.createNativeQuery(
                        """
                        SELECT DISTINCT up.display_name FROM user_profiles up
                        INNER JOIN swipes s ON s.swiped_id = up.id
                        WHERE s.swiper_id = :uid AND starts_with(LOWER(up.display_name), :prefix)
                        ORDER BY up.display_name LIMIT 10
                        """)
                .setParameter("uid", userId)
                .setParameter("prefix", prefix.toLowerCase())
                .getResultList();
        return names;
    }

    private Map<String, Object> toEntry(Swipe s, UserProfile viewer) {
        UserProfile p = s.swiped;
        double distance = geoService.calculateDistance(viewer, p);
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
    }
}
