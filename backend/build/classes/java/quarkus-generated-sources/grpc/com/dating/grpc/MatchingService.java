package com.dating.grpc;

import io.quarkus.grpc.MutinyService;

@jakarta.annotation.Generated(value = "by Mutiny Grpc generator", comments = "Source: dating.proto")
public interface MatchingService extends MutinyService {

    io.smallrye.mutiny.Uni<com.dating.grpc.MatchResponse> getPotentialMatches(com.dating.grpc.MatchRequest request);

    io.smallrye.mutiny.Uni<com.dating.grpc.SwipeResponse> recordSwipe(com.dating.grpc.SwipeRequest request);

    io.smallrye.mutiny.Uni<com.dating.grpc.GetMatchesResponse> getMatches(com.dating.grpc.GetMatchesRequest request);

    io.smallrye.mutiny.Uni<com.dating.grpc.UnmatchResponse> unmatchUser(com.dating.grpc.UnmatchRequest request);
}
