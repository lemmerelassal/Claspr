package com.dating.grpc;

import io.grpc.BindableService;
import io.quarkus.grpc.GrpcService;
import io.quarkus.grpc.MutinyBean;

@jakarta.annotation.Generated(value = "by Mutiny Grpc generator", comments = "Source: dating.proto")
public class ProfileServiceBean extends MutinyProfileServiceGrpc.ProfileServiceImplBase implements BindableService, MutinyBean {

    private final ProfileService delegate;

    ProfileServiceBean(@GrpcService ProfileService delegate) {
        this.delegate = delegate;
    }

    @Override
    public io.smallrye.mutiny.Uni<com.dating.grpc.ProfileResponse> getMyProfile(com.dating.grpc.ProfileRequest request) {
        try {
            return delegate.getMyProfile(request);
        } catch (UnsupportedOperationException e) {
            throw new io.grpc.StatusRuntimeException(io.grpc.Status.UNIMPLEMENTED);
        }
    }

    @Override
    public io.smallrye.mutiny.Uni<com.dating.grpc.ProfileResponse> getProfile(com.dating.grpc.ProfileByIdRequest request) {
        try {
            return delegate.getProfile(request);
        } catch (UnsupportedOperationException e) {
            throw new io.grpc.StatusRuntimeException(io.grpc.Status.UNIMPLEMENTED);
        }
    }

    @Override
    public io.smallrye.mutiny.Uni<com.dating.grpc.ProfileResponse> updateProfile(com.dating.grpc.UpdateProfileRequest request) {
        try {
            return delegate.updateProfile(request);
        } catch (UnsupportedOperationException e) {
            throw new io.grpc.StatusRuntimeException(io.grpc.Status.UNIMPLEMENTED);
        }
    }

    @Override
    public io.smallrye.mutiny.Uni<com.dating.grpc.ProfileResponse> updateLocation(com.dating.grpc.LocationUpdate request) {
        try {
            return delegate.updateLocation(request);
        } catch (UnsupportedOperationException e) {
            throw new io.grpc.StatusRuntimeException(io.grpc.Status.UNIMPLEMENTED);
        }
    }
}
