package com.dating.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "chat_messages")
public class ChatMessageEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    public Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    public UserProfile sender;

    @Column(nullable = false, length = 2000)
    public String content;

    @Enumerated(EnumType.STRING)
    public MessageType messageType = MessageType.TEXT;

    public boolean read = false;

    public LocalDateTime sentAt;

    @PrePersist
    void onCreate() {
        sentAt = LocalDateTime.now();
    }

    public enum MessageType {
        TEXT, IMAGE, GIF
    }

    public static List<ChatMessageEntity> findByMatchId(UUID matchId, int page, int size) {
        return find("match.id = ?1 ORDER BY sentAt DESC", matchId)
                .page(page, size)
                .list();
    }

    public static ChatMessageEntity findLastByMatchId(UUID matchId) {
        return find("match.id = ?1 ORDER BY sentAt DESC", matchId).firstResult();
    }

    public static long countByMatchId(UUID matchId) {
        return count("match.id", matchId);
    }
}
