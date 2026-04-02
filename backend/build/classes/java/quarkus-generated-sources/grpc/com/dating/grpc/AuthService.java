package com.dating.grpc;

import io.quarkus.grpc.MutinyService;

@jakarta.annotation.Generated(value = "by Mutiny Grpc generator", comments = "Source: dating.proto")
public interface AuthService extends MutinyService {

    io.smallrye.mutiny.Uni<com.dating.grpc.AuthResponse> register(com.dating.grpc.RegisterRequest request);

    io.smallrye.mutiny.Uni<com.dating.grpc.AuthResponse> login(com.dating.grpc.LoginRequest request);

    io.smallrye.mutiny.Uni<com.dating.grpc.TokenValidationResponse> validateToken(com.dating.grpc.TokenRequest request);
}
