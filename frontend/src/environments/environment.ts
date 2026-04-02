export const environment = {
  production: false,
  // gRPC-Web endpoint — in production this goes through the Istio ingress gateway.
  // Locally, the Envoy proxy (or Quarkus gRPC transcoding) runs on port 9000.
  // The grpc-web-filter in Istio transparently converts gRPC-Web → native gRPC.
  grpcUrl: 'http://localhost:8080/grpc',
};

export const environmentProd = {
  production: true,
  // In K8s, the Istio gateway routes gRPC-Web traffic to the backend gRPC port
  grpcUrl: 'https://claspr.app',
};
