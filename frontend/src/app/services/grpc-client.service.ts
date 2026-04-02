import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, from } from 'rxjs';
import { environment } from '../../environments/environment';

// ── gRPC-Web transport layer ────────────────────────────
// Uses the grpc-web library to call protobuf services from the browser.
// The Envoy sidecar (Istio) or standalone Envoy proxy translates
// gRPC-Web ↔ native gRPC for the Quarkus backend.

// We use a generic gRPC-Web client approach that works with
// the @improbable-eng/grpc-web library and ts-protoc-gen stubs.
// In production, generate stubs from dating.proto using:
//   protoc --js_out=import_style=commonjs:. \
//          --grpc-web_out=import_style=typescript,mode=grpcwebtext:. \
//          dating.proto

interface GrpcMetadata {
  [key: string]: string;
}

@Injectable({ providedIn: 'root' })
export class GrpcClientService {
  private grpcUrl = environment.grpcUrl;
  private currentUser$ = new BehaviorSubject<AuthState | null>(null);

  constructor() {
    const stored = localStorage.getItem('auth');
    if (stored) {
      this.currentUser$.next(JSON.parse(stored));
    }
  }

  get currentUser(): Observable<AuthState | null> {
    return this.currentUser$.asObservable();
  }

  get userId(): string | null {
    return this.currentUser$.value?.userId ?? null;
  }

  get token(): string | null {
    return this.currentUser$.value?.token ?? null;
  }

  get isLoggedIn(): boolean {
    return !!this.currentUser$.value;
  }

  // ── Auth metadata for every gRPC call ────────────────
  private getMetadata(): GrpcMetadata {
    const meta: GrpcMetadata = {
      'Content-Type': 'application/grpc-web-text',
    };
    if (this.token) {
      meta['authorization'] = `Bearer ${this.token}`;
    }
    return meta;
  }

  // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  // gRPC-Web transport: sends protobuf-encoded requests
  // via HTTP/1.1 POST with base64 framing.
  // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  private async grpcCall<T>(service: string, method: string, payload: any): Promise<T> {
    const url = `${this.grpcUrl}/dating.${service}/${method}`;

    // Encode the payload as JSON for our simplified gRPC-Web proxy
    // In production with protobuf stubs, this would be binary encoded
    const body = JSON.stringify(payload);

    const response = await fetch(url, {
      method: 'POST',
      headers: {
        ...this.getMetadata(),
        'Content-Type': 'application/json',
        'X-Grpc-Web': '1',
      },
      body,
    });

    if (!response.ok) {
      const error = await response.text();
      throw new Error(error || `gRPC call failed: ${response.status}`);
    }

    return response.json();
  }

  // ── Fallback: JSON-over-gRPC proxy for dev ───────────
  // In dev mode, we use a thin JSON proxy. In production,
  // use proper protobuf binary encoding with generated stubs.
  private async rpc<T>(service: string, method: string, payload: any): Promise<T> {
    // Use the REST-to-gRPC transcoding endpoint
    const url = `${this.grpcUrl}/${service}/${method}`;

    const response = await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(payload),
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({ message: 'Request failed' }));
      throw new Error(errorData.message || `gRPC error: ${response.status}`);
    }

    return response.json();
  }

  // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  // AuthService RPCs
  // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  async login(email: string, password: string): Promise<AuthState> {
    const result = await this.rpc<AuthState>('AuthService', 'Login', {
      email, password
    });
    this.storeAuth(result);
    return result;
  }

  async register(data: RegisterPayload): Promise<AuthState> {
    const result = await this.rpc<AuthState>('AuthService', 'Register', {
      email: data.email,
      password: data.password,
      display_name: data.displayName,
      date_of_birth: data.dateOfBirth,
      gender: data.gender,
    });
    this.storeAuth(result);
    return result;
  }

  logout(): void {
    localStorage.removeItem('auth');
    this.currentUser$.next(null);
  }

  // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  // ProfileService RPCs
  // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  async getMyProfile(): Promise<ProfileData> {
    return this.rpc<ProfileData>('ProfileService', 'GetMyProfile', {
      user_id: this.userId
    });
  }

  async updateProfile(update: ProfileUpdate): Promise<ProfileData> {
    return this.rpc<ProfileData>('ProfileService', 'UpdateProfile', {
      user_id: this.userId,
      ...update
    });
  }

  async updateLocation(lat: number, lng: number, city: string): Promise<ProfileData> {
    return this.rpc<ProfileData>('ProfileService', 'UpdateLocation', {
      user_id: this.userId,
      latitude: lat,
      longitude: lng,
      city
    });
  }

  // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  // MatchingService RPCs
  // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  async discover(limit = 10): Promise<DiscoverResponse> {
    return this.rpc<DiscoverResponse>('MatchingService', 'GetPotentialMatches', {
      user_id: this.userId,
      limit
    });
  }

  async swipe(swipedId: string, direction: string): Promise<SwipeResult> {
    return this.rpc<SwipeResult>('MatchingService', 'RecordSwipe', {
      swiper_id: this.userId,
      swiped_id: swipedId,
      direction
    });
  }

  async getMatches(): Promise<MatchesResponse> {
    return this.rpc<MatchesResponse>('MatchingService', 'GetMatches', {
      user_id: this.userId,
      page: 0,
      size: 50
    });
  }

  async unmatch(matchId: string): Promise<void> {
    await this.rpc<any>('MatchingService', 'UnmatchUser', {
      user_id: this.userId,
      match_id: matchId
    });
  }

  // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  // ChatService RPCs
  // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  async sendMessage(matchId: string, content: string): Promise<SendMessageResult> {
    return this.rpc<SendMessageResult>('ChatService', 'SendMessage', {
      match_id: matchId,
      sender_id: this.userId,
      content,
      type: 'TEXT'
    });
  }

  async getConversation(matchId: string, page = 0, size = 50): Promise<ConversationResult> {
    return this.rpc<ConversationResult>('ChatService', 'GetConversation', {
      match_id: matchId,
      user_id: this.userId,
      page,
      size
    });
  }

  async markRead(matchId: string): Promise<void> {
    await this.rpc<any>('ChatService', 'MarkRead', {
      match_id: matchId,
      user_id: this.userId
    });
  }

  // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  // Image Upload (REST — binary uploads stay REST)
  // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  private get baseUrl(): string {
    // grpcUrl is 'http://localhost:8080/grpc', strip '/grpc' for REST endpoints
    return this.grpcUrl.replace(/\/grpc$/, '');
  }

  async uploadPhoto(file: File): Promise<PhotoUploadResult> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('userId', this.userId!);

    const response = await fetch(`${this.baseUrl}/uploads/photos`, {
      method: 'POST',
      body: formData,
    });

    if (!response.ok) {
      const err = await response.json().catch(() => ({ message: 'Upload failed' }));
      throw new Error(err.message);
    }

    return response.json();
  }

  async deletePhoto(photoIndex: number): Promise<PhotoDeleteResult> {
    const response = await fetch(
      `${this.baseUrl}/uploads/photos?userId=${this.userId}&photoIndex=${photoIndex}`,
      { method: 'DELETE' }
    );

    if (!response.ok) {
      const err = await response.json().catch(() => ({ message: 'Delete failed' }));
      throw new Error(err.message);
    }

    return response.json();
  }

  async reorderPhotos(photoUrls: string[]): Promise<any> {
    const response = await fetch(`${this.baseUrl}/uploads/photos/reorder`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ userId: this.userId, photo_urls: photoUrls }),
    });

    if (!response.ok) throw new Error('Reorder failed');
    return response.json();
  }

  /**
   * Resolve a photo URL — local uploads need the base URL prepended.
   */
  resolvePhotoUrl(url: string): string {
    if (!url) return 'https://via.placeholder.com/400x500?text=No+Photo';
    if (url.startsWith('http')) return url;
    return `${this.baseUrl}${url}`;
  }

  // ── Private ──────────────────────────────────────────
  private storeAuth(res: AuthState): void {
    // Normalize gRPC field names (snake_case) to our model
    const normalized: AuthState = {
      token: res.token,
      userId: res.user_id || res.userId,
      displayName: res.display_name || res.displayName,
    };
    localStorage.setItem('auth', JSON.stringify(normalized));
    this.currentUser$.next(normalized);
  }
}

// ── Types matching protobuf messages ────────────────────
export interface AuthState {
  token: string;
  userId: string;
  displayName: string;
  user_id?: string;
  display_name?: string;
}

export interface RegisterPayload {
  email: string;
  password: string;
  displayName: string;
  dateOfBirth: string;
  gender: string;
}

export interface ProfileData {
  user_id: string;
  display_name: string;
  age: number;
  bio: string;
  gender: string;
  photo_urls: string[];
  interests: string[];
  city: string;
  distance_km: number;
  latitude: number;
  longitude: number;
  max_distance_km: number;
  min_age_preference: number;
  max_age_preference: number;
}

export interface ProfileUpdate {
  display_name?: string;
  bio?: string;
  gender_preference?: string;
  max_distance_km?: number;
  min_age_preference?: number;
  max_age_preference?: number;
  photo_urls?: string[];
  interests?: string[];
  weight?: number;
  weight_unit?: string;
  height?: number;
  height_unit?: string;
  min_weight_preference?: number;
  max_weight_preference?: number;
  min_height_preference?: number;
  max_height_preference?: number;
}

export interface ProfileCard {
  user_id: string;
  display_name: string;
  age: number;
  bio: string;
  photo_urls: string[];
  distance_km: number;
  interests: string[];
  location_city: string;
}

export interface DiscoverResponse {
  profiles: ProfileCard[];
}

export interface SwipeResult {
  is_match: boolean;
  match_id?: string;
  matched_profile?: ProfileCard;
}

export interface MatchEntry {
  match_id: string;
  profile: ProfileCard;
  matched_at: string;
  last_message?: ChatMsg;
}

export interface MatchesResponse {
  matches: MatchEntry[];
  total: number;
}

export interface ChatMsg {
  message_id: string;
  sender_id: string;
  content: string;
  type: string;
  sent_at: string;
  read: boolean;
}

export interface SendMessageResult {
  message_id: string;
  sent_at: string;
  delivered: boolean;
}

export interface ConversationResult {
  messages: ChatMsg[];
  total: number;
}

export interface PhotoUploadResult {
  url: string;
  photo_urls: string[];
  total_photos: number;
}

export interface PhotoDeleteResult {
  removed: string;
  photo_urls: string[];
}
