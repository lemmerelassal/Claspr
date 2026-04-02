package com.dating.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * ── Profile Service ────────────────────────────────────
 * </pre>
 */
@io.quarkus.Generated(value = "by gRPC proto compiler (version 1.59.1)", comments = "Source: dating.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ProfileServiceGrpc {

    private ProfileServiceGrpc() {
    }

    public static final java.lang.String SERVICE_NAME = "dating.ProfileService";

    // Static method descriptors that strictly reflect the proto.
    private static volatile io.grpc.MethodDescriptor<com.dating.grpc.ProfileRequest, com.dating.grpc.ProfileResponse> getGetMyProfileMethod;

    @io.grpc.stub.annotations.RpcMethod(fullMethodName = SERVICE_NAME + '/' + "GetMyProfile", requestType = com.dating.grpc.ProfileRequest.class, responseType = com.dating.grpc.ProfileResponse.class, methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
    public static io.grpc.MethodDescriptor<com.dating.grpc.ProfileRequest, com.dating.grpc.ProfileResponse> getGetMyProfileMethod() {
        io.grpc.MethodDescriptor<com.dating.grpc.ProfileRequest, com.dating.grpc.ProfileResponse> getGetMyProfileMethod;
        if ((getGetMyProfileMethod = ProfileServiceGrpc.getGetMyProfileMethod) == null) {
            synchronized (ProfileServiceGrpc.class) {
                if ((getGetMyProfileMethod = ProfileServiceGrpc.getGetMyProfileMethod) == null) {
                    ProfileServiceGrpc.getGetMyProfileMethod = getGetMyProfileMethod = io.grpc.MethodDescriptor.<com.dating.grpc.ProfileRequest, com.dating.grpc.ProfileResponse>newBuilder().setType(io.grpc.MethodDescriptor.MethodType.UNARY).setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetMyProfile")).setSampledToLocalTracing(true).setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(com.dating.grpc.ProfileRequest.getDefaultInstance())).setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(com.dating.grpc.ProfileResponse.getDefaultInstance())).setSchemaDescriptor(new ProfileServiceMethodDescriptorSupplier("GetMyProfile")).build();
                }
            }
        }
        return getGetMyProfileMethod;
    }

    private static volatile io.grpc.MethodDescriptor<com.dating.grpc.ProfileByIdRequest, com.dating.grpc.ProfileResponse> getGetProfileMethod;

    @io.grpc.stub.annotations.RpcMethod(fullMethodName = SERVICE_NAME + '/' + "GetProfile", requestType = com.dating.grpc.ProfileByIdRequest.class, responseType = com.dating.grpc.ProfileResponse.class, methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
    public static io.grpc.MethodDescriptor<com.dating.grpc.ProfileByIdRequest, com.dating.grpc.ProfileResponse> getGetProfileMethod() {
        io.grpc.MethodDescriptor<com.dating.grpc.ProfileByIdRequest, com.dating.grpc.ProfileResponse> getGetProfileMethod;
        if ((getGetProfileMethod = ProfileServiceGrpc.getGetProfileMethod) == null) {
            synchronized (ProfileServiceGrpc.class) {
                if ((getGetProfileMethod = ProfileServiceGrpc.getGetProfileMethod) == null) {
                    ProfileServiceGrpc.getGetProfileMethod = getGetProfileMethod = io.grpc.MethodDescriptor.<com.dating.grpc.ProfileByIdRequest, com.dating.grpc.ProfileResponse>newBuilder().setType(io.grpc.MethodDescriptor.MethodType.UNARY).setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetProfile")).setSampledToLocalTracing(true).setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(com.dating.grpc.ProfileByIdRequest.getDefaultInstance())).setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(com.dating.grpc.ProfileResponse.getDefaultInstance())).setSchemaDescriptor(new ProfileServiceMethodDescriptorSupplier("GetProfile")).build();
                }
            }
        }
        return getGetProfileMethod;
    }

    private static volatile io.grpc.MethodDescriptor<com.dating.grpc.UpdateProfileRequest, com.dating.grpc.ProfileResponse> getUpdateProfileMethod;

    @io.grpc.stub.annotations.RpcMethod(fullMethodName = SERVICE_NAME + '/' + "UpdateProfile", requestType = com.dating.grpc.UpdateProfileRequest.class, responseType = com.dating.grpc.ProfileResponse.class, methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
    public static io.grpc.MethodDescriptor<com.dating.grpc.UpdateProfileRequest, com.dating.grpc.ProfileResponse> getUpdateProfileMethod() {
        io.grpc.MethodDescriptor<com.dating.grpc.UpdateProfileRequest, com.dating.grpc.ProfileResponse> getUpdateProfileMethod;
        if ((getUpdateProfileMethod = ProfileServiceGrpc.getUpdateProfileMethod) == null) {
            synchronized (ProfileServiceGrpc.class) {
                if ((getUpdateProfileMethod = ProfileServiceGrpc.getUpdateProfileMethod) == null) {
                    ProfileServiceGrpc.getUpdateProfileMethod = getUpdateProfileMethod = io.grpc.MethodDescriptor.<com.dating.grpc.UpdateProfileRequest, com.dating.grpc.ProfileResponse>newBuilder().setType(io.grpc.MethodDescriptor.MethodType.UNARY).setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdateProfile")).setSampledToLocalTracing(true).setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(com.dating.grpc.UpdateProfileRequest.getDefaultInstance())).setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(com.dating.grpc.ProfileResponse.getDefaultInstance())).setSchemaDescriptor(new ProfileServiceMethodDescriptorSupplier("UpdateProfile")).build();
                }
            }
        }
        return getUpdateProfileMethod;
    }

    private static volatile io.grpc.MethodDescriptor<com.dating.grpc.LocationUpdate, com.dating.grpc.ProfileResponse> getUpdateLocationMethod;

    @io.grpc.stub.annotations.RpcMethod(fullMethodName = SERVICE_NAME + '/' + "UpdateLocation", requestType = com.dating.grpc.LocationUpdate.class, responseType = com.dating.grpc.ProfileResponse.class, methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
    public static io.grpc.MethodDescriptor<com.dating.grpc.LocationUpdate, com.dating.grpc.ProfileResponse> getUpdateLocationMethod() {
        io.grpc.MethodDescriptor<com.dating.grpc.LocationUpdate, com.dating.grpc.ProfileResponse> getUpdateLocationMethod;
        if ((getUpdateLocationMethod = ProfileServiceGrpc.getUpdateLocationMethod) == null) {
            synchronized (ProfileServiceGrpc.class) {
                if ((getUpdateLocationMethod = ProfileServiceGrpc.getUpdateLocationMethod) == null) {
                    ProfileServiceGrpc.getUpdateLocationMethod = getUpdateLocationMethod = io.grpc.MethodDescriptor.<com.dating.grpc.LocationUpdate, com.dating.grpc.ProfileResponse>newBuilder().setType(io.grpc.MethodDescriptor.MethodType.UNARY).setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdateLocation")).setSampledToLocalTracing(true).setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(com.dating.grpc.LocationUpdate.getDefaultInstance())).setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(com.dating.grpc.ProfileResponse.getDefaultInstance())).setSchemaDescriptor(new ProfileServiceMethodDescriptorSupplier("UpdateLocation")).build();
                }
            }
        }
        return getUpdateLocationMethod;
    }

    /**
     * Creates a new async stub that supports all call types for the service
     */
    public static ProfileServiceStub newStub(io.grpc.Channel channel) {
        io.grpc.stub.AbstractStub.StubFactory<ProfileServiceStub> factory = new io.grpc.stub.AbstractStub.StubFactory<ProfileServiceStub>() {

            @java.lang.Override
            public ProfileServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                return new ProfileServiceStub(channel, callOptions);
            }
        };
        return ProfileServiceStub.newStub(factory, channel);
    }

    /**
     * Creates a new blocking-style stub that supports unary and streaming output calls on the service
     */
    public static ProfileServiceBlockingStub newBlockingStub(io.grpc.Channel channel) {
        io.grpc.stub.AbstractStub.StubFactory<ProfileServiceBlockingStub> factory = new io.grpc.stub.AbstractStub.StubFactory<ProfileServiceBlockingStub>() {

            @java.lang.Override
            public ProfileServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                return new ProfileServiceBlockingStub(channel, callOptions);
            }
        };
        return ProfileServiceBlockingStub.newStub(factory, channel);
    }

    /**
     * Creates a new ListenableFuture-style stub that supports unary calls on the service
     */
    public static ProfileServiceFutureStub newFutureStub(io.grpc.Channel channel) {
        io.grpc.stub.AbstractStub.StubFactory<ProfileServiceFutureStub> factory = new io.grpc.stub.AbstractStub.StubFactory<ProfileServiceFutureStub>() {

            @java.lang.Override
            public ProfileServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                return new ProfileServiceFutureStub(channel, callOptions);
            }
        };
        return ProfileServiceFutureStub.newStub(factory, channel);
    }

    /**
     * <pre>
     * ── Profile Service ────────────────────────────────────
     * </pre>
     */
    public interface AsyncService {

        /**
         */
        default void getMyProfile(com.dating.grpc.ProfileRequest request, io.grpc.stub.StreamObserver<com.dating.grpc.ProfileResponse> responseObserver) {
            io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetMyProfileMethod(), responseObserver);
        }

        /**
         */
        default void getProfile(com.dating.grpc.ProfileByIdRequest request, io.grpc.stub.StreamObserver<com.dating.grpc.ProfileResponse> responseObserver) {
            io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetProfileMethod(), responseObserver);
        }

        /**
         */
        default void updateProfile(com.dating.grpc.UpdateProfileRequest request, io.grpc.stub.StreamObserver<com.dating.grpc.ProfileResponse> responseObserver) {
            io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateProfileMethod(), responseObserver);
        }

        /**
         */
        default void updateLocation(com.dating.grpc.LocationUpdate request, io.grpc.stub.StreamObserver<com.dating.grpc.ProfileResponse> responseObserver) {
            io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateLocationMethod(), responseObserver);
        }
    }

    /**
     * Base class for the server implementation of the service ProfileService.
     * <pre>
     * ── Profile Service ────────────────────────────────────
     * </pre>
     */
    public static abstract class ProfileServiceImplBase implements io.grpc.BindableService, AsyncService {

        @java.lang.Override
        public io.grpc.ServerServiceDefinition bindService() {
            return ProfileServiceGrpc.bindService(this);
        }
    }

    /**
     * A stub to allow clients to do asynchronous rpc calls to service ProfileService.
     * <pre>
     * ── Profile Service ────────────────────────────────────
     * </pre>
     */
    public static class ProfileServiceStub extends io.grpc.stub.AbstractAsyncStub<ProfileServiceStub> {

        private ProfileServiceStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @java.lang.Override
        protected ProfileServiceStub build(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new ProfileServiceStub(channel, callOptions);
        }

        /**
         */
        public void getMyProfile(com.dating.grpc.ProfileRequest request, io.grpc.stub.StreamObserver<com.dating.grpc.ProfileResponse> responseObserver) {
            io.grpc.stub.ClientCalls.asyncUnaryCall(getChannel().newCall(getGetMyProfileMethod(), getCallOptions()), request, responseObserver);
        }

        /**
         */
        public void getProfile(com.dating.grpc.ProfileByIdRequest request, io.grpc.stub.StreamObserver<com.dating.grpc.ProfileResponse> responseObserver) {
            io.grpc.stub.ClientCalls.asyncUnaryCall(getChannel().newCall(getGetProfileMethod(), getCallOptions()), request, responseObserver);
        }

        /**
         */
        public void updateProfile(com.dating.grpc.UpdateProfileRequest request, io.grpc.stub.StreamObserver<com.dating.grpc.ProfileResponse> responseObserver) {
            io.grpc.stub.ClientCalls.asyncUnaryCall(getChannel().newCall(getUpdateProfileMethod(), getCallOptions()), request, responseObserver);
        }

        /**
         */
        public void updateLocation(com.dating.grpc.LocationUpdate request, io.grpc.stub.StreamObserver<com.dating.grpc.ProfileResponse> responseObserver) {
            io.grpc.stub.ClientCalls.asyncUnaryCall(getChannel().newCall(getUpdateLocationMethod(), getCallOptions()), request, responseObserver);
        }
    }

    /**
     * A stub to allow clients to do synchronous rpc calls to service ProfileService.
     * <pre>
     * ── Profile Service ────────────────────────────────────
     * </pre>
     */
    public static class ProfileServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<ProfileServiceBlockingStub> {

        private ProfileServiceBlockingStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @java.lang.Override
        protected ProfileServiceBlockingStub build(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new ProfileServiceBlockingStub(channel, callOptions);
        }

        /**
         */
        public com.dating.grpc.ProfileResponse getMyProfile(com.dating.grpc.ProfileRequest request) {
            return io.grpc.stub.ClientCalls.blockingUnaryCall(getChannel(), getGetMyProfileMethod(), getCallOptions(), request);
        }

        /**
         */
        public com.dating.grpc.ProfileResponse getProfile(com.dating.grpc.ProfileByIdRequest request) {
            return io.grpc.stub.ClientCalls.blockingUnaryCall(getChannel(), getGetProfileMethod(), getCallOptions(), request);
        }

        /**
         */
        public com.dating.grpc.ProfileResponse updateProfile(com.dating.grpc.UpdateProfileRequest request) {
            return io.grpc.stub.ClientCalls.blockingUnaryCall(getChannel(), getUpdateProfileMethod(), getCallOptions(), request);
        }

        /**
         */
        public com.dating.grpc.ProfileResponse updateLocation(com.dating.grpc.LocationUpdate request) {
            return io.grpc.stub.ClientCalls.blockingUnaryCall(getChannel(), getUpdateLocationMethod(), getCallOptions(), request);
        }
    }

    /**
     * A stub to allow clients to do ListenableFuture-style rpc calls to service ProfileService.
     * <pre>
     * ── Profile Service ────────────────────────────────────
     * </pre>
     */
    public static class ProfileServiceFutureStub extends io.grpc.stub.AbstractFutureStub<ProfileServiceFutureStub> {

        private ProfileServiceFutureStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @java.lang.Override
        protected ProfileServiceFutureStub build(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new ProfileServiceFutureStub(channel, callOptions);
        }

        /**
         */
        public com.google.common.util.concurrent.ListenableFuture<com.dating.grpc.ProfileResponse> getMyProfile(com.dating.grpc.ProfileRequest request) {
            return io.grpc.stub.ClientCalls.futureUnaryCall(getChannel().newCall(getGetMyProfileMethod(), getCallOptions()), request);
        }

        /**
         */
        public com.google.common.util.concurrent.ListenableFuture<com.dating.grpc.ProfileResponse> getProfile(com.dating.grpc.ProfileByIdRequest request) {
            return io.grpc.stub.ClientCalls.futureUnaryCall(getChannel().newCall(getGetProfileMethod(), getCallOptions()), request);
        }

        /**
         */
        public com.google.common.util.concurrent.ListenableFuture<com.dating.grpc.ProfileResponse> updateProfile(com.dating.grpc.UpdateProfileRequest request) {
            return io.grpc.stub.ClientCalls.futureUnaryCall(getChannel().newCall(getUpdateProfileMethod(), getCallOptions()), request);
        }

        /**
         */
        public com.google.common.util.concurrent.ListenableFuture<com.dating.grpc.ProfileResponse> updateLocation(com.dating.grpc.LocationUpdate request) {
            return io.grpc.stub.ClientCalls.futureUnaryCall(getChannel().newCall(getUpdateLocationMethod(), getCallOptions()), request);
        }
    }

    private static final int METHODID_GET_MY_PROFILE = 0;

    private static final int METHODID_GET_PROFILE = 1;

    private static final int METHODID_UPDATE_PROFILE = 2;

    private static final int METHODID_UPDATE_LOCATION = 3;

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
                case METHODID_GET_MY_PROFILE:
                    serviceImpl.getMyProfile((com.dating.grpc.ProfileRequest) request, (io.grpc.stub.StreamObserver<com.dating.grpc.ProfileResponse>) responseObserver);
                    break;
                case METHODID_GET_PROFILE:
                    serviceImpl.getProfile((com.dating.grpc.ProfileByIdRequest) request, (io.grpc.stub.StreamObserver<com.dating.grpc.ProfileResponse>) responseObserver);
                    break;
                case METHODID_UPDATE_PROFILE:
                    serviceImpl.updateProfile((com.dating.grpc.UpdateProfileRequest) request, (io.grpc.stub.StreamObserver<com.dating.grpc.ProfileResponse>) responseObserver);
                    break;
                case METHODID_UPDATE_LOCATION:
                    serviceImpl.updateLocation((com.dating.grpc.LocationUpdate) request, (io.grpc.stub.StreamObserver<com.dating.grpc.ProfileResponse>) responseObserver);
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
        return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor()).addMethod(getGetMyProfileMethod(), io.grpc.stub.ServerCalls.asyncUnaryCall(new MethodHandlers<com.dating.grpc.ProfileRequest, com.dating.grpc.ProfileResponse>(service, METHODID_GET_MY_PROFILE))).addMethod(getGetProfileMethod(), io.grpc.stub.ServerCalls.asyncUnaryCall(new MethodHandlers<com.dating.grpc.ProfileByIdRequest, com.dating.grpc.ProfileResponse>(service, METHODID_GET_PROFILE))).addMethod(getUpdateProfileMethod(), io.grpc.stub.ServerCalls.asyncUnaryCall(new MethodHandlers<com.dating.grpc.UpdateProfileRequest, com.dating.grpc.ProfileResponse>(service, METHODID_UPDATE_PROFILE))).addMethod(getUpdateLocationMethod(), io.grpc.stub.ServerCalls.asyncUnaryCall(new MethodHandlers<com.dating.grpc.LocationUpdate, com.dating.grpc.ProfileResponse>(service, METHODID_UPDATE_LOCATION))).build();
    }

    private static abstract class ProfileServiceBaseDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {

        ProfileServiceBaseDescriptorSupplier() {
        }

        @java.lang.Override
        public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
            return com.dating.grpc.DatingProto.getDescriptor();
        }

        @java.lang.Override
        public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
            return getFileDescriptor().findServiceByName("ProfileService");
        }
    }

    private static final class ProfileServiceFileDescriptorSupplier extends ProfileServiceBaseDescriptorSupplier {

        ProfileServiceFileDescriptorSupplier() {
        }
    }

    private static final class ProfileServiceMethodDescriptorSupplier extends ProfileServiceBaseDescriptorSupplier implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {

        private final java.lang.String methodName;

        ProfileServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
            synchronized (ProfileServiceGrpc.class) {
                result = serviceDescriptor;
                if (result == null) {
                    serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME).setSchemaDescriptor(new ProfileServiceFileDescriptorSupplier()).addMethod(getGetMyProfileMethod()).addMethod(getGetProfileMethod()).addMethod(getUpdateProfileMethod()).addMethod(getUpdateLocationMethod()).build();
                }
            }
        }
        return result;
    }
}
