#!/usr/bin/env bash
set -euo pipefail

# ── Claspr Dating App — gRPC-First Deployment ────────────
# All business logic is served via gRPC.
# Browser → gRPC-Web (Envoy/Istio transcoding) → Quarkus gRPC services
#
# Usage:
#   ./deploy.sh local    → docker-compose up
#   ./deploy.sh k8s      → deploy to Kubernetes with Istio + gRPC-Web
#   ./deploy.sh build    → build Docker images only

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
REGISTRY="${REGISTRY:-dating-app}"

CYAN='\033[0;36m'; GREEN='\033[0;32m'; RED='\033[0;31m'; NC='\033[0m'
log()  { echo -e "${CYAN}[spark]${NC} $1"; }
ok()   { echo -e "${GREEN}  [✓]${NC} $1"; }
fail() { echo -e "${RED}  [✗]${NC} $1"; exit 1; }

build_images() {
    log "Building backend image..."
    docker build -t "${REGISTRY}/backend:latest" "${SCRIPT_DIR}/backend"
    ok "Backend image built"
    log "Building frontend image..."
    docker build -t "${REGISTRY}/frontend:latest" "${SCRIPT_DIR}/frontend"
    ok "Frontend image built"
}

deploy_local() {
    log "Starting local environment with docker-compose..."
    docker-compose -f "${SCRIPT_DIR}/docker-compose.yaml" up --build -d
    ok "Local environment running"
    echo ""
    log "Transport: gRPC (port 9000) + gRPC-JSON transcoding proxy (port 8080)"
    echo ""
    echo "  Frontend:       http://localhost:4200"
    echo "  gRPC Server:    localhost:9000"
    echo "  Transcoding:    http://localhost:8080/grpc/{Service}/{Method}"
    echo "  Health:         http://localhost:8080/api/info"
    echo "  PostgreSQL:     localhost:5432"
    echo ""
    log "Demo login: sophie@demo.com / password123"
    echo ""
    log "Test gRPC with grpcurl:"
    echo "  grpcurl -plaintext localhost:9000 list"
    echo "  grpcurl -plaintext -d '{\"email\":\"sophie@demo.com\",\"password\":\"password123\"}' localhost:9000 dating.AuthService/Login"
}

deploy_k8s() {
    log "Checking prerequisites..."
    command -v kubectl >/dev/null 2>&1 || fail "kubectl not found"
    command -v istioctl >/dev/null 2>&1 || fail "istioctl not found"

    istioctl verify-install 2>/dev/null || {
        log "Installing Istio..."
        istioctl install --set profile=default -y
        ok "Istio installed"
    }

    log "Building and pushing images..."
    build_images

    log "Applying Kubernetes manifests..."

    # Namespace with Istio sidecar injection
    kubectl apply -f "${SCRIPT_DIR}/k8s/base/namespace.yaml"
    ok "Namespace created (istio-injection: enabled)"

    # Database
    kubectl apply -f "${SCRIPT_DIR}/k8s/base/postgres.yaml"
    log "Waiting for PostgreSQL..."
    kubectl -n dating-app wait --for=condition=ready pod -l app=postgres --timeout=120s
    ok "PostgreSQL ready"

    # Backend (gRPC server on :9000, health on :8080)
    kubectl apply -f "${SCRIPT_DIR}/k8s/base/backend.yaml"
    log "Waiting for backend gRPC server..."
    kubectl -n dating-app wait --for=condition=ready pod -l app=backend --timeout=180s
    ok "Backend gRPC server ready"

    # Frontend (Angular SPA)
    kubectl apply -f "${SCRIPT_DIR}/k8s/base/frontend.yaml"
    log "Waiting for frontend..."
    kubectl -n dating-app wait --for=condition=ready pod -l app=frontend --timeout=120s
    ok "Frontend ready"

    # Istio: Gateway + VirtualServices (routes gRPC-Web + gRPC + static)
    log "Configuring Istio service mesh..."
    kubectl apply -f "${SCRIPT_DIR}/k8s/istio/gateway.yaml"
    ok "Gateway + VirtualService + DestinationRules"

    # Istio: gRPC-Web EnvoyFilter (the key piece!)
    kubectl apply -f "${SCRIPT_DIR}/k8s/istio/grpc-web-filter.yaml"
    ok "gRPC-Web EnvoyFilter on sidecar + ingress gateway"

    # Istio: Security (mTLS, AuthorizationPolicies, rate limiting)
    kubectl apply -f "${SCRIPT_DIR}/k8s/istio/security.yaml"
    ok "mTLS STRICT + AuthorizationPolicies + rate limiting"

    # Istio: Observability (telemetry, tracing, network policies)
    kubectl apply -f "${SCRIPT_DIR}/k8s/istio/observability.yaml"
    ok "Telemetry + distributed tracing + NetworkPolicies"

    echo ""
    ok "Deployment complete!"
    echo ""

    INGRESS_IP=$(kubectl -n istio-system get svc istio-ingressgateway \
        -o jsonpath='{.status.loadBalancer.ingress[0].ip}' 2>/dev/null || echo "pending")

    log "Architecture:"
    echo "  Browser → gRPC-Web (HTTP/1.1+base64) → Envoy gRPC-Web filter → native gRPC → Quarkus"
    echo ""
    log "Ingress IP: ${INGRESS_IP}"
    log "Add to /etc/hosts: ${INGRESS_IP} claspr.app"
    echo ""
    log "Verify:"
    echo "  istioctl analyze -n dating-app"
    echo "  istioctl dashboard kiali"
    echo "  kubectl -n dating-app logs -l app=backend -f"
    echo ""
    log "Pod status:"
    kubectl -n dating-app get pods -o wide
}

case "${1:-local}" in
    local) deploy_local ;;
    k8s)   deploy_k8s ;;
    build) build_images ;;
    *)     echo "Usage: $0 {local|k8s|build}"; exit 1 ;;
esac
