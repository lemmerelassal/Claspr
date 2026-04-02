package com.dating.grpc;

import java.util.function.BiFunction;
import io.quarkus.grpc.MutinyClient;

@jakarta.annotation.Generated(value = "by Mutiny Grpc generator", comments = "Source: dating.proto")
public class ChatServiceClient implements ChatService, MutinyClient<MutinyChatServiceGrpc.MutinyChatServiceStub> {

    private final MutinyChatServiceGrpc.MutinyChatServiceStub stub;

    public ChatServiceClient(String name, io.grpc.Channel channel, BiFunction<String, MutinyChatServiceGrpc.MutinyChatServiceStub, MutinyChatServiceGrpc.MutinyChatServiceStub> stubConfigurator) {
        this.stub = stubConfigurator.apply(name, MutinyChatServiceGrpc.newMutinyStub(channel));
    }

    private ChatServiceClient(MutinyChatServiceGrpc.MutinyChatServiceStub stub) {
        this.stub = stub;
    }

    public ChatServiceClient newInstanceWithStub(MutinyChatServiceGrpc.MutinyChatServiceStub stub) {
        return new ChatServiceClient(stub);
    }

    @Override
    public MutinyChatServiceGrpc.MutinyChatServiceStub getStub() {
        return stub;
    }

    @Override
    public io.smallrye.mutiny.Uni<com.dating.grpc.ChatMessageResponse> sendMessage(com.dating.grpc.ChatMessageRequest request) {
        return stub.sendMessage(request);
    }

    @Override
    public io.smallrye.mutiny.Uni<com.dating.grpc.ConversationResponse> getConversation(com.dating.grpc.ConversationRequest request) {
        return stub.getConversation(request);
    }

    @Override
    public io.smallrye.mutiny.Uni<com.dating.grpc.MarkReadResponse> markRead(com.dating.grpc.MarkReadRequest request) {
        return stub.markRead(request);
    }

    @Override
    public io.smallrye.mutiny.Multi<com.dating.grpc.ChatMessage> streamMessages(com.dating.grpc.ConversationRequest request) {
        return stub.streamMessages(request);
    }
}
