package com.dating.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * ── Authentication Service ─────────────────────────────
 * </pre>
 */
@io.quarkus.Generated(value = "by gRPC proto compiler (version 1.59.1)", comments = "Source: dating.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class AuthServiceGrpc {

    private AuthServiceGrpc() {
    }

    public static final java.lang.String SERVICE_NAME = "dating.AuthService";

    // Static method descriptors that strictly reflect the proto.
    private static volatile io.grpc.MethodDescriptor<com.dating.grpc.RegisterRequest, com.dating.grpc.AuthResponse> getRegisterMethod;

    @io.grpc.stub.annotations.RpcMethod(fullMethodName = SERVICE_NAME + '/' + "Register", requestType = com.dating.grpc.RegisterRequest.class, responseType = com.dating.grpc.AuthResponse.class, methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
    public static io.grpc.MethodDescriptor<com.dating.grpc.RegisterRequest, com.dating.grpc.AuthResponse> getRegisterMethod() {
        io.grpc.MethodDescriptor<com.dating.grpc.RegisterRequest, com.dating.grpc.AuthResponse> getRegisterMethod;
        if ((getRegisterMethod = AuthServiceGrpc.getRegisterMethod) == null) {
            synchronized (AuthServiceGrpc.class) {
                if ((getRegisterMethod = AuthServiceGrpc.getRegisterMethod) == null) {
                    AuthServiceGrpc.getRegisterMethod = getRegisterMethod = io.grpc.MethodDescriptor.<com.dating.grpc.RegisterRequest, com.dating.grpc.AuthResponse>newBuilder().setType(io.grpc.MethodDescriptor.MethodType.UNARY).setFullMethodName(generateFullMethodName(SERVICE_NAME, "Register")).setSampledToLocalTracing(true).setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(com.dating.grpc.RegisterRequest.getDefaultInstance())).setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(com.dating.grpc.AuthResponse.getDefaultInstance())).setSchemaDescriptor(new AuthServiceMethodDescriptorSupplier("Register")).build();
                }
            }
        }
        return getRegisterMethod;
    }

    private static volatile io.grpc.MethodDescriptor<com.dating.grpc.LoginRequest, com.dating.grpc.AuthResponse> getLoginMethod;

    @io.grpc.stub.annotations.RpcMethod(fullMethodName = SERVICE_NAME + '/' + "Login", requestType = com.dating.grpc.LoginRequest.class, responseType = com.dating.grpc.AuthResponse.class, methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
    public static io.grpc.MethodDescriptor<com.dating.grpc.LoginRequest, com.dating.grpc.AuthResponse> getLoginMethod() {
        io.grpc.MethodDescriptor<com.dating.grpc.LoginRequest, com.dating.grpc.AuthResponse> getLoginMethod;
        if ((getLoginMethod = AuthServiceGrpc.getLoginMethod) == null) {
            synchronized (AuthServiceGrpc.class) {
                if ((getLoginMethod = AuthServiceGrpc.getLoginMethod) == null) {
                    AuthServiceGrpc.getLoginMethod = getLoginMethod = io.grpc.MethodDescriptor.<com.dating.grpc.LoginRequest, com.dating.grpc.AuthResponse>newBuilder().setType(io.grpc.MethodDescriptor.MethodType.UNARY).setFullMethodName(generateFullMethodName(SERVICE_NAME, "Login")).setSampledToLocalTracing(true).setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(com.dating.grpc.LoginRequest.getDefaultInstance())).setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(com.dating.grpc.AuthResponse.getDefaultInstance())).setSchemaDescriptor(new AuthServiceMethodDescriptorSupplier("Login")).build();
                }
            }
        }
        return getLoginMethod;
    }

    private static volatile io.grpc.MethodDescriptor<com.dating.grpc.TokenRequest, com.dating.grpc.TokenValidationResponse> getValidateTokenMethod;

    @io.grpc.stub.annotations.RpcMethod(fullMethodName = SERVICE_NAME + '/' + "ValidateToken", requestType = com.dating.grpc.TokenRequest.class, responseType = com.dating.grpc.TokenValidationResponse.class, methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
    public static io.grpc.MethodDescriptor<com.dating.grpc.TokenRequest, com.dating.grpc.TokenValidationResponse> getValidateTokenMethod() {
        io.grpc.MethodDescriptor<com.dating.grpc.TokenRequest, com.dating.grpc.TokenValidationResponse> getValidateTokenMethod;
        if ((getValidateTokenMethod = AuthServiceGrpc.getValidateTokenMethod) == null) {
            synchronized (AuthServiceGrpc.class) {
                if ((getValidateTokenMethod = AuthServiceGrpc.getValidateTokenMethod) == null) {
                    AuthServiceGrpc.getValidateTokenMethod = getValidateTokenMethod = io.grpc.MethodDescriptor.<com.dating.grpc.TokenRequest, com.dating.grpc.TokenValidationResponse>newBuilder().setType(io.grpc.MethodDescriptor.MethodType.UNARY).setFullMethodName(generateFullMethodName(SERVICE_NAME, "ValidateToken")).setSampledToLocalTracing(true).setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(com.dating.grpc.TokenRequest.getDefaultInstance())).setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(com.dating.grpc.TokenValidationResponse.getDefaultInstance())).setSchemaDescriptor(new AuthServiceMethodDescriptorSupplier("ValidateToken")).build();
                }
            }
        }
        return getValidateTokenMethod;
    }

    /**
     * Creates a new async stub that supports all call types for the service
     */
    public static AuthServiceStub newStub(io.grpc.Channel channel) {
        io.grpc.stub.AbstractStub.StubFactory<AuthServiceStub> factory = new io.grpc.stub.AbstractStub.StubFactory<AuthServiceStub>() {

            @java.lang.Override
            public AuthServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                return new AuthServiceStub(channel, callOptions);
            }
        };
        return AuthServiceStub.newStub(factory, channel);
    }

    /**
     * Creates a new blocking-style stub that supports unary and streaming output calls on the service
     */
    public static AuthServiceBlockingStub newBlockingStub(io.grpc.Channel channel) {
        io.grpc.stub.AbstractStub.StubFactory<AuthServiceBlockingStub> factory = new io.grpc.stub.AbstractStub.StubFactory<AuthServiceBlockingStub>() {

            @java.lang.Override
            public AuthServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                return new AuthServiceBlockingStub(channel, callOptions);
            }
        };
        return AuthServiceBlockingStub.newStub(factory, channel);
    }

    /**
     * Creates a new ListenableFuture-style stub that supports unary calls on the service
     */
    public static AuthServiceFutureStub newFutureStub(io.grpc.Channel channel) {
        io.grpc.stub.AbstractStub.StubFactory<AuthServiceFutureStub> factory = new io.grpc.stub.AbstractStub.StubFactory<AuthServiceFutureStub>() {

            @java.lang.Override
            public AuthServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                return new AuthServiceFutureStub(channel, callOptions);
            }
        };
        return AuthServiceFutureStub.newStub(factory, channel);
    }

    /**
     * <pre>
     * ── Authentication Service ─────────────────────────────
     * </pre>
     */
    public interface AsyncService {

        /**
         */
        default void register(com.dating.grpc.RegisterRequest request, io.grpc.stub.StreamObserver<com.dating.grpc.AuthResponse> responseObserver) {
            io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRegisterMethod(), responseObserver);
        }

        /**
         */
        default void login(com.dating.grpc.LoginRequest request, io.grpc.stub.StreamObserver<com.dating.grpc.AuthResponse> responseObserver) {
            io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getLoginMethod(), responseObserver);
        }

        /**
         */
        default void validateToken(com.dating.grpc.TokenRequest request, io.grpc.stub.StreamObserver<com.dating.grpc.TokenValidationResponse> responseObserver) {
            io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getValidateTokenMethod(), responseObserver);
        }
    }

    /**
     * Base class for the server implementation of the service AuthService.
     * <pre>
     * ── Authentication Service ─────────────────────────────
     * </pre>
     */
    public static abstract class AuthServiceImplBase implements io.grpc.BindableService, AsyncService {

        @java.lang.Override
        public io.grpc.ServerServiceDefinition bindService() {
            return AuthServiceGrpc.bindService(this);
        }
    }

    /**
     * A stub to allow clients to do asynchronous rpc calls to service AuthService.
     * <pre>
     * ── Authentication Service ─────────────────────────────
     * </pre>
     */
    public static class AuthServiceStub extends io.grpc.stub.AbstractAsyncStub<AuthServiceStub> {

        private AuthServiceStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @java.lang.Override
        protected AuthServiceStub build(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new AuthServiceStub(channel, callOptions);
        }

        /**
         */
        public void register(com.dating.grpc.RegisterRequest request, io.grpc.stub.StreamObserver<com.dating.grpc.AuthResponse> responseObserver) {
            io.grpc.stub.ClientCalls.asyncUnaryCall(getChannel().newCall(getRegisterMethod(), getCallOptions()), request, responseObserver);
        }

        /**
         */
        public void login(com.dating.grpc.LoginRequest request, io.grpc.stub.StreamObserver<com.dating.grpc.AuthResponse> responseObserver) {
            io.grpc.stub.ClientCalls.asyncUnaryCall(getChannel().newCall(getLoginMethod(), getCallOptions()), request, responseObserver);
        }

        /**
         */
        public void validateToken(com.dating.grpc.TokenRequest request, io.grpc.stub.StreamObserver<com.dating.grpc.TokenValidationResponse> responseObserver) {
            io.grpc.stub.ClientCalls.asyncUnaryCall(getChannel().newCall(getValidateTokenMethod(), getCallOptions()), request, responseObserver);
        }
    }

    /**
     * A stub to allow clients to do synchronous rpc calls to service AuthService.
     * <pre>
     * ── Authentication Service ─────────────────────────────
     * </pre>
     */
    public static class AuthServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<AuthServiceBlockingStub> {

        private AuthServiceBlockingStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @java.lang.Override
        protected AuthServiceBlockingStub build(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new AuthServiceBlockingStub(channel, callOptions);
        }

        /**
         */
        public com.dating.grpc.AuthResponse register(com.dating.grpc.RegisterRequest request) {
            return io.grpc.stub.ClientCalls.blockingUnaryCall(getChannel(), getRegisterMethod(), getCallOptions(), request);
        }

        /**
         */
        public com.dating.grpc.AuthResponse login(com.dating.grpc.LoginRequest request) {
            return io.grpc.stub.ClientCalls.blockingUnaryCall(getChannel(), getLoginMethod(), getCallOptions(), request);
        }

        /**
         */
        public com.dating.grpc.TokenValidationResponse validateToken(com.dating.grpc.TokenRequest request) {
            return io.grpc.stub.ClientCalls.blockingUnaryCall(getChannel(), getValidateTokenMethod(), getCallOptions(), request);
        }
    }

    /**
     * A stub to allow clients to do ListenableFuture-style rpc calls to service AuthService.
     * <pre>
     * ── Authentication Service ─────────────────────────────
     * </pre>
     */
    public static class AuthServiceFutureStub extends io.grpc.stub.AbstractFutureStub<AuthServiceFutureStub> {

        private AuthServiceFutureStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @java.lang.Override
        protected AuthServiceFutureStub build(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new AuthServiceFutureStub(channel, callOptions);
        }

        /**
         */
        public com.google.common.util.concurrent.ListenableFuture<com.dating.grpc.AuthResponse> register(com.dating.grpc.RegisterRequest request) {
            return io.grpc.stub.ClientCalls.futureUnaryCall(getChannel().newCall(getRegisterMethod(), getCallOptions()), request);
        }

        /**
         */
        public com.google.common.util.concurrent.ListenableFuture<com.dating.grpc.AuthResponse> login(com.dating.grpc.LoginRequest request) {
            return io.grpc.stub.ClientCalls.futureUnaryCall(getChannel().newCall(getLoginMethod(), getCallOptions()), request);
        }

        /**
         */
        public com.google.common.util.concurrent.ListenableFuture<com.dating.grpc.TokenValidationResponse> validateToken(com.dating.grpc.TokenRequest request) {
            return io.grpc.stub.ClientCalls.futureUnaryCall(getChannel().newCall(getValidateTokenMethod(), getCallOptions()), request);
        }
    }

    private static final int METHODID_REGISTER = 0;

    private static final int METHODID_LOGIN = 1;

    private static final int METHODID_VALIDATE_TOKEN = 2;

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
                case METHODID_REGISTER:
                    serviceImpl.register((com.dating.grpc.RegisterRequest) request, (io.grpc.stub.StreamObserver<com.dating.grpc.AuthResponse>) responseObserver);
                    break;
                case METHODID_LOGIN:
                    serviceImpl.login((com.dating.grpc.LoginRequest) request, (io.grpc.stub.StreamObserver<com.dating.grpc.AuthResponse>) responseObserver);
                    break;
                case METHODID_VALIDATE_TOKEN:
                    serviceImpl.validateToken((com.dating.grpc.TokenRequest) request, (io.grpc.stub.StreamObserver<com.dating.grpc.TokenValidationResponse>) responseObserver);
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
        return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor()).addMethod(getRegisterMethod(), io.grpc.stub.ServerCalls.asyncUnaryCall(new MethodHandlers<com.dating.grpc.RegisterRequest, com.dating.grpc.AuthResponse>(service, METHODID_REGISTER))).addMethod(getLoginMethod(), io.grpc.stub.ServerCalls.asyncUnaryCall(new MethodHandlers<com.dating.grpc.LoginRequest, com.dating.grpc.AuthResponse>(service, METHODID_LOGIN))).addMethod(getValidateTokenMethod(), io.grpc.stub.ServerCalls.asyncUnaryCall(new MethodHandlers<com.dating.grpc.TokenRequest, com.dating.grpc.TokenValidationResponse>(service, METHODID_VALIDATE_TOKEN))).build();
    }

    private static abstract class AuthServiceBaseDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {

        AuthServiceBaseDescriptorSupplier() {
        }

        @java.lang.Override
        public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
            return com.dating.grpc.DatingProto.getDescriptor();
        }

        @java.lang.Override
        public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
            return getFileDescriptor().findServiceByName("AuthService");
        }
    }

    private static final class AuthServiceFileDescriptorSupplier extends AuthServiceBaseDescriptorSupplier {

        AuthServiceFileDescriptorSupplier() {
        }
    }

    private static final class AuthServiceMethodDescriptorSupplier extends AuthServiceBaseDescriptorSupplier implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {

        private final java.lang.String methodName;

        AuthServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
            synchronized (AuthServiceGrpc.class) {
                result = serviceDescriptor;
                if (result == null) {
                    serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME).setSchemaDescriptor(new AuthServiceFileDescriptorSupplier()).addMethod(getRegisterMethod()).addMethod(getLoginMethod()).addMethod(getValidateTokenMethod()).build();
                }
            }
        }
        return result;
    }
}
