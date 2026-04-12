package com.dating.grpc;

import com.dating.entity.ChatMessageEntity;
import com.dating.service.IChatService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import io.quarkus.grpc.GrpcService;
import jakarta.inject.Inject;
import java.util.UUID;

@GrpcService
public class ChatGrpcService extends ChatServiceGrpc.ChatServiceImplBase {

    @Inject
    IChatService chatService;

    @Override
    public void sendMessage(ChatMessageRequest request, StreamObserver<ChatMessageResponse> responseObserver) {
        try {
            UUID matchId = UUID.fromString(request.getMatchId());
            UUID senderId = UUID.fromString(request.getSenderId());

            var msg = chatService.sendMessage(matchId, senderId,
                    request.getContent(), request.getType().name());

            responseObserver.onNext(ChatMessageResponse.newBuilder()
                    .setMessageId(msg.id().toString())
                    .setSentAt(msg.sentAt())
                    .setDelivered(true)
                    .build());
            responseObserver.onCompleted();
        } catch (SecurityException e) {
            responseObserver.onError(Status.PERMISSION_DENIED
                    .withDescription(e.getMessage()).asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void getConversation(ConversationRequest request, StreamObserver<ConversationResponse> responseObserver) {
        try {
            UUID matchId = UUID.fromString(request.getMatchId());
            UUID userId = UUID.fromString(request.getUserId());
            int page = request.getPage();
            int size = request.getSize() > 0 ? request.getSize() : 50;

            var messages = chatService.getConversation(matchId, userId, page, size);

            ConversationResponse.Builder builder = ConversationResponse.newBuilder()
                    .setTotal(messages.size());

            messages.forEach(m -> builder.addMessages(ChatMessage.newBuilder()
                    .setMessageId(m.id().toString())
                    .setSenderId(m.senderId().toString())
                    .setContent(m.content())
                    .setType(MessageType.valueOf(m.type()))
                    .setSentAt(m.sentAt())
                    .setRead(m.read())
                    .build()));

            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        } catch (SecurityException e) {
            responseObserver.onError(Status.PERMISSION_DENIED
                    .withDescription(e.getMessage()).asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void streamMessages(ConversationRequest request, StreamObserver<ChatMessage> responseObserver) {
        try {
            UUID matchId = UUID.fromString(request.getMatchId());
            UUID userId = UUID.fromString(request.getUserId());

            // Server-streaming: poll for new messages every 2 seconds
            // In production, replace with event-driven approach (CDC, Redis Pub/Sub)
            Thread pollingThread = new Thread(() -> {
                String lastMessageId = "";
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        var messages = chatService.getConversation(matchId, userId, 0, 10);
                        for (var m : messages) {
                            if (!m.id().toString().equals(lastMessageId)) {
                                responseObserver.onNext(ChatMessage.newBuilder()
                                        .setMessageId(m.id().toString())
                                        .setSenderId(m.senderId().toString())
                                        .setContent(m.content())
                                        .setType(MessageType.valueOf(m.type()))
                                        .setSentAt(m.sentAt())
                                        .setRead(m.read())
                                        .build());
                                lastMessageId = m.id().toString();
                            }
                        }
                        Thread.sleep(2000);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    responseObserver.onError(Status.INTERNAL
                            .withDescription(e.getMessage()).asRuntimeException());
                }
            });
            pollingThread.setDaemon(true);
            pollingThread.start();

        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void markRead(MarkReadRequest request, StreamObserver<MarkReadResponse> responseObserver) {
        try {
            UUID matchId = UUID.fromString(request.getMatchId());
            UUID userId = UUID.fromString(request.getUserId());

            chatService.markAsRead(matchId, userId);

            responseObserver.onNext(MarkReadResponse.newBuilder()
                    .setMarkedCount(1)
                    .build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage()).asRuntimeException());
        }
    }
}
