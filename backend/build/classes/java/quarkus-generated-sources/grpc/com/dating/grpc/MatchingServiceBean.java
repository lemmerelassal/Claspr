package com.dating.grpc;

import io.grpc.BindableService;
import io.quarkus.grpc.GrpcService;
import io.quarkus.grpc.MutinyBean;

@jakarta.annotation.Generated(value = "by Mutiny Grpc generator", comments = "Source: dating.proto")
public class MatchingServiceBean extends MutinyMatchingServiceGrpc.MatchingServiceImplBase implements BindableService, MutinyBean {

    private final MatchingService delegate;

    MatchingServiceBean(@GrpcService MatchingService delegate) {
        this.delegate = delegate;
    }

    @Override
    public io.smallrye.mutiny.Uni<com.dating.grpc.MatchResponse> getPotentialMatches(com.dating.grpc.MatchRequest request) {
        try {
            return delegate.getPotentialMatches(request);
        } catch (UnsupportedOperationException e) {
            throw new io.grpc.StatusRuntimeException(io.grpc.Status.UNIMPLEMENTED);
        }
    }

    @Override
    public io.smallrye.mutiny.Uni<com.dating.grpc.SwipeResponse> recordSwipe(com.dating.grpc.SwipeRequest request) {
        try {
            return delegate.recordSwipe(request);
        } catch (UnsupportedOperationException e) {
            throw new io.grpc.StatusRuntimeException(io.grpc.Status.UNIMPLEMENTED);
        }
    }

    @Override
    public io.smallrye.mutiny.Uni<com.dating.grpc.GetMatchesResponse> getMatches(com.dating.grpc.GetMatchesRequest request) {
        try {
            return delegate.getMatches(request);
        } catch (UnsupportedOperationException e) {
            throw new io.grpc.StatusRuntimeException(io.grpc.Status.UNIMPLEMENTED);
        }
    }

    @Override
    public io.smallrye.mutiny.Uni<com.dating.grpc.UnmatchResponse> unmatchUser(com.dating.grpc.UnmatchRequest request) {
        try {
            return delegate.unmatchUser(request);
        } catch (UnsupportedOperationException e) {
            throw new io.grpc.StatusRuntimeException(io.grpc.Status.UNIMPLEMENTED);
        }
    }
}
