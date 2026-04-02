package com.dating.grpc;

import java.util.function.BiFunction;
import io.quarkus.grpc.MutinyClient;

@jakarta.annotation.Generated(value = "by Mutiny Grpc generator", comments = "Source: dating.proto")
public class ProfileServiceClient implements ProfileService, MutinyClient<MutinyProfileServiceGrpc.MutinyProfileServiceStub> {

    private final MutinyProfileServiceGrpc.MutinyProfileServiceStub stub;

    public ProfileServiceClient(String name, io.grpc.Channel channel, BiFunction<String, MutinyProfileServiceGrpc.MutinyProfileServiceStub, MutinyProfileServiceGrpc.MutinyProfileServiceStub> stubConfigurator) {
        this.stub = stubConfigurator.apply(name, MutinyProfileServiceGrpc.newMutinyStub(channel));
    }

    private ProfileServiceClient(MutinyProfileServiceGrpc.MutinyProfileServiceStub stub) {
        this.stub = stub;
    }

    public ProfileServiceClient newInstanceWithStub(MutinyProfileServiceGrpc.MutinyProfileServiceStub stub) {
        return new ProfileServiceClient(stub);
    }

    @Override
    public MutinyProfileServiceGrpc.MutinyProfileServiceStub getStub() {
        return stub;
    }

    @Override
    public io.smallrye.mutiny.Uni<com.dating.grpc.ProfileResponse> getMyProfile(com.dating.grpc.ProfileRequest request) {
        return stub.getMyProfile(request);
    }

    @Override
    public io.smallrye.mutiny.Uni<com.dating.grpc.ProfileResponse> getProfile(com.dating.grpc.ProfileByIdRequest request) {
        return stub.getProfile(request);
    }

    @Override
    public io.smallrye.mutiny.Uni<com.dating.grpc.ProfileResponse> updateProfile(com.dating.grpc.UpdateProfileRequest request) {
        return stub.updateProfile(request);
    }

    @Override
    public io.smallrye.mutiny.Uni<com.dating.grpc.ProfileResponse> updateLocation(com.dating.grpc.LocationUpdate request) {
        return stub.updateLocation(request);
    }
}
