package com.dating.grpc;

import java.util.function.BiFunction;
import io.quarkus.grpc.MutinyClient;

@jakarta.annotation.Generated(value = "by Mutiny Grpc generator", comments = "Source: dating.proto")
public class AuthServiceClient implements AuthService, MutinyClient<MutinyAuthServiceGrpc.MutinyAuthServiceStub> {

    private final MutinyAuthServiceGrpc.MutinyAuthServiceStub stub;

    public AuthServiceClient(String name, io.grpc.Channel channel, BiFunction<String, MutinyAuthServiceGrpc.MutinyAuthServiceStub, MutinyAuthServiceGrpc.MutinyAuthServiceStub> stubConfigurator) {
        this.stub = stubConfigurator.apply(name, MutinyAuthServiceGrpc.newMutinyStub(channel));
    }

    private AuthServiceClient(MutinyAuthServiceGrpc.MutinyAuthServiceStub stub) {
        this.stub = stub;
    }

    public AuthServiceClient newInstanceWithStub(MutinyAuthServiceGrpc.MutinyAuthServiceStub stub) {
        return new AuthServiceClient(stub);
    }

    @Override
    public MutinyAuthServiceGrpc.MutinyAuthServiceStub getStub() {
        return stub;
    }

    @Override
    public io.smallrye.mutiny.Uni<com.dating.grpc.AuthResponse> register(com.dating.grpc.RegisterRequest request) {
        return stub.register(request);
    }

    @Override
    public io.smallrye.mutiny.Uni<com.dating.grpc.AuthResponse> login(com.dating.grpc.LoginRequest request) {
        return stub.login(request);
    }

    @Override
    public io.smallrye.mutiny.Uni<com.dating.grpc.TokenValidationResponse> validateToken(com.dating.grpc.TokenRequest request) {
        return stub.validateToken(request);
    }
}
