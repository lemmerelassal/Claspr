package com.dating.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "swipes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"swiper_id", "swiped_id"})
})
public class Swipe extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "swiper_id", nullable = false)
    public UserProfile swiper;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "swiped_id", nullable = false)
    public UserProfile swiped;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public SwipeDirection direction;

    public LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum SwipeDirection {
        LEFT, RIGHT, SUPER_LIKE
    }

    public static Swipe findBySwipedPair(UUID swiperId, UUID swipedId) {
        return find("swiper.id = ?1 and swiped.id = ?2", swiperId, swipedId).firstResult();
    }

    public static List<Swipe> findRightSwipesBy(UUID userId) {
        return list("swiper.id = ?1 and (direction = ?2 or direction = ?3)",
                userId, SwipeDirection.RIGHT, SwipeDirection.SUPER_LIKE);
    }

    public static List<UUID> findAlreadySwipedIds(UUID userId) {
        return getEntityManager()
                .createQuery("SELECT s.swiped.id FROM Swipe s WHERE s.swiper.id = :uid", UUID.class)
                .setParameter("uid", userId)
                .getResultList();
    }
}
