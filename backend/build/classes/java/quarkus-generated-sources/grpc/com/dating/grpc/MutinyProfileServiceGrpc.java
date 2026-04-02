package com.dating.grpc;

import static com.dating.grpc.ProfileServiceGrpc.getServiceDescriptor;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;

@jakarta.annotation.Generated(value = "by Mutiny Grpc generator", comments = "Source: dating.proto")
public final class MutinyProfileServiceGrpc implements io.quarkus.grpc.MutinyGrpc {

    private MutinyProfileServiceGrpc() {
    }

    public static MutinyProfileServiceStub newMutinyStub(io.grpc.Channel channel) {
        return new MutinyProfileServiceStub(channel);
    }

    /**
     * <pre>
     *  ── Profile Service ────────────────────────────────────
     * </pre>
     */
    public static class MutinyProfileServiceStub extends io.grpc.stub.AbstractStub<MutinyProfileServiceStub> implements io.quarkus.grpc.MutinyStub {

        private ProfileServiceGrpc.ProfileServiceStub delegateStub;

        private MutinyProfileServiceStub(io.grpc.Channel channel) {
            super(channel);
            delegateStub = ProfileServiceGrpc.newStub(channel);
        }

        private MutinyProfileServiceStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
            delegateStub = ProfileServiceGrpc.newStub(channel).build(channel, callOptions);
        }

        @Override
        protected MutinyProfileServiceStub build(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new MutinyProfileServiceStub(channel, callOptions);
        }

        public io.smallrye.mutiny.Uni<com.dating.grpc.ProfileResponse> getMyProfile(com.dating.grpc.ProfileRequest request) {
            return io.quarkus.grpc.stubs.ClientCalls.oneToOne(request, delegateStub::getMyProfile);
        }

        public io.smallrye.mutiny.Uni<com.dating.grpc.ProfileResponse> getProfile(com.dating.grpc.ProfileByIdRequest request) {
            return io.quarkus.grpc.stubs.ClientCalls.oneToOne(request, delegateStub::getProfile);
        }

        public io.smallrye.mutiny.Uni<com.dating.grpc.ProfileResponse> updateProfile(com.dating.grpc.UpdateProfileRequest request) {
            return io.quarkus.grpc.stubs.ClientCalls.oneToOne(request, delegateStub::updateProfile);
        }

        public io.smallrye.mutiny.Uni<com.dating.grpc.ProfileResponse> updateLocation(com.dating.grpc.LocationUpdate request) {
            return io.quarkus.grpc.stubs.ClientCalls.oneToOne(request, delegateStub::updateLocation);
        }
    }

    /**
     * <pre>
     *  ── Profile Service ────────────────────────────────────
     * </pre>
     */
    public static abstract class ProfileServiceImplBase implements io.grpc.BindableService {

        private String compression;

        /**
         * Set whether the server will try to use a compressed response.
         *
         * @param compression the compression, e.g {@code gzip}
         */
        public ProfileServiceImplBase withCompression(String compression) {
            this.compression = compression;
            return this;
        }

        public io.smallrye.mutiny.Uni<com.dating.grpc.ProfileResponse> getMyProfile(com.dating.grpc.ProfileRequest request) {
            throw new io.grpc.StatusRuntimeException(io.grpc.Status.UNIMPLEMENTED);
        }

        public io.smallrye.mutiny.Uni<com.dating.grpc.ProfileResponse> getProfile(com.dating.grpc.ProfileByIdRequest request) {
            throw new io.grpc.StatusRuntimeException(io.grpc.Status.UNIMPLEMENTED);
        }

        public io.smallrye.mutiny.Uni<com.dating.grpc.ProfileResponse> updateProfile(com.dating.grpc.UpdateProfileRequest request) {
            throw new io.grpc.StatusRuntimeException(io.grpc.Status.UNIMPLEMENTED);
        }

        public io.smallrye.mutiny.Uni<com.dating.grpc.ProfileResponse> updateLocation(com.dating.grpc.LocationUpdate request) {
            throw new io.grpc.StatusRuntimeException(io.grpc.Status.UNIMPLEMENTED);
        }

        @java.lang.Override
        public io.grpc.ServerServiceDefinition bindService() {
            return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor()).addMethod(com.dating.grpc.ProfileServiceGrpc.getGetMyProfileMethod(), asyncUnaryCall(new MethodHandlers<com.dating.grpc.ProfileRequest, com.dating.grpc.ProfileResponse>(this, METHODID_GET_MY_PROFILE, compression))).addMethod(com.dating.grpc.ProfileServiceGrpc.getGetProfileMethod(), asyncUnaryCall(new MethodHandlers<com.dating.grpc.ProfileByIdRequest, com.dating.grpc.ProfileResponse>(this, METHODID_GET_PROFILE, compression))).addMethod(com.dating.grpc.ProfileServiceGrpc.getUpdateProfileMethod(), asyncUnaryCall(new MethodHandlers<com.dating.grpc.UpdateProfileRequest, com.dating.grpc.ProfileResponse>(this, METHODID_UPDATE_PROFILE, compression))).addMethod(com.dating.grpc.ProfileServiceGrpc.getUpdateLocationMethod(), asyncUnaryCall(new MethodHandlers<com.dating.grpc.LocationUpdate, com.dating.grpc.ProfileResponse>(this, METHODID_UPDATE_LOCATION, compression))).build();
        }
    }

    private static final int METHODID_GET_MY_PROFILE = 0;

    private static final int METHODID_GET_PROFILE = 1;

    private static final int METHODID_UPDATE_PROFILE = 2;

    private static final int METHODID_UPDATE_LOCATION = 3;

    private static final class MethodHandlers<Req, Resp> implements io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>, io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>, io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>, io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {

        private final ProfileServiceImplBase serviceImpl;

        private final int methodId;

        private final String compression;

        MethodHandlers(ProfileServiceImplBase serviceImpl, int methodId, String compression) {
            this.serviceImpl = serviceImpl;
            this.methodId = methodId;
            this.compression = compression;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("unchecked")
        public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch(methodId) {
                case METHODID_GET_MY_PROFILE:
                    io.quarkus.grpc.stubs.ServerCalls.oneToOne((com.dating.grpc.ProfileRequest) request, (io.grpc.stub.StreamObserver<com.dating.grpc.ProfileResponse>) responseObserver, compression, serviceImpl::getMyProfile);
                    break;
                case METHODID_GET_PROFILE:
                    io.quarkus.grpc.stubs.ServerCalls.oneToOne((com.dating.grpc.ProfileByIdRequest) request, (io.grpc.stub.StreamObserver<com.dating.grpc.ProfileResponse>) responseObserver, compression, serviceImpl::getProfile);
                    break;
                case METHODID_UPDATE_PROFILE:
                    io.quarkus.grpc.stubs.ServerCalls.oneToOne((com.dating.grpc.UpdateProfileRequest) request, (io.grpc.stub.StreamObserver<com.dating.grpc.ProfileResponse>) responseObserver, compression, serviceImpl::updateProfile);
                    break;
                case METHODID_UPDATE_LOCATION:
                    io.quarkus.grpc.stubs.ServerCalls.oneToOne((com.dating.grpc.LocationUpdate) request, (io.grpc.stub.StreamObserver<com.dating.grpc.ProfileResponse>) responseObserver, compression, serviceImpl::updateLocation);
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
