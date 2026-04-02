package com.dating.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * ── Matching / Discovery Service ───────────────────────
 * </pre>
 */
@io.quarkus.Generated(value = "by gRPC proto compiler (version 1.59.1)", comments = "Source: dating.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class MatchingServiceGrpc {

    private MatchingServiceGrpc() {
    }

    public static final java.lang.String SERVICE_NAME = "dating.MatchingService";

    // Static method descriptors that strictly reflect the proto.
    private static volatile io.grpc.MethodDescriptor<com.dating.grpc.MatchRequest, com.dating.grpc.MatchResponse> getGetPotentialMatchesMethod;

    @io.grpc.stub.annotations.RpcMethod(fullMethodName = SERVICE_NAME + '/' + "GetPotentialMatches", requestType = com.dating.grpc.MatchRequest.class, responseType = com.dating.grpc.MatchResponse.class, methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
    public static io.grpc.MethodDescriptor<com.dating.grpc.MatchRequest, com.dating.grpc.MatchResponse> getGetPotentialMatchesMethod() {
        io.grpc.MethodDescriptor<com.dating.grpc.MatchRequest, com.dating.grpc.MatchResponse> getGetPotentialMatchesMethod;
        if ((getGetPotentialMatchesMethod = MatchingServiceGrpc.getGetPotentialMatchesMethod) == null) {
            synchronized (MatchingServiceGrpc.class) {
                if ((getGetPotentialMatchesMethod = MatchingServiceGrpc.getGetPotentialMatchesMethod) == null) {
                    MatchingServiceGrpc.getGetPotentialMatchesMethod = getGetPotentialMatchesMethod = io.grpc.MethodDescriptor.<com.dating.grpc.MatchRequest, com.dating.grpc.MatchResponse>newBuilder().setType(io.grpc.MethodDescriptor.MethodType.UNARY).setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetPotentialMatches")).setSampledToLocalTracing(true).setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(com.dating.grpc.MatchRequest.getDefaultInstance())).setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(com.dating.grpc.MatchResponse.getDefaultInstance())).setSchemaDescriptor(new MatchingServiceMethodDescriptorSupplier("GetPotentialMatches")).build();
                }
            }
        }
        return getGetPotentialMatchesMethod;
    }

    private static volatile io.grpc.MethodDescriptor<com.dating.grpc.SwipeRequest, com.dating.grpc.SwipeResponse> getRecordSwipeMethod;

    @io.grpc.stub.annotations.RpcMethod(fullMethodName = SERVICE_NAME + '/' + "RecordSwipe", requestType = com.dating.grpc.SwipeRequest.class, responseType = com.dating.grpc.SwipeResponse.class, methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
    public static io.grpc.MethodDescriptor<com.dating.grpc.SwipeRequest, com.dating.grpc.SwipeResponse> getRecordSwipeMethod() {
        io.grpc.MethodDescriptor<com.dating.grpc.SwipeRequest, com.dating.grpc.SwipeResponse> getRecordSwipeMethod;
        if ((getRecordSwipeMethod = MatchingServiceGrpc.getRecordSwipeMethod) == null) {
            synchronized (MatchingServiceGrpc.class) {
                if ((getRecordSwipeMethod = MatchingServiceGrpc.getRecordSwipeMethod) == null) {
                    MatchingServiceGrpc.getRecordSwipeMethod = getRecordSwipeMethod = io.grpc.MethodDescriptor.<com.dating.grpc.SwipeRequest, com.dating.grpc.SwipeResponse>newBuilder().setType(io.grpc.MethodDescriptor.MethodType.UNARY).setFullMethodName(generateFullMethodName(SERVICE_NAME, "RecordSwipe")).setSampledToLocalTracing(true).setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(com.dating.grpc.SwipeRequest.getDefaultInstance())).setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(com.dating.grpc.SwipeResponse.getDefaultInstance())).setSchemaDescriptor(new MatchingServiceMethodDescriptorSupplier("RecordSwipe")).build();
                }
            }
        }
        return getRecordSwipeMethod;
    }

    private static volatile io.grpc.MethodDescriptor<com.dating.grpc.GetMatchesRequest, com.dating.grpc.GetMatchesResponse> getGetMatchesMethod;

    @io.grpc.stub.annotations.RpcMethod(fullMethodName = SERVICE_NAME + '/' + "GetMatches", requestType = com.dating.grpc.GetMatchesRequest.class, responseType = com.dating.grpc.GetMatchesResponse.class, methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
    public static io.grpc.MethodDescriptor<com.dating.grpc.GetMatchesRequest, com.dating.grpc.GetMatchesResponse> getGetMatchesMethod() {
        io.grpc.MethodDescriptor<com.dating.grpc.GetMatchesRequest, com.dating.grpc.GetMatchesResponse> getGetMatchesMethod;
        if ((getGetMatchesMethod = MatchingServiceGrpc.getGetMatchesMethod) == null) {
            synchronized (MatchingServiceGrpc.class) {
                if ((getGetMatchesMethod = MatchingServiceGrpc.getGetMatchesMethod) == null) {
                    MatchingServiceGrpc.getGetMatchesMethod = getGetMatchesMethod = io.grpc.MethodDescriptor.<com.dating.grpc.GetMatchesRequest, com.dating.grpc.GetMatchesResponse>newBuilder().setType(io.grpc.MethodDescriptor.MethodType.UNARY).setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetMatches")).setSampledToLocalTracing(true).setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(com.dating.grpc.GetMatchesRequest.getDefaultInstance())).setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(com.dating.grpc.GetMatchesResponse.getDefaultInstance())).setSchemaDescriptor(new MatchingServiceMethodDescriptorSupplier("GetMatches")).build();
                }
            }
        }
        return getGetMatchesMethod;
    }

    private static volatile io.grpc.MethodDescriptor<com.dating.grpc.UnmatchRequest, com.dating.grpc.UnmatchResponse> getUnmatchUserMethod;

    @io.grpc.stub.annotations.RpcMethod(fullMethodName = SERVICE_NAME + '/' + "UnmatchUser", requestType = com.dating.grpc.UnmatchRequest.class, responseType = com.dating.grpc.UnmatchResponse.class, methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
    public static io.grpc.MethodDescriptor<com.dating.grpc.UnmatchRequest, com.dating.grpc.UnmatchResponse> getUnmatchUserMethod() {
        io.grpc.MethodDescriptor<com.dating.grpc.UnmatchRequest, com.dating.grpc.UnmatchResponse> getUnmatchUserMethod;
        if ((getUnmatchUserMethod = MatchingServiceGrpc.getUnmatchUserMethod) == null) {
            synchronized (MatchingServiceGrpc.class) {
                if ((getUnmatchUserMethod = MatchingServiceGrpc.getUnmatchUserMethod) == null) {
                    MatchingServiceGrpc.getUnmatchUserMethod = getUnmatchUserMethod = io.grpc.MethodDescriptor.<com.dating.grpc.UnmatchRequest, com.dating.grpc.UnmatchResponse>newBuilder().setType(io.grpc.MethodDescriptor.MethodType.UNARY).setFullMethodName(generateFullMethodName(SERVICE_NAME, "UnmatchUser")).setSampledToLocalTracing(true).setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(com.dating.grpc.UnmatchRequest.getDefaultInstance())).setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(com.dating.grpc.UnmatchResponse.getDefaultInstance())).setSchemaDescriptor(new MatchingServiceMethodDescriptorSupplier("UnmatchUser")).build();
                }
            }
        }
        return getUnmatchUserMethod;
    }

    /**
     * Creates a new async stub that supports all call types for the service
     */
    public static MatchingServiceStub newStub(io.grpc.Channel channel) {
        io.grpc.stub.AbstractStub.StubFactory<MatchingServiceStub> factory = new io.grpc.stub.AbstractStub.StubFactory<MatchingServiceStub>() {

            @java.lang.Override
            public MatchingServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                return new MatchingServiceStub(channel, callOptions);
            }
        };
        return MatchingServiceStub.newStub(factory, channel);
    }

    /**
     * Creates a new blocking-style stub that supports unary and streaming output calls on the service
     */
    public static MatchingServiceBlockingStub newBlockingStub(io.grpc.Channel channel) {
        io.grpc.stub.AbstractStub.StubFactory<MatchingServiceBlockingStub> factory = new io.grpc.stub.AbstractStub.StubFactory<MatchingServiceBlockingStub>() {

            @java.lang.Override
            public MatchingServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                return new MatchingServiceBlockingStub(channel, callOptions);
            }
        };
        return MatchingServiceBlockingStub.newStub(factory, channel);
    }

    /**
     * Creates a new ListenableFuture-style stub that supports unary calls on the service
     */
    public static MatchingServiceFutureStub newFutureStub(io.grpc.Channel channel) {
        io.grpc.stub.AbstractStub.StubFactory<MatchingServiceFutureStub> factory = new io.grpc.stub.AbstractStub.StubFactory<MatchingServiceFutureStub>() {

            @java.lang.Override
            public MatchingServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                return new MatchingServiceFutureStub(channel, callOptions);
            }
        };
        return MatchingServiceFutureStub.newStub(factory, channel);
    }

    /**
     * <pre>
     * ── Matching / Discovery Service ───────────────────────
     * </pre>
     */
    public interface AsyncService {

        /**
         */
        default void getPotentialMatches(com.dating.grpc.MatchRequest request, io.grpc.stub.StreamObserver<com.dating.grpc.MatchResponse> responseObserver) {
            io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetPotentialMatchesMethod(), responseObserver);
        }

        /**
         */
        default void recordSwipe(com.dating.grpc.SwipeRequest request, io.grpc.stub.StreamObserver<com.dating.grpc.SwipeResponse> responseObserver) {
            io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRecordSwipeMethod(), responseObserver);
        }

        /**
         */
        default void getMatches(com.dating.grpc.GetMatchesRequest request, io.grpc.stub.StreamObserver<com.dating.grpc.GetMatchesResponse> responseObserver) {
            io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetMatchesMethod(), responseObserver);
        }

        /**
         */
        default void unmatchUser(com.dating.grpc.UnmatchRequest request, io.grpc.stub.StreamObserver<com.dating.grpc.UnmatchResponse> responseObserver) {
            io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUnmatchUserMethod(), responseObserver);
        }
    }

    /**
     * Base class for the server implementation of the service MatchingService.
     * <pre>
     * ── Matching / Discovery Service ───────────────────────
     * </pre>
     */
    public static abstract class MatchingServiceImplBase implements io.grpc.BindableService, AsyncService {

        @java.lang.Override
        public io.grpc.ServerServiceDefinition bindService() {
            return MatchingServiceGrpc.bindService(this);
        }
    }

    /**
     * A stub to allow clients to do asynchronous rpc calls to service MatchingService.
     * <pre>
     * ── Matching / Discovery Service ───────────────────────
     * </pre>
     */
    public static class MatchingServiceStub extends io.grpc.stub.AbstractAsyncStub<MatchingServiceStub> {

        private MatchingServiceStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @java.lang.Override
        protected MatchingServiceStub build(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new MatchingServiceStub(channel, callOptions);
        }

        /**
         */
        public void getPotentialMatches(com.dating.grpc.MatchRequest request, io.grpc.stub.StreamObserver<com.dating.grpc.MatchResponse> responseObserver) {
            io.grpc.stub.ClientCalls.asyncUnaryCall(getChannel().newCall(getGetPotentialMatchesMethod(), getCallOptions()), request, responseObserver);
        }

        /**
         */
        public void recordSwipe(com.dating.grpc.SwipeRequest request, io.grpc.stub.StreamObserver<com.dating.grpc.SwipeResponse> responseObserver) {
            io.grpc.stub.ClientCalls.asyncUnaryCall(getChannel().newCall(getRecordSwipeMethod(), getCallOptions()), request, responseObserver);
        }

        /**
         */
        public void getMatches(com.dating.grpc.GetMatchesRequest request, io.grpc.stub.StreamObserver<com.dating.grpc.GetMatchesResponse> responseObserver) {
            io.grpc.stub.ClientCalls.asyncUnaryCall(getChannel().newCall(getGetMatchesMethod(), getCallOptions()), request, responseObserver);
        }

        /**
         */
        public void unmatchUser(com.dating.grpc.UnmatchRequest request, io.grpc.stub.StreamObserver<com.dating.grpc.UnmatchResponse> responseObserver) {
            io.grpc.stub.ClientCalls.asyncUnaryCall(getChannel().newCall(getUnmatchUserMethod(), getCallOptions()), request, responseObserver);
        }
    }

    /**
     * A stub to allow clients to do synchronous rpc calls to service MatchingService.
     * <pre>
     * ── Matching / Discovery Service ───────────────────────
     * </pre>
     */
    public static class MatchingServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<MatchingServiceBlockingStub> {

        private MatchingServiceBlockingStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @java.lang.Override
        protected MatchingServiceBlockingStub build(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new MatchingServiceBlockingStub(channel, callOptions);
        }

        /**
         */
        public com.dating.grpc.MatchResponse getPotentialMatches(com.dating.grpc.MatchRequest request) {
            return io.grpc.stub.ClientCalls.blockingUnaryCall(getChannel(), getGetPotentialMatchesMethod(), getCallOptions(), request);
        }

        /**
         */
        public com.dating.grpc.SwipeResponse recordSwipe(com.dating.grpc.SwipeRequest request) {
            return io.grpc.stub.ClientCalls.blockingUnaryCall(getChannel(), getRecordSwipeMethod(), getCallOptions(), request);
        }

        /**
         */
        public com.dating.grpc.GetMatchesResponse getMatches(com.dating.grpc.GetMatchesRequest request) {
            return io.grpc.stub.ClientCalls.blockingUnaryCall(getChannel(), getGetMatchesMethod(), getCallOptions(), request);
        }

        /**
         */
        public com.dating.grpc.UnmatchResponse unmatchUser(com.dating.grpc.UnmatchRequest request) {
            return io.grpc.stub.ClientCalls.blockingUnaryCall(getChannel(), getUnmatchUserMethod(), getCallOptions(), request);
        }
    }

    /**
     * A stub to allow clients to do ListenableFuture-style rpc calls to service MatchingService.
     * <pre>
     * ── Matching / Discovery Service ───────────────────────
     * </pre>
     */
    public static class MatchingServiceFutureStub extends io.grpc.stub.AbstractFutureStub<MatchingServiceFutureStub> {

        private MatchingServiceFutureStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @java.lang.Override
        protected MatchingServiceFutureStub build(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new MatchingServiceFutureStub(channel, callOptions);
        }

        /**
         */
        public com.google.common.util.concurrent.ListenableFuture<com.dating.grpc.MatchResponse> getPotentialMatches(com.dating.grpc.MatchRequest request) {
            return io.grpc.stub.ClientCalls.futureUnaryCall(getChannel().newCall(getGetPotentialMatchesMethod(), getCallOptions()), request);
        }

        /**
         */
        public com.google.common.util.concurrent.ListenableFuture<com.dating.grpc.SwipeResponse> recordSwipe(com.dating.grpc.SwipeRequest request) {
            return io.grpc.stub.ClientCalls.futureUnaryCall(getChannel().newCall(getRecordSwipeMethod(), getCallOptions()), request);
        }

        /**
         */
        public com.google.common.util.concurrent.ListenableFuture<com.dating.grpc.GetMatchesResponse> getMatches(com.dating.grpc.GetMatchesRequest request) {
            return io.grpc.stub.ClientCalls.futureUnaryCall(getChannel().newCall(getGetMatchesMethod(), getCallOptions()), request);
        }

        /**
         */
        public com.google.common.util.concurrent.ListenableFuture<com.dating.grpc.UnmatchResponse> unmatchUser(com.dating.grpc.UnmatchRequest request) {
            return io.grpc.stub.ClientCalls.futureUnaryCall(getChannel().newCall(getUnmatchUserMethod(), getCallOptions()), request);
        }
    }

    private static final int METHODID_GET_POTENTIAL_MATCHES = 0;

    private static final int METHODID_RECORD_SWIPE = 1;

    private static final int METHODID_GET_MATCHES = 2;

    private static final int METHODID_UNMATCH_USER = 3;

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
                case METHODID_GET_POTENTIAL_MATCHES:
                    serviceImpl.getPotentialMatches((com.dating.grpc.MatchRequest) request, (io.grpc.stub.StreamObserver<com.dating.grpc.MatchResponse>) responseObserver);
                    break;
                case METHODID_RECORD_SWIPE:
                    serviceImpl.recordSwipe((com.dating.grpc.SwipeRequest) request, (io.grpc.stub.StreamObserver<com.dating.grpc.SwipeResponse>) responseObserver);
                    break;
                case METHODID_GET_MATCHES:
                    serviceImpl.getMatches((com.dating.grpc.GetMatchesRequest) request, (io.grpc.stub.StreamObserver<com.dating.grpc.GetMatchesResponse>) responseObserver);
                    break;
                case METHODID_UNMATCH_USER:
                    serviceImpl.unmatchUser((com.dating.grpc.UnmatchRequest) request, (io.grpc.stub.StreamObserver<com.dating.grpc.UnmatchResponse>) responseObserver);
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
        return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor()).addMethod(getGetPotentialMatchesMethod(), io.grpc.stub.ServerCalls.asyncUnaryCall(new MethodHandlers<com.dating.grpc.MatchRequest, com.dating.grpc.MatchResponse>(service, METHODID_GET_POTENTIAL_MATCHES))).addMethod(getRecordSwipeMethod(), io.grpc.stub.ServerCalls.asyncUnaryCall(new MethodHandlers<com.dating.grpc.SwipeRequest, com.dating.grpc.SwipeResponse>(service, METHODID_RECORD_SWIPE))).addMethod(getGetMatchesMethod(), io.grpc.stub.ServerCalls.asyncUnaryCall(new MethodHandlers<com.dating.grpc.GetMatchesRequest, com.dating.grpc.GetMatchesResponse>(service, METHODID_GET_MATCHES))).addMethod(getUnmatchUserMethod(), io.grpc.stub.ServerCalls.asyncUnaryCall(new MethodHandlers<com.dating.grpc.UnmatchRequest, com.dating.grpc.UnmatchResponse>(service, METHODID_UNMATCH_USER))).build();
    }

    private static abstract class MatchingServiceBaseDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {

        MatchingServiceBaseDescriptorSupplier() {
        }

        @java.lang.Override
        public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
            return com.dating.grpc.DatingProto.getDescriptor();
        }

        @java.lang.Override
        public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
            return getFileDescriptor().findServiceByName("MatchingService");
        }
    }

    private static final class MatchingServiceFileDescriptorSupplier extends MatchingServiceBaseDescriptorSupplier {

        MatchingServiceFileDescriptorSupplier() {
        }
    }

    private static final class MatchingServiceMethodDescriptorSupplier extends MatchingServiceBaseDescriptorSupplier implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {

        private final java.lang.String methodName;

        MatchingServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
            synchronized (MatchingServiceGrpc.class) {
                result = serviceDescriptor;
                if (result == null) {
                    serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME).setSchemaDescriptor(new MatchingServiceFileDescriptorSupplier()).addMethod(getGetPotentialMatchesMethod()).addMethod(getRecordSwipeMethod()).addMethod(getGetMatchesMethod()).addMethod(getUnmatchUserMethod()).build();
                }
            }
        }
        return result;
    }
}
