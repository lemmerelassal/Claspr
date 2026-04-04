package com.dating.service;

import com.dating.dto.UserDtos.MessageResponse;
import java.util.List;
import java.util.UUID;

/**
 * Chat service contract.
 * Handles messaging between matched users.
 */
public interface IChatService {

    MessageResponse sendMessage(UUID matchId, UUID senderId, String content, String type);

    List<MessageResponse> getConversation(UUID matchId, UUID userId, int page, int size);

    void markAsRead(UUID matchId, UUID userId);
}
