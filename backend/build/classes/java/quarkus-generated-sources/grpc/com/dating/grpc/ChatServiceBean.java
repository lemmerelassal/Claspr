package com.dating.grpc;

import io.grpc.BindableService;
import io.quarkus.grpc.GrpcService;
import io.quarkus.grpc.MutinyBean;

@jakarta.annotation.Generated(value = "by Mutiny Grpc generator", comments = "Source: dating.proto")
public class ChatServiceBean extends MutinyChatServiceGrpc.ChatServiceImplBase implements BindableService, MutinyBean {

    private final ChatService delegate;

    ChatServiceBean(@GrpcService ChatService delegate) {
        this.delegate = delegate;
    }

    @Override
    public io.smallrye.mutiny.Uni<com.dating.grpc.ChatMessageResponse> sendMessage(com.dating.grpc.ChatMessageRequest request) {
        try {
            return delegate.sendMessage(request);
        } catch (UnsupportedOperationException e) {
            throw new io.grpc.StatusRuntimeException(io.grpc.Status.UNIMPLEMENTED);
        }
    }

    @Override
    public io.smallrye.mutiny.Uni<com.dating.grpc.ConversationResponse> getConversation(com.dating.grpc.ConversationRequest request) {
        try {
            return delegate.getConversation(request);
        } catch (UnsupportedOperationException e) {
            throw new io.grpc.StatusRuntimeException(io.grpc.Status.UNIMPLEMENTED);
        }
    }

    @Override
    public io.smallrye.mutiny.Uni<com.dating.grpc.MarkReadResponse> markRead(com.dating.grpc.MarkReadRequest request) {
        try {
            return delegate.markRead(request);
        } catch (UnsupportedOperationException e) {
            throw new io.grpc.StatusRuntimeException(io.grpc.Status.UNIMPLEMENTED);
        }
    }

    @Override
    public io.smallrye.mutiny.Multi<com.dating.grpc.ChatMessage> streamMessages(com.dating.grpc.ConversationRequest request) {
        try {
            return delegate.streamMessages(request);
        } catch (UnsupportedOperationException e) {
            throw new io.grpc.StatusRuntimeException(io.grpc.Status.UNIMPLEMENTED);
        }
    }
}
