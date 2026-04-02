package com.dating.grpc;

import io.grpc.BindableService;
import io.quarkus.grpc.GrpcService;
import io.quarkus.grpc.MutinyBean;

@jakarta.annotation.Generated(value = "by Mutiny Grpc generator", comments = "Source: dating.proto")
public class AuthServiceBean extends MutinyAuthServiceGrpc.AuthServiceImplBase implements BindableService, MutinyBean {

    private final AuthService delegate;

    AuthServiceBean(@GrpcService AuthService delegate) {
        this.delegate = delegate;
    }

    @Override
    public io.smallrye.mutiny.Uni<com.dating.grpc.AuthResponse> register(com.dating.grpc.RegisterRequest request) {
        try {
            return delegate.register(request);
        } catch (UnsupportedOperationException e) {
            throw new io.grpc.StatusRuntimeException(io.grpc.Status.UNIMPLEMENTED);
        }
    }

    @Override
    public io.smallrye.mutiny.Uni<com.dating.grpc.AuthResponse> login(com.dating.grpc.LoginRequest request) {
        try {
            return delegate.login(request);
        } catch (UnsupportedOperationException e) {
            throw new io.grpc.StatusRuntimeException(io.grpc.Status.UNIMPLEMENTED);
        }
    }

    @Override
    public io.smallrye.mutiny.Uni<com.dating.grpc.TokenValidationResponse> validateToken(com.dating.grpc.TokenRequest request) {
        try {
            return delegate.validateToken(request);
        } catch (UnsupportedOperationException e) {
            throw new io.grpc.StatusRuntimeException(io.grpc.Status.UNIMPLEMENTED);
        }
    }
}
