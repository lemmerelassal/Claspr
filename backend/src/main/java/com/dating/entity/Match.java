package com.dating.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "matches")
public class Match extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user1_id", nullable = false)
    public UserProfile user1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user2_id", nullable = false)
    public UserProfile user2;

    public boolean active = true;

    public LocalDateTime matchedAt;

    @PrePersist
    void onCreate() {
        matchedAt = LocalDateTime.now();
    }

    public static List<Match> findByUserId(UUID userId) {
        return list("(user1.id = ?1 or user2.id = ?1) and active = true", userId);
    }

    public static Match findActiveMatch(UUID user1Id, UUID user2Id) {
        return find("((user1.id = ?1 and user2.id = ?2) or (user1.id = ?2 and user2.id = ?1)) and active = true",
                user1Id, user2Id).firstResult();
    }

    public UserProfile getOtherUser(UUID userId) {
        return user1.id.equals(userId) ? user2 : user1;
    }
}
