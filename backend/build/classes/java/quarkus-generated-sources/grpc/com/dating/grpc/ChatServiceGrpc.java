package com.dating.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * ── Chat Service ───────────────────────────────────────
 * </pre>
 */
@io.quarkus.Generated(value = "by gRPC proto compiler (version 1.59.1)", comments = "Source: dating.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ChatServiceGrpc {

    private ChatServiceGrpc() {
    }

    public static final java.lang.String SERVICE_NAME = "dating.ChatService";

    // Static method descriptors that strictly reflect the proto.
    private static volatile io.grpc.MethodDescriptor<com.dating.grpc.ChatMessageRequest, com.dating.grpc.ChatMessageResponse> getSendMessageMethod;

    @io.grpc.stub.annotations.RpcMethod(fullMethodName = SERVICE_NAME + '/' + "SendMessage", requestType = com.dating.grpc.ChatMessageRequest.class, responseType = com.dating.grpc.ChatMessageResponse.class, methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
    public static io.grpc.MethodDescriptor<com.dating.grpc.ChatMessageRequest, com.dating.grpc.ChatMessageResponse> getSendMessageMethod() {
        io.grpc.MethodDescriptor<com.dating.grpc.ChatMessageRequest, com.dating.grpc.ChatMessageResponse> getSendMessageMethod;
        if ((getSendMessageMethod = ChatServiceGrpc.getSendMessageMethod) == null) {
            synchronized (ChatServiceGrpc.class) {
                if ((getSendMessageMethod = ChatServiceGrpc.getSendMessageMethod) == null) {
                    ChatServiceGrpc.getSendMessageMethod = getSendMessageMethod = io.grpc.MethodDescriptor.<com.dating.grpc.ChatMessageRequest, com.dating.grpc.ChatMessageResponse>newBuilder().setType(io.grpc.MethodDescriptor.MethodType.UNARY).setFullMethodName(generateFullMethodName(SERVICE_NAME, "SendMessage")).setSampledToLocalTracing(true).setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(com.dating.grpc.ChatMessageRequest.getDefaultInstance())).setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(com.dating.grpc.ChatMessageResponse.getDefaultInstance())).setSchemaDescriptor(new ChatServiceMethodDescriptorSupplier("SendMessage")).build();
                }
            }
        }
        return getSendMessageMethod;
    }

    private static volatile io.grpc.MethodDescriptor<com.dating.grpc.ConversationRequest, com.dating.grpc.ConversationResponse> getGetConversationMethod;

    @io.grpc.stub.annotations.RpcMethod(fullMethodName = SERVICE_NAME + '/' + "GetConversation", requestType = com.dating.grpc.ConversationRequest.class, responseType = com.dating.grpc.ConversationResponse.class, methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
    public static io.grpc.MethodDescriptor<com.dating.grpc.ConversationRequest, com.dating.grpc.ConversationResponse> getGetConversationMethod() {
        io.grpc.MethodDescriptor<com.dating.grpc.ConversationRequest, com.dating.grpc.ConversationResponse> getGetConversationMethod;
        if ((getGetConversationMethod = ChatServiceGrpc.getGetConversationMethod) == null) {
            synchronized (ChatServiceGrpc.class) {
                if ((getGetConversationMethod = ChatServiceGrpc.getGetConversationMethod) == null) {
                    ChatServiceGrpc.getGetConversationMethod = getGetConversationMethod = io.grpc.MethodDescriptor.<com.dating.grpc.ConversationRequest, com.dating.grpc.ConversationResponse>newBuilder().setType(io.grpc.MethodDescriptor.MethodType.UNARY).setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetConversation")).setSampledToLocalTracing(true).setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(com.dating.grpc.ConversationRequest.getDefaultInstance())).setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(com.dating.grpc.ConversationResponse.getDefaultInstance())).setSchemaDescriptor(new ChatServiceMethodDescriptorSupplier("GetConversation")).build();
                }
            }
        }
        return getGetConversationMethod;
    }

    private static volatile io.grpc.MethodDescriptor<com.dating.grpc.ConversationRequest, com.dating.grpc.ChatMessage> getStreamMessagesMethod;

    @io.grpc.stub.annotations.RpcMethod(fullMethodName = SERVICE_NAME + '/' + "StreamMessages", requestType = com.dating.grpc.ConversationRequest.class, responseType = com.dating.grpc.ChatMessage.class, methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
    public static io.grpc.MethodDescriptor<com.dating.grpc.ConversationRequest, com.dating.grpc.ChatMessage> getStreamMessagesMethod() {
        io.grpc.MethodDescriptor<com.dating.grpc.ConversationRequest, com.dating.grpc.ChatMessage> getStreamMessagesMethod;
        if ((getStreamMessagesMethod = ChatServiceGrpc.getStreamMessagesMethod) == null) {
            synchronized (ChatServiceGrpc.class) {
                if ((getStreamMessagesMethod = ChatServiceGrpc.getStreamMessagesMethod) == null) {
                    ChatServiceGrpc.getStreamMessagesMethod = getStreamMessagesMethod = io.grpc.MethodDescriptor.<com.dating.grpc.ConversationRequest, com.dating.grpc.ChatMessage>newBuilder().setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING).setFullMethodName(generateFullMethodName(SERVICE_NAME, "StreamMessages")).setSampledToLocalTracing(true).setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(com.dating.grpc.ConversationRequest.getDefaultInstance())).setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(com.dating.grpc.ChatMessage.getDefaultInstance())).setSchemaDescriptor(new ChatServiceMethodDescriptorSupplier("StreamMessages")).build();
                }
            }
        }
        return getStreamMessagesMethod;
    }

    private static volatile io.grpc.MethodDescriptor<com.dating.grpc.MarkReadRequest, com.dating.grpc.MarkReadResponse> getMarkReadMethod;

    @io.grpc.stub.annotations.RpcMethod(fullMethodName = SERVICE_NAME + '/' + "MarkRead", requestType = com.dating.grpc.MarkReadRequest.class, responseType = com.dating.grpc.MarkReadResponse.class, methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
    public static io.grpc.MethodDescriptor<com.dating.grpc.MarkReadRequest, com.dating.grpc.MarkReadResponse> getMarkReadMethod() {
        io.grpc.MethodDescriptor<com.dating.grpc.MarkReadRequest, com.dating.grpc.MarkReadResponse> getMarkReadMethod;
        if ((getMarkReadMethod = ChatServiceGrpc.getMarkReadMethod) == null) {
            synchronized (ChatServiceGrpc.class) {
                if ((getMarkReadMethod = ChatServiceGrpc.getMarkReadMethod) == null) {
                    ChatServiceGrpc.getMarkReadMethod = getMarkReadMethod = io.grpc.MethodDescriptor.<com.dating.grpc.MarkReadRequest, com.dating.grpc.MarkReadResponse>newBuilder().setType(io.grpc.MethodDescriptor.MethodType.UNARY).setFullMethodName(generateFullMethodName(SERVICE_NAME, "MarkRead")).setSampledToLocalTracing(true).setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(com.dating.grpc.MarkReadRequest.getDefaultInstance())).setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(com.dating.grpc.MarkReadResponse.getDefaultInstance())).setSchemaDescriptor(new ChatServiceMethodDescriptorSupplier("MarkRead")).build();
                }
            }
        }
        return getMarkReadMethod;
    }

    /**
     * Creates a new async stub that supports all call types for the service
     */
    public static ChatServiceStub newStub(io.grpc.Channel channel) {
        io.grpc.stub.AbstractStub.StubFactory<ChatServiceStub> factory = new io.grpc.stub.AbstractStub.StubFactory<ChatServiceStub>() {

            @java.lang.Override
            public ChatServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                return new ChatServiceStub(channel, callOptions);
            }
        };
        return ChatServiceStub.newStub(factory, channel);
    }

    /**
     * Creates a new blocking-style stub that supports unary and streaming output calls on the service
     */
    public static ChatServiceBlockingStub newBlockingStub(io.grpc.Channel channel) {
        io.grpc.stub.AbstractStub.StubFactory<ChatServiceBlockingStub> factory = new io.grpc.stub.AbstractStub.StubFactory<ChatServiceBlockingStub>() {

            @java.lang.Override
            public ChatServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                return new ChatServiceBlockingStub(channel, callOptions);
            }
        };
        return ChatServiceBlockingStub.newStub(factory, channel);
    }

    /**
     * Creates a new ListenableFuture-style stub that supports unary calls on the service
     */
    public static ChatServiceFutureStub newFutureStub(io.grpc.Channel channel) {
        io.grpc.stub.AbstractStub.StubFactory<ChatServiceFutureStub> factory = new io.grpc.stub.AbstractStub.StubFactory<ChatServiceFutureStub>() {

            @java.lang.Override
            public ChatServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                return new ChatServiceFutureStub(channel, callOptions);
            }
        };
        return ChatServiceFutureStub.newStub(factory, channel);
    }

    /**
     * <pre>
     * ── Chat Service ───────────────────────────────────────
     * </pre>
     */
    public interface AsyncService {

        /**
         */
        default void sendMessage(com.dating.grpc.ChatMessageRequest request, io.grpc.stub.StreamObserver<com.dating.grpc.ChatMessageResponse> responseObserver) {
            io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSendMessageMethod(), responseObserver);
        }

        /**
         */
        default void getConversation(com.dating.grpc.ConversationRequest request, io.grpc.stub.StreamObserver<com.dating.grpc.ConversationResponse> responseObserver) {
            io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetConversationMethod(), responseObserver);
        }

        /**
         */
        default void streamMessages(com.dating.grpc.ConversationRequest request, io.grpc.stub.StreamObserver<com.dating.grpc.ChatMessage> responseObserver) {
            io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getStreamMessagesMethod(), responseObserver);
        }

        /**
         */
        default void markRead(com.dating.grpc.MarkReadRequest request, io.grpc.stub.StreamObserver<com.dating.grpc.MarkReadResponse> responseObserver) {
            io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getMarkReadMethod(), responseObserver);
        }
    }

    /**
     * Base class for the server implementation of the service ChatService.
     * <pre>
     * ── Chat Service ───────────────────────────────────────
     * </pre>
     */
    public static abstract class ChatServiceImplBase implements io.grpc.BindableService, AsyncService {

        @java.lang.Override
        public io.grpc.ServerServiceDefinition bindService() {
            return ChatServiceGrpc.bindService(this);
        }
    }

    /**
     * A stub to allow clients to do asynchronous rpc calls to service ChatService.
     * <pre>
     * ── Chat Service ───────────────────────────────────────
     * </pre>
     */
    public static class ChatServiceStub extends io.grpc.stub.AbstractAsyncStub<ChatServiceStub> {

        private ChatServiceStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @java.lang.Override
        protected ChatServiceStub build(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new ChatServiceStub(channel, callOptions);
        }

        /**
         */
        public void sendMessage(com.dating.grpc.ChatMessageRequest request, io.grpc.stub.StreamObserver<com.dating.grpc.ChatMessageResponse> responseObserver) {
            io.grpc.stub.ClientCalls.asyncUnaryCall(getChannel().newCall(getSendMessageMethod(), getCallOptions()), request, responseObserver);
        }

        /**
         */
        public void getConversation(com.dating.grpc.ConversationRequest request, io.grpc.stub.StreamObserver<com.dating.grpc.ConversationResponse> responseObserver) {
            io.grpc.stub.ClientCalls.asyncUnaryCall(getChannel().newCall(getGetConversationMethod(), getCallOptions()), request, responseObserver);
        }

        /**
         */
        public void streamMessages(com.dating.grpc.ConversationRequest request, io.grpc.stub.StreamObserver<com.dating.grpc.ChatMessage> responseObserver) {
            io.grpc.stub.ClientCalls.asyncServerStreamingCall(getChannel().newCall(getStreamMessagesMethod(), getCallOptions()), request, responseObserver);
        }

        /**
         */
        public void markRead(com.dating.grpc.MarkReadRequest request, io.grpc.stub.StreamObserver<com.dating.grpc.MarkReadResponse> responseObserver) {
            io.grpc.stub.ClientCalls.asyncUnaryCall(getChannel().newCall(getMarkReadMethod(), getCallOptions()), request, responseObserver);
        }
    }

    /**
     * A stub to allow clients to do synchronous rpc calls to service ChatService.
     * <pre>
     * ── Chat Service ───────────────────────────────────────
     * </pre>
     */
    public static class ChatServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<ChatServiceBlockingStub> {

        private ChatServiceBlockingStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @java.lang.Override
        protected ChatServiceBlockingStub build(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new ChatServiceBlockingStub(channel, callOptions);
        }

        /**
         */
        public com.dating.grpc.ChatMessageResponse sendMessage(com.dating.grpc.ChatMessageRequest request) {
            return io.grpc.stub.ClientCalls.blockingUnaryCall(getChannel(), getSendMessageMethod(), getCallOptions(), request);
        }

        /**
         */
        public com.dating.grpc.ConversationResponse getConversation(com.dating.grpc.ConversationRequest request) {
            return io.grpc.stub.ClientCalls.blockingUnaryCall(getChannel(), getGetConversationMethod(), getCallOptions(), request);
        }

        /**
         */
        public java.util.Iterator<com.dating.grpc.ChatMessage> streamMessages(com.dating.grpc.ConversationRequest request) {
            return io.grpc.stub.ClientCalls.blockingServerStreamingCall(getChannel(), getStreamMessagesMethod(), getCallOptions(), request);
        }

        /**
         */
        public com.dating.grpc.MarkReadResponse markRead(com.dating.grpc.MarkReadRequest request) {
            return io.grpc.stub.ClientCalls.blockingUnaryCall(getChannel(), getMarkReadMethod(), getCallOptions(), request);
        }
    }

    /**
     * A stub to allow clients to do ListenableFuture-style rpc calls to service ChatService.
     * <pre>
     * ── Chat Service ───────────────────────────────────────
     * </pre>
     */
    public static class ChatServiceFutureStub extends io.grpc.stub.AbstractFutureStub<ChatServiceFutureStub> {

        private ChatServiceFutureStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @java.lang.Override
        protected ChatServiceFutureStub build(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new ChatServiceFutureStub(channel, callOptions);
        }

        /**
         */
        public com.google.common.util.concurrent.ListenableFuture<com.dating.grpc.ChatMessageResponse> sendMessage(com.dating.grpc.ChatMessageRequest request) {
            return io.grpc.stub.ClientCalls.futureUnaryCall(getChannel().newCall(getSendMessageMethod(), getCallOptions()), request);
        }

        /**
         */
        public com.google.common.util.concurrent.ListenableFuture<com.dating.grpc.ConversationResponse> getConversation(com.dating.grpc.ConversationRequest request) {
            return io.grpc.stub.ClientCalls.futureUnaryCall(getChannel().newCall(getGetConversationMethod(), getCallOptions()), request);
        }

        /**
         */
        public com.google.common.util.concurrent.ListenableFuture<com.dating.grpc.MarkReadResponse> markRead(com.dating.grpc.MarkReadRequest request) {
            return io.grpc.stub.ClientCalls.futureUnaryCall(getChannel().newCall(getMarkReadMethod(), getCallOptions()), request);
        }
    }

    private static final int METHODID_SEND_MESSAGE = 0;

    private static final int METHODID_GET_CONVERSATION = 1;

    private static final int METHODID_STREAM_MESSAGES = 2;

    private static final int METHODID_MARK_READ = 3;

    private static final class MethodHandlers<Req, Resp> implements io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>, io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>, io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>, io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {

        private final AsyncService serviceImpl;

        private final int methodId;

        MethodHandlers(AsyncService serviceImpl, int methodId) {
            this.serviceImpl = serviceImpl;
            this.methodId = methodId;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("unchecked")
        public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch(methodId) {
                case METHODID_SEND_MESSAGE:
                    serviceImpl.sendMessage((com.dating.grpc.ChatMessageRequest) request, (io.grpc.stub.StreamObserver<com.dating.grpc.ChatMessageResponse>) responseObserver);
                    break;
                case METHODID_GET_CONVERSATION:
                    serviceImpl.getConversation((com.dating.grpc.ConversationRequest) request, (io.grpc.stub.StreamObserver<com.dating.grpc.ConversationResponse>) responseObserver);
                    break;
                case METHODID_STREAM_MESSAGES:
                    serviceImpl.streamMessages((com.dating.grpc.ConversationRequest) request, (io.grpc.stub.StreamObserver<com.dating.grpc.ChatMessage>) responseObserver);
                    break;
                case METHODID_MARK_READ:
                    serviceImpl.markRead((com.dating.grpc.MarkReadRequest) request, (io.grpc.stub.StreamObserver<com.dating.grpc.MarkReadResponse>) responseObserver);
                    break;
                default:
                    throw new AssertionError();
            }
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("unchecked")
        public io.grpc.stub.StreamObserver<Req> invoke(io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch(methodId) {
                default:
                    throw new AssertionError();
            }
        }
    }

    public static io.grpc.ServerServiceDefinition bindService(AsyncService service) {
        return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor()).addMethod(getSendMessageMethod(), io.grpc.stub.ServerCalls.asyncUnaryCall(new MethodHandlers<com.dating.grpc.ChatMessageRequest, com.dating.grpc.ChatMessageResponse>(service, METHODID_SEND_MESSAGE))).addMethod(getGetConversationMethod(), io.grpc.stub.ServerCalls.asyncUnaryCall(new MethodHandlers<com.dating.grpc.ConversationRequest, com.dating.grpc.ConversationResponse>(service, METHODID_GET_CONVERSATION))).addMethod(getStreamMessagesMethod(), io.grpc.stub.ServerCalls.asyncServerStreamingCall(new MethodHandlers<com.dating.grpc.ConversationRequest, com.dating.grpc.ChatMessage>(service, METHODID_STREAM_MESSAGES))).addMethod(getMarkReadMethod(), io.grpc.stub.ServerCalls.asyncUnaryCall(new MethodHandlers<com.dating.grpc.MarkReadRequest, com.dating.grpc.MarkReadResponse>(service, METHODID_MARK_READ))).build();
    }

    private static abstract class ChatServiceBaseDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {

        ChatServiceBaseDescriptorSupplier() {
        }

        @java.lang.Override
        public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
            return com.dating.grpc.DatingProto.getDescriptor();
        }

        @java.lang.Override
        public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
            return getFileDescriptor().findServiceByName("ChatService");
        }
    }

    private static final class ChatServiceFileDescriptorSupplier extends ChatServiceBaseDescriptorSupplier {

        ChatServiceFileDescriptorSupplier() {
        }
    }

    private static final class ChatServiceMethodDescriptorSupplier extends ChatServiceBaseDescriptorSupplier implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {

        private final java.lang.String methodName;

        ChatServiceMethodDescriptorSupplier(java.lang.String methodName) {
            this.methodName = methodName;
        }

        @java.lang.Override
        public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
            return getServiceDescriptor().findMethodByName(methodName);
        }
    }

    private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

    public static io.grpc.ServiceDescriptor getServiceDescriptor() {
        io.grpc.ServiceDescriptor result = serviceDescriptor;
        if (result == null) {
            synchronized (ChatServiceGrpc.class) {
                result = serviceDescriptor;
                if (result == null) {
                    serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME).setSchemaDescriptor(new ChatServiceFileDescriptorSupplier()).addMethod(getSendMessageMethod()).addMethod(getGetConversationMethod()).addMethod(getStreamMessagesMethod()).addMethod(getMarkReadMethod()).build();
                }
            }
        }
        return result;
    }
}
