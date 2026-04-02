package com.dating.grpc;

import static com.dating.grpc.AuthServiceGrpc.getServiceDescriptor;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;

@jakarta.annotation.Generated(value = "by Mutiny Grpc generator", comments = "Source: dating.proto")
public final class MutinyAuthServiceGrpc implements io.quarkus.grpc.MutinyGrpc {

    private MutinyAuthServiceGrpc() {
    }

    public static MutinyAuthServiceStub newMutinyStub(io.grpc.Channel channel) {
        return new MutinyAuthServiceStub(channel);
    }

    /**
     * <pre>
     *  ── Authentication Service ─────────────────────────────
     * </pre>
     */
    public static class MutinyAuthServiceStub extends io.grpc.stub.AbstractStub<MutinyAuthServiceStub> implements io.quarkus.grpc.MutinyStub {

        private AuthServiceGrpc.AuthServiceStub delegateStub;

        private MutinyAuthServiceStub(io.grpc.Channel channel) {
            super(channel);
            delegateStub = AuthServiceGrpc.newStub(channel);
        }

        private MutinyAuthServiceStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
            delegateStub = AuthServiceGrpc.newStub(channel).build(channel, callOptions);
        }

        @Override
        protected MutinyAuthServiceStub build(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new MutinyAuthServiceStub(channel, callOptions);
        }

        public io.smallrye.mutiny.Uni<com.dating.grpc.AuthResponse> register(com.dating.grpc.RegisterRequest request) {
            return io.quarkus.grpc.stubs.ClientCalls.oneToOne(request, delegateStub::register);
        }

        public io.smallrye.mutiny.Uni<com.dating.grpc.AuthResponse> login(com.dating.grpc.LoginRequest request) {
            return io.quarkus.grpc.stubs.ClientCalls.oneToOne(request, delegateStub::login);
        }

        public io.smallrye.mutiny.Uni<com.dating.grpc.TokenValidationResponse> validateToken(com.dating.grpc.TokenRequest request) {
            return io.quarkus.grpc.stubs.ClientCalls.oneToOne(request, delegateStub::validateToken);
        }
    }

    /**
     * <pre>
     *  ── Authentication Service ─────────────────────────────
     * </pre>
     */
    public static abstract class AuthServiceImplBase implements io.grpc.BindableService {

        private String compression;

        /**
         * Set whether the server will try to use a compressed response.
         *
         * @param compression the compression, e.g {@code gzip}
         */
        public AuthServiceImplBase withCompression(String compression) {
            this.compression = compression;
            return this;
        }

        public io.smallrye.mutiny.Uni<com.dating.grpc.AuthResponse> register(com.dating.grpc.RegisterRequest request) {
            throw new io.grpc.StatusRuntimeException(io.grpc.Status.UNIMPLEMENTED);
        }

        public io.smallrye.mutiny.Uni<com.dating.grpc.AuthResponse> login(com.dating.grpc.LoginRequest request) {
            throw new io.grpc.StatusRuntimeException(io.grpc.Status.UNIMPLEMENTED);
        }

        public io.smallrye.mutiny.Uni<com.dating.grpc.TokenValidationResponse> validateToken(com.dating.grpc.TokenRequest request) {
            throw new io.grpc.StatusRuntimeException(io.grpc.Status.UNIMPLEMENTED);
        }

        @java.lang.Override
        public io.grpc.ServerServiceDefinition bindService() {
            return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor()).addMethod(com.dating.grpc.AuthServiceGrpc.getRegisterMethod(), asyncUnaryCall(new MethodHandlers<com.dating.grpc.RegisterRequest, com.dating.grpc.AuthResponse>(this, METHODID_REGISTER, compression))).addMethod(com.dating.grpc.AuthServiceGrpc.getLoginMethod(), asyncUnaryCall(new MethodHandlers<com.dating.grpc.LoginRequest, com.dating.grpc.AuthResponse>(this, METHODID_LOGIN, compression))).addMethod(com.dating.grpc.AuthServiceGrpc.getValidateTokenMethod(), asyncUnaryCall(new MethodHandlers<com.dating.grpc.TokenRequest, com.dating.grpc.TokenValidationResponse>(this, METHODID_VALIDATE_TOKEN, compression))).build();
        }
    }

    private static final int METHODID_REGISTER = 0;

    private static final int METHODID_LOGIN = 1;

    private static final int METHODID_VALIDATE_TOKEN = 2;

    private static final class MethodHandlers<Req, Resp> implements io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>, io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>, io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>, io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {

        private final AuthServiceImplBase serviceImpl;

        private final int methodId;

        private final String compression;

        MethodHandlers(AuthServiceImplBase serviceImpl, int methodId, String compression) {
            this.serviceImpl = serviceImpl;
            this.methodId = methodId;
            this.compression = compression;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("unchecked")
        public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch(methodId) {
                case METHODID_REGISTER:
                    io.quarkus.grpc.stubs.ServerCalls.oneToOne((com.dating.grpc.RegisterRequest) request, (io.grpc.stub.StreamObserver<com.dating.grpc.AuthResponse>) responseObserver, compression, serviceImpl::register);
                    break;
                case METHODID_LOGIN:
                    io.quarkus.grpc.stubs.ServerCalls.oneToOne((com.dating.grpc.LoginRequest) request, (io.grpc.stub.StreamObserver<com.dating.grpc.AuthResponse>) responseObserver, compression, serviceImpl::login);
                    break;
                case METHODID_VALIDATE_TOKEN:
                    io.quarkus.grpc.stubs.ServerCalls.oneToOne((com.dating.grpc.TokenRequest) request, (io.grpc.stub.StreamObserver<com.dating.grpc.TokenValidationResponse>) responseObserver, compression, serviceImpl::validateToken);
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
