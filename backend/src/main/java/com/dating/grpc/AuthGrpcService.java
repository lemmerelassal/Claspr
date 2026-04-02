package com.dating.grpc;

import com.dating.service.AuthService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import io.quarkus.grpc.GrpcService;
import jakarta.inject.Inject;

@GrpcService
public class AuthGrpcService extends AuthServiceGrpc.AuthServiceImplBase {

    @Inject
    AuthService authService;

    @Override
    public void register(RegisterRequest request, StreamObserver<AuthResponse> responseObserver) {
        try {
            var result = authService.register(
                    request.getEmail(),
                    request.getPassword(),
                    request.getDisplayName(),
                    request.getDateOfBirth(),
                    mapGender(request.getGender())
            );
            responseObserver.onNext(AuthResponse.newBuilder()
                    .setToken(result.token())
                    .setUserId(result.userId().toString())
                    .setDisplayName(result.displayName())
                    .build());
            responseObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            responseObserver.onError(Status.ALREADY_EXISTS
                    .withDescription(e.getMessage()).asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void login(LoginRequest request, StreamObserver<AuthResponse> responseObserver) {
        try {
            var result = authService.login(request.getEmail(), request.getPassword());
            responseObserver.onNext(AuthResponse.newBuilder()
                    .setToken(result.token())
                    .setUserId(result.userId().toString())
                    .setDisplayName(result.displayName())
                    .build());
            responseObserver.onCompleted();
        } catch (SecurityException e) {
            responseObserver.onError(Status.UNAUTHENTICATED
                    .withDescription("Invalid credentials").asRuntimeException());
        }
    }

    @Override
    public void validateToken(TokenRequest request, StreamObserver<TokenValidationResponse> responseObserver) {
        try {
            var result = authService.validateToken(request.getToken());
            responseObserver.onNext(TokenValidationResponse.newBuilder()
                    .setValid(result.valid())
                    .setUserId(result.userId() != null ? result.userId() : "")
                    .build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onNext(TokenValidationResponse.newBuilder()
                    .setValid(false).build());
            responseObserver.onCompleted();
        }
    }

    private com.dating.entity.UserProfile.Gender mapGender(Gender proto) {
        return switch (proto) {
            case MALE -> com.dating.entity.UserProfile.Gender.MALE;
            case FEMALE -> com.dating.entity.UserProfile.Gender.FEMALE;
            case NON_BINARY -> com.dating.entity.UserProfile.Gender.NON_BINARY;
            default -> com.dating.entity.UserProfile.Gender.OTHER;
        };
    }
}
