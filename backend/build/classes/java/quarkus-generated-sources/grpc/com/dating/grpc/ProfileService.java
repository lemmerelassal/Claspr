package com.dating.grpc;

import io.quarkus.grpc.MutinyService;

@jakarta.annotation.Generated(value = "by Mutiny Grpc generator", comments = "Source: dating.proto")
public interface ProfileService extends MutinyService {

    io.smallrye.mutiny.Uni<com.dating.grpc.ProfileResponse> getMyProfile(com.dating.grpc.ProfileRequest request);

    io.smallrye.mutiny.Uni<com.dating.grpc.ProfileResponse> getProfile(com.dating.grpc.ProfileByIdRequest request);

    io.smallrye.mutiny.Uni<com.dating.grpc.ProfileResponse> updateProfile(com.dating.grpc.UpdateProfileRequest request);

    io.smallrye.mutiny.Uni<com.dating.grpc.ProfileResponse> updateLocation(com.dating.grpc.LocationUpdate request);
}
