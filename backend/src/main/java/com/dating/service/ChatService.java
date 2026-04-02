package com.dating.service;

import com.dating.dto.UserDtos.*;
import com.dating.entity.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class ChatService {

    @Transactional
    public MessageResponse sendMessage(UUID matchId, UUID senderId, String content, String type) {
        Match match = Match.findById(matchId);
        if (match == null || !match.active) {
            throw new IllegalArgumentException("Match not found or inactive");
        }
        if (!match.user1.id.equals(senderId) && !match.user2.id.equals(senderId)) {
            throw new SecurityException("Not part of this match");
        }

        var msg = new ChatMessageEntity();
        msg.match = match;
        msg.sender = UserProfile.findById(senderId);
        msg.content = content;
        msg.messageType = type != null
                ? ChatMessageEntity.MessageType.valueOf(type.toUpperCase())
                : ChatMessageEntity.MessageType.TEXT;
        msg.persist();

        return new MessageResponse(
                msg.id, msg.sender.id, msg.content,
                msg.messageType.name(), msg.sentAt.toString(), msg.read
        );
    }

    public List<MessageResponse> getConversation(UUID matchId, UUID userId, int page, int size) {
        Match match = Match.findById(matchId);
        if (match == null) throw new IllegalArgumentException("Match not found");
        if (!match.user1.id.equals(userId) && !match.user2.id.equals(userId)) {
            throw new SecurityException("Not part of this match");
        }

        return ChatMessageEntity.findByMatchId(matchId, page, size)
                .stream()
                .map(m -> new MessageResponse(
                        m.id, m.sender.id, m.content,
                        m.messageType.name(), m.sentAt.toString(), m.read
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void markAsRead(UUID matchId, UUID userId) {
        ChatMessageEntity.update(
                "read = true WHERE match.id = ?1 AND sender.id != ?2 AND read = false",
                matchId, userId
        );
    }
}
