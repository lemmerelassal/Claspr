package com.dating.grpc;

import static com.dating.grpc.ChatServiceGrpc.getServiceDescriptor;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;

@jakarta.annotation.Generated(value = "by Mutiny Grpc generator", comments = "Source: dating.proto")
public final class MutinyChatServiceGrpc implements io.quarkus.grpc.MutinyGrpc {

    private MutinyChatServiceGrpc() {
    }

    public static MutinyChatServiceStub newMutinyStub(io.grpc.Channel channel) {
        return new MutinyChatServiceStub(channel);
    }

    /**
     * <pre>
     *  ── Chat Service ───────────────────────────────────────
     * </pre>
     */
    public static class MutinyChatServiceStub extends io.grpc.stub.AbstractStub<MutinyChatServiceStub> implements io.quarkus.grpc.MutinyStub {

        private ChatServiceGrpc.ChatServiceStub delegateStub;

        private MutinyChatServiceStub(io.grpc.Channel channel) {
            super(channel);
            delegateStub = ChatServiceGrpc.newStub(channel);
        }

        private MutinyChatServiceStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
            delegateStub = ChatServiceGrpc.newStub(channel).build(channel, callOptions);
        }

        @Override
        protected MutinyChatServiceStub build(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new MutinyChatServiceStub(channel, callOptions);
        }

        public io.smallrye.mutiny.Uni<com.dating.grpc.ChatMessageResponse> sendMessage(com.dating.grpc.ChatMessageRequest request) {
            return io.quarkus.grpc.stubs.ClientCalls.oneToOne(request, delegateStub::sendMessage);
        }

        public io.smallrye.mutiny.Uni<com.dating.grpc.ConversationResponse> getConversation(com.dating.grpc.ConversationRequest request) {
            return io.quarkus.grpc.stubs.ClientCalls.oneToOne(request, delegateStub::getConversation);
        }

        public io.smallrye.mutiny.Uni<com.dating.grpc.MarkReadResponse> markRead(com.dating.grpc.MarkReadRequest request) {
            return io.quarkus.grpc.stubs.ClientCalls.oneToOne(request, delegateStub::markRead);
        }

        public io.smallrye.mutiny.Multi<com.dating.grpc.ChatMessage> streamMessages(com.dating.grpc.ConversationRequest request) {
            return io.quarkus.grpc.stubs.ClientCalls.oneToMany(request, delegateStub::streamMessages);
        }
    }

    /**
     * <pre>
     *  ── Chat Service ───────────────────────────────────────
     * </pre>
     */
    public static abstract class ChatServiceImplBase implements io.grpc.BindableService {

        private String compression;

        /**
         * Set whether the server will try to use a compressed response.
         *
         * @param compression the compression, e.g {@code gzip}
         */
        public ChatServiceImplBase withCompression(String compression) {
            this.compression = compression;
            return this;
        }

        public io.smallrye.mutiny.Uni<com.dating.grpc.ChatMessageResponse> sendMessage(com.dating.grpc.ChatMessageRequest request) {
            throw new io.grpc.StatusRuntimeException(io.grpc.Status.UNIMPLEMENTED);
        }

        public io.smallrye.mutiny.Uni<com.dating.grpc.ConversationResponse> getConversation(com.dating.grpc.ConversationRequest request) {
            throw new io.grpc.StatusRuntimeException(io.grpc.Status.UNIMPLEMENTED);
        }

        public io.smallrye.mutiny.Uni<com.dating.grpc.MarkReadResponse> markRead(com.dating.grpc.MarkReadRequest request) {
            throw new io.grpc.StatusRuntimeException(io.grpc.Status.UNIMPLEMENTED);
        }

        public io.smallrye.mutiny.Multi<com.dating.grpc.ChatMessage> streamMessages(com.dating.grpc.ConversationRequest request) {
            throw new io.grpc.StatusRuntimeException(io.grpc.Status.UNIMPLEMENTED);
        }

        @java.lang.Override
        public io.grpc.ServerServiceDefinition bindService() {
            return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor()).addMethod(com.dating.grpc.ChatServiceGrpc.getSendMessageMethod(), asyncUnaryCall(new MethodHandlers<com.dating.grpc.ChatMessageRequest, com.dating.grpc.ChatMessageResponse>(this, METHODID_SEND_MESSAGE, compression))).addMethod(com.dating.grpc.ChatServiceGrpc.getGetConversationMethod(), asyncUnaryCall(new MethodHandlers<com.dating.grpc.ConversationRequest, com.dating.grpc.ConversationResponse>(this, METHODID_GET_CONVERSATION, compression))).addMethod(com.dating.grpc.ChatServiceGrpc.getStreamMessagesMethod(), asyncServerStreamingCall(new MethodHandlers<com.dating.grpc.ConversationRequest, com.dating.grpc.ChatMessage>(this, METHODID_STREAM_MESSAGES, compression))).addMethod(com.dating.grpc.ChatServiceGrpc.getMarkReadMethod(), asyncUnaryCall(new MethodHandlers<com.dating.grpc.MarkReadRequest, com.dating.grpc.MarkReadResponse>(this, METHODID_MARK_READ, compression))).build();
        }
    }

    private static final int METHODID_SEND_MESSAGE = 0;

    private static final int METHODID_GET_CONVERSATION = 1;

    private static final int METHODID_STREAM_MESSAGES = 2;

    private static final int METHODID_MARK_READ = 3;

    private static final class MethodHandlers<Req, Resp> implements io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>, io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>, io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>, io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {

        private final ChatServiceImplBase serviceImpl;

        private final int methodId;

        private final String compression;

        MethodHandlers(ChatServiceImplBase serviceImpl, int methodId, String compression) {
            this.serviceImpl = serviceImpl;
            this.methodId = methodId;
            this.compression = compression;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("unchecked")
        public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch(methodId) {
                case METHODID_SEND_MESSAGE:
                    io.quarkus.grpc.stubs.ServerCalls.oneToOne((com.dating.grpc.ChatMessageRequest) request, (io.grpc.stub.StreamObserver<com.dating.grpc.ChatMessageResponse>) responseObserver, compression, serviceImpl::sendMessage);
                    break;
                case METHODID_GET_CONVERSATION:
                    io.quarkus.grpc.stubs.ServerCalls.oneToOne((com.dating.grpc.ConversationRequest) request, (io.grpc.stub.StreamObserver<com.dating.grpc.ConversationResponse>) responseObserver, compression, serviceImpl::getConversation);
                    break;
                case METHODID_STREAM_MESSAGES:
                    io.quarkus.grpc.stubs.ServerCalls.oneToMany((com.dating.grpc.ConversationRequest) request, (io.grpc.stub.StreamObserver<com.dating.grpc.ChatMessage>) responseObserver, compression, serviceImpl::streamMessages);
                    break;
                case METHODID_MARK_READ:
                    io.quarkus.grpc.stubs.ServerCalls.oneToOne((com.dating.grpc.MarkReadRequest) request, (io.grpc.stub.StreamObserver<com.dating.grpc.MarkReadResponse>) responseObserver, compression, serviceImpl::markRead);
                    break;
                default:
                    throw new java.lang.AssertionError();
            }
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("unchecked")
        public io.grpc.stub.StreamObserver<Req> invoke(io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch(methodId) {
                default:
                    throw new java.lang.AssertionError();
            }
        }
    }
}
