package com.dating.grpc;

import java.util.function.BiFunction;
import io.quarkus.grpc.MutinyClient;

@jakarta.annotation.Generated(value = "by Mutiny Grpc generator", comments = "Source: dating.proto")
public class MatchingServiceClient implements MatchingService, MutinyClient<MutinyMatchingServiceGrpc.MutinyMatchingServiceStub> {

    private final MutinyMatchingServiceGrpc.MutinyMatchingServiceStub stub;

    public MatchingServiceClient(String name, io.grpc.Channel channel, BiFunction<String, MutinyMatchingServiceGrpc.MutinyMatchingServiceStub, MutinyMatchingServiceGrpc.MutinyMatchingServiceStub> stubConfigurator) {
        this.stub = stubConfigurator.apply(name, MutinyMatchingServiceGrpc.newMutinyStub(channel));
    }

    private MatchingServiceClient(MutinyMatchingServiceGrpc.MutinyMatchingServiceStub stub) {
        this.stub = stub;
    }

    public MatchingServiceClient newInstanceWithStub(MutinyMatchingServiceGrpc.MutinyMatchingServiceStub stub) {
        return new MatchingServiceClient(stub);
    }

    @Override
    public MutinyMatchingServiceGrpc.MutinyMatchingServiceStub getStub() {
        return stub;
    }

    @Override
    public io.smallrye.mutiny.Uni<com.dating.grpc.MatchResponse> getPotentialMatches(com.dating.grpc.MatchRequest request) {
        return stub.getPotentialMatches(request);
    }

    @Override
    public io.smallrye.mutiny.Uni<com.dating.grpc.SwipeResponse> recordSwipe(com.dating.grpc.SwipeRequest request) {
        return stub.recordSwipe(request);
    }

    @Override
    public io.smallrye.mutiny.Uni<com.dating.grpc.GetMatchesResponse> getMatches(com.dating.grpc.GetMatchesRequest request) {
        return stub.getMatches(request);
    }

    @Override
    public io.smallrye.mutiny.Uni<com.dating.grpc.UnmatchResponse> unmatchUser(com.dating.grpc.UnmatchRequest request) {
        return stub.unmatchUser(request);
    }
}
