package com.dating.grpc;

import io.quarkus.grpc.MutinyService;

@jakarta.annotation.Generated(value = "by Mutiny Grpc generator", comments = "Source: dating.proto")
public interface ChatService extends MutinyService {

    io.smallrye.mutiny.Uni<com.dating.grpc.ChatMessageResponse> sendMessage(com.dating.grpc.ChatMessageRequest request);

    io.smallrye.mutiny.Uni<com.dating.grpc.ConversationResponse> getConversation(com.dating.grpc.ConversationRequest request);

    io.smallrye.mutiny.Uni<com.dating.grpc.MarkReadResponse> markRead(com.dating.grpc.MarkReadRequest request);

    io.smallrye.mutiny.Multi<com.dating.grpc.ChatMessage> streamMessages(com.dating.grpc.ConversationRequest request);
}
