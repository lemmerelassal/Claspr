package com.dating.grpc;

import static com.dating.grpc.MatchingServiceGrpc.getServiceDescriptor;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;

@jakarta.annotation.Generated(value = "by Mutiny Grpc generator", comments = "Source: dating.proto")
public final class MutinyMatchingServiceGrpc implements io.quarkus.grpc.MutinyGrpc {

    private MutinyMatchingServiceGrpc() {
    }

    public static MutinyMatchingServiceStub newMutinyStub(io.grpc.Channel channel) {
        return new MutinyMatchingServiceStub(channel);
    }

    /**
     * <pre>
     *  ── Matching / Discovery Service ───────────────────────
     * </pre>
     */
    public static class MutinyMatchingServiceStub extends io.grpc.stub.AbstractStub<MutinyMatchingServiceStub> implements io.quarkus.grpc.MutinyStub {

        private MatchingServiceGrpc.MatchingServiceStub delegateStub;

        private MutinyMatchingServiceStub(io.grpc.Channel channel) {
            super(channel);
            delegateStub = MatchingServiceGrpc.newStub(channel);
        }

        private MutinyMatchingServiceStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
            delegateStub = MatchingServiceGrpc.newStub(channel).build(channel, callOptions);
        }

        @Override
        protected MutinyMatchingServiceStub build(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new MutinyMatchingServiceStub(channel, callOptions);
        }

        public io.smallrye.mutiny.Uni<com.dating.grpc.MatchResponse> getPotentialMatches(com.dating.grpc.MatchRequest request) {
            return io.quarkus.grpc.stubs.ClientCalls.oneToOne(request, delegateStub::getPotentialMatches);
        }

        public io.smallrye.mutiny.Uni<com.dating.grpc.SwipeResponse> recordSwipe(com.dating.grpc.SwipeRequest request) {
            return io.quarkus.grpc.stubs.ClientCalls.oneToOne(request, delegateStub::recordSwipe);
        }

        public io.smallrye.mutiny.Uni<com.dating.grpc.GetMatchesResponse> getMatches(com.dating.grpc.GetMatchesRequest request) {
            return io.quarkus.grpc.stubs.ClientCalls.oneToOne(request, delegateStub::getMatches);
        }

        public io.smallrye.mutiny.Uni<com.dating.grpc.UnmatchResponse> unmatchUser(com.dating.grpc.UnmatchRequest request) {
            return io.quarkus.grpc.stubs.ClientCalls.oneToOne(request, delegateStub::unmatchUser);
        }
    }

    /**
     * <pre>
     *  ── Matching / Discovery Service ───────────────────────
     * </pre>
     */
    public static abstract class MatchingServiceImplBase implements io.grpc.BindableService {

        private String compression;

        /**
         * Set whether the server will try to use a compressed response.
         *
         * @param compression the compression, e.g {@code gzip}
         */
        public MatchingServiceImplBase withCompression(String compression) {
            this.compression = compression;
            return this;
        }

        public io.smallrye.mutiny.Uni<com.dating.grpc.MatchResponse> getPotentialMatches(com.dating.grpc.MatchRequest request) {
            throw new io.grpc.StatusRuntimeException(io.grpc.Status.UNIMPLEMENTED);
        }

        public io.smallrye.mutiny.Uni<com.dating.grpc.SwipeResponse> recordSwipe(com.dating.grpc.SwipeRequest request) {
            throw new io.grpc.StatusRuntimeException(io.grpc.Status.UNIMPLEMENTED);
        }

        public io.smallrye.mutiny.Uni<com.dating.grpc.GetMatchesResponse> getMatches(com.dating.grpc.GetMatchesRequest request) {
            throw new io.grpc.StatusRuntimeException(io.grpc.Status.UNIMPLEMENTED);
        }

        public io.smallrye.mutiny.Uni<com.dating.grpc.UnmatchResponse> unmatchUser(com.dating.grpc.UnmatchRequest request) {
            throw new io.grpc.StatusRuntimeException(io.grpc.Status.UNIMPLEMENTED);
        }

        @java.lang.Override
        public io.grpc.ServerServiceDefinition bindService() {
            return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor()).addMethod(com.dating.grpc.MatchingServiceGrpc.getGetPotentialMatchesMethod(), asyncUnaryCall(new MethodHandlers<com.dating.grpc.MatchRequest, com.dating.grpc.MatchResponse>(this, METHODID_GET_POTENTIAL_MATCHES, compression))).addMethod(com.dating.grpc.MatchingServiceGrpc.getRecordSwipeMethod(), asyncUnaryCall(new MethodHandlers<com.dating.grpc.SwipeRequest, com.dating.grpc.SwipeResponse>(this, METHODID_RECORD_SWIPE, compression))).addMethod(com.dating.grpc.MatchingServiceGrpc.getGetMatchesMethod(), asyncUnaryCall(new MethodHandlers<com.dating.grpc.GetMatchesRequest, com.dating.grpc.GetMatchesResponse>(this, METHODID_GET_MATCHES, compression))).addMethod(com.dating.grpc.MatchingServiceGrpc.getUnmatchUserMethod(), asyncUnaryCall(new MethodHandlers<com.dating.grpc.UnmatchRequest, com.dating.grpc.UnmatchResponse>(this, METHODID_UNMATCH_USER, compression))).build();
        }
    }

    private static final int METHODID_GET_POTENTIAL_MATCHES = 0;

    private static final int METHODID_RECORD_SWIPE = 1;

    private static final int METHODID_GET_MATCHES = 2;

    private static final int METHODID_UNMATCH_USER = 3;

    private static final class MethodHandlers<Req, Resp> implements io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>, io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>, io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>, io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {

        private final MatchingServiceImplBase serviceImpl;

        private final int methodId;

        private final String compression;

        MethodHandlers(MatchingServiceImplBase serviceImpl, int methodId, String compression) {
            this.serviceImpl = serviceImpl;
            this.methodId = methodId;
            this.compression = compression;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("unchecked")
        public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch(methodId) {
                case METHODID_GET_POTENTIAL_MATCHES:
                    io.quarkus.grpc.stubs.ServerCalls.oneToOne((com.dating.grpc.MatchRequest) request, (io.grpc.stub.StreamObserver<com.dating.grpc.MatchResponse>) responseObserver, compression, serviceImpl::getPotentialMatches);
                    break;
                case METHODID_RECORD_SWIPE:
                    io.quarkus.grpc.stubs.ServerCalls.oneToOne((com.dating.grpc.SwipeRequest) request, (io.grpc.stub.StreamObserver<com.dating.grpc.SwipeResponse>) responseObserver, compression, serviceImpl::recordSwipe);
                    break;
                case METHODID_GET_MATCHES:
                    io.quarkus.grpc.stubs.ServerCalls.oneToOne((com.dating.grpc.GetMatchesRequest) request, (io.grpc.stub.StreamObserver<com.dating.grpc.GetMatchesResponse>) responseObserver, compression, serviceImpl::getMatches);
                    break;
                case METHODID_UNMATCH_USER:
                    io.quarkus.grpc.stubs.ServerCalls.oneToOne((com.dating.grpc.UnmatchRequest) request, (io.grpc.stub.StreamObserver<com.dating.grpc.UnmatchResponse>) responseObserver, compression, serviceImpl::unmatchUser);
                    break;
                default:
                    throw new java.lang.AssertionError();
            }
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("unchecked")
        public io.grpc.stub.StreamObserver<Req> invoke(io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch(methodId) {
                default:
                    throw new java.lang.AssertionError();
            }
        }
    }
}
