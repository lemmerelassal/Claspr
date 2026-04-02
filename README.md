# 🔥 Claspr — Tinder-Style Dating App (gRPC-First)

A full-stack, cloud-native dating application where **all business logic is served via gRPC**. The browser communicates using gRPC-Web, translated to native gRPC by Envoy/Istio.

## Architecture: gRPC All The Way Down

```
┌─────────────────────────────────────────────────────────────────┐
│                     Browser (Angular 17)                        │
│               GrpcClientService → fetch() POST                  │
│           Content-Type: application/grpc-web-text               │
└───────────────────────────┬─────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│                  Istio Ingress Gateway                          │
│            EnvoyFilter: grpc_web + cors                         │
│      Routes: gRPC-Web → backend:9000, static → frontend:80     │
└───────────────────────┬─────────────────┬───────────────────────┘
                        │                 │
              gRPC-Web → gRPC         Static assets
                        │                 │
                ┌───────▼───────┐  ┌──────▼────────┐
                │   Backend     │  │   Frontend    │
                │  (Quarkus)    │  │ (Angular/Nginx)│
                │               │  └───────────────┘
                │  gRPC :9000   │
                │  ┌──────────┐ │
                │  │AuthSvc   │ │
                │  │ProfileSvc│ │
                │  │MatchSvc  │ │
                │  │ChatSvc   │ │
                │  └──────────┘ │
                │               │
                │  Transcoding  │
                │  Proxy :8080  │ ← (dev-only JSON bridge)
                └───────┬───────┘
                        │
                ┌───────▼───────┐
                │  PostgreSQL   │
                │    :5432      │
                └───────────────┘
```

## Tech Stack

| Layer | Technology |
|-------|-----------|
| **Backend** | Quarkus 3.8, Java 21, Hibernate ORM Panache |
| **Transport** | **gRPC** (protobuf) — primary for all APIs |
| **Browser Transport** | **gRPC-Web** via Envoy filter (Istio) |
| **Database** | PostgreSQL 16 |
| **Build** | Gradle 8 |
| **Frontend** | Angular 17 (standalone components) |
| **Orchestration** | Kubernetes (HPA, probes) |
| **Service Mesh** | Istio (mTLS, gRPC-Web filter, traffic mgmt) |

## gRPC Services (defined in `dating.proto`)

### AuthService
| RPC | Description |
|-----|-------------|
| `Register` | Create account, returns JWT |
| `Login` | Authenticate, returns JWT |
| `ValidateToken` | Verify JWT validity |

### ProfileService
| RPC | Description |
|-----|-------------|
| `GetMyProfile` | Get authenticated user's profile |
| `GetProfile` | View another user's profile |
| `UpdateProfile` | Update bio, interests, preferences |
| `UpdateLocation` | Update GPS coordinates |

### MatchingService
| RPC | Description |
|-----|-------------|
| `GetPotentialMatches` | Discover profiles (Haversine distance) |
| `RecordSwipe` | Swipe LEFT/RIGHT/SUPER_LIKE |
| `GetMatches` | List mutual matches |
| `UnmatchUser` | Remove a match |

### ChatService
| RPC | Description |
|-----|-------------|
| `SendMessage` | Send TEXT/IMAGE/GIF message |
| `GetConversation` | Paginated message history |
| `StreamMessages` | Server-streaming real-time messages |
| `MarkRead` | Mark messages as read |

## Quick Start

### Local Development

```bash
./deploy.sh local
```

```
Frontend:       http://localhost:4200
gRPC Server:    localhost:9000
Transcoding:    http://localhost:8080/grpc/{Service}/{Method}
Demo login:     sophie@demo.com / password123
```

### Test gRPC directly with grpcurl

```bash
# List all services
grpcurl -plaintext localhost:9000 list

# Login via gRPC
grpcurl -plaintext \
  -d '{"email":"sophie@demo.com","password":"password123"}' \
  localhost:9000 dating.AuthService/Login

# Get potential matches
grpcurl -plaintext \
  -d '{"user_id":"<UUID>","limit":5}' \
  localhost:9000 dating.MatchingService/GetPotentialMatches
```

### Kubernetes + Istio

```bash
./deploy.sh k8s
```

## How gRPC-Web Works in This App

1. **Angular** calls `GrpcClientService.discover()` → `fetch()` POST to `/grpc/MatchingService/GetPotentialMatches`
2. **Dev mode**: The `GrpcTranscodingProxy` (JAX-RS) receives JSON, calls the gRPC service layer directly
3. **Production (K8s)**: The Angular app sends `Content-Type: application/grpc-web` → Istio's EnvoyFilter (`grpc_web`) converts to native gRPC → Quarkus gRPC server handles it natively
4. **Istio sidecar** enforces mTLS between all pods regardless of transport

### Generating Protobuf Stubs (Production)

For binary protobuf in the browser (smaller payloads, type safety):

```bash
# Install protoc + grpc-web plugin
npm install -g grpc-web

# Generate TypeScript stubs from dating.proto
protoc --js_out=import_style=commonjs:./src/app/generated \
       --grpc-web_out=import_style=typescript,mode=grpcwebtext:./src/app/generated \
       backend/src/main/proto/dating.proto
```

## Project Structure

```
tinder-clone/
├── backend/
│   ├── src/main/
│   │   ├── java/com/dating/
│   │   │   ├── config/
│   │   │   │   ├── GrpcTranscodingProxy.java  ← JSON↔gRPC bridge (dev)
│   │   │   │   ├── HealthResource.java         ← minimal REST: /api/info
│   │   │   │   └── DataSeeder.java
│   │   │   ├── grpc/
│   │   │   │   ├── AuthGrpcService.java        ← gRPC implementation
│   │   │   │   ├── ProfileGrpcService.java     ← gRPC implementation
│   │   │   │   ├── MatchingGrpcService.java    ← gRPC implementation
│   │   │   │   └── ChatGrpcService.java        ← gRPC implementation
│   │   │   ├── service/                        ← business logic
│   │   │   ├── entity/                         ← JPA entities
│   │   │   └── dto/
│   │   ├── proto/
│   │   │   └── dating.proto                    ← ALL API definitions
│   │   └── resources/
│   ├── build.gradle
│   └── Dockerfile
├── frontend/
│   ├── src/app/
│   │   ├── services/
│   │   │   └── grpc-client.service.ts          ← gRPC-Web client
│   │   ├── app.component.ts                    ← all UI components
│   │   └── app.routes.ts
│   └── Dockerfile
├── k8s/
│   ├── base/                                   ← K8s deployments
│   └── istio/
│       ├── gateway.yaml                        ← routes gRPC-Web traffic
│       ├── grpc-web-filter.yaml                ← EnvoyFilter for gRPC-Web!
│       ├── security.yaml                       ← mTLS + AuthZ + rate limit
│       └── observability.yaml                  ← telemetry + tracing
├── docker-compose.yaml
├── deploy.sh
└── README.md
```

## Istio gRPC-Web Configuration

The key Istio manifest is `k8s/istio/grpc-web-filter.yaml`:

- **Backend sidecar**: Injects `envoy.filters.http.grpc_web` + CORS filter on port 9000
- **Ingress gateway**: Same filter for external traffic
- **Result**: Browsers send `application/grpc-web-text` → Envoy converts → native gRPC to Quarkus

Additional Istio features:
- **mTLS STRICT** — all pod-to-pod traffic encrypted
- **Authorization policies** — deny-all default with explicit allow rules
- **Rate limiting** — 100 req/min per backend pod
- **Circuit breaking** — outlier detection ejects unhealthy pods
- **10% distributed tracing** — Zipkin sampling
