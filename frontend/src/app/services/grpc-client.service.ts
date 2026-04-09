import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthStateService, AuthState } from './auth-state.service';
import { GrpcTransport } from './grpc-transport.service';

/**
 * Facade service — delegates to AuthStateService for state and GrpcTransport for RPCs.
 * Each method maps 1:1 to a protobuf RPC.
 */
@Injectable({ providedIn: 'root' })
export class GrpcClientService {

  constructor(
    private auth: AuthStateService,
    private transport: GrpcTransport
  ) {}

  // ── Auth state (delegated) ───────────────────────────
  get currentUser(): Observable<AuthState | null> { return this.auth.currentUser; }
  get userId(): string | null { return this.auth.userId; }
  get token(): string | null { return this.auth.token; }
  get isLoggedIn(): boolean { return this.auth.isLoggedIn; }

  // ── AuthService RPCs ─────────────────────────────────
  async login(email: string, password: string): Promise<AuthState> {
    const result = await this.transport.call<AuthState>('AuthService', 'Login', { email, password });
    this.auth.store(result);
    return result;
  }

  async register(data: RegisterPayload): Promise<AuthState> {
    const result = await this.transport.call<AuthState>('AuthService', 'Register', {
      email: data.email, password: data.password,
      display_name: data.displayName, date_of_birth: data.dateOfBirth, gender: data.gender,
    });
    this.auth.store(result);
    return result;
  }

  logout(): void { this.auth.clear(); }

  // ── ProfileService RPCs ──────────────────────────────
  async getMyProfile(): Promise<ProfileData> {
    return this.transport.call<ProfileData>('ProfileService', 'GetMyProfile', { user_id: this.userId });
  }

  async updateProfile(update: ProfileUpdate): Promise<ProfileData> {
    return this.transport.call<ProfileData>('ProfileService', 'UpdateProfile', { user_id: this.userId, ...update });
  }

  async updateLocation(lat: number, lng: number, city: string): Promise<ProfileData> {
    return this.transport.call<ProfileData>('ProfileService', 'UpdateLocation', {
      user_id: this.userId, latitude: lat, longitude: lng, city
    });
  }

  async getProfileById(targetUserId: string): Promise<ProfileData> {
    return this.transport.call<ProfileData>('ProfileService', 'GetProfile', {
      user_id: this.userId, target_user_id: targetUserId
    });
  }

  // ── MatchingService RPCs ─────────────────────────────
  async discover(limit = 10): Promise<DiscoverResponse> {
    return this.transport.call<DiscoverResponse>('MatchingService', 'GetPotentialMatches', {
      user_id: this.userId, limit
    });
  }

  async swipe(swipedId: string, direction: string): Promise<SwipeResult> {
    return this.transport.call<SwipeResult>('MatchingService', 'RecordSwipe', {
      swiper_id: this.userId, swiped_id: swipedId, direction
    });
  }

  async getMatches(): Promise<MatchesResponse> {
    return this.transport.call<MatchesResponse>('MatchingService', 'GetMatches', {
      user_id: this.userId, page: 0, size: 50
    });
  }

  async unmatch(matchId: string): Promise<void> {
    await this.transport.call<any>('MatchingService', 'UnmatchUser', {
      user_id: this.userId, match_id: matchId
    });
  }

  // ── ChatService RPCs ─────────────────────────────────
  async sendMessage(matchId: string, content: string): Promise<SendMessageResult> {
    return this.transport.call<SendMessageResult>('ChatService', 'SendMessage', {
      match_id: matchId, sender_id: this.userId, content, type: 'TEXT'
    });
  }

  async getConversation(matchId: string, page = 0, size = 50): Promise<ConversationResult> {
    return this.transport.call<ConversationResult>('ChatService', 'GetConversation', {
      match_id: matchId, user_id: this.userId, page, size
    });
  }

  async markRead(matchId: string): Promise<void> {
    await this.transport.call<any>('ChatService', 'MarkRead', {
      match_id: matchId, user_id: this.userId
    });
  }

  // ── Image Upload (REST) ──────────────────────────────
  async uploadPhoto(file: File): Promise<PhotoUploadResult> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('userId', this.userId!);
    const response = await fetch(`${this.transport.baseUrl}/uploads/photos`, {
      method: 'POST', body: formData,
    });
    if (!response.ok) {
      const err = await response.json().catch(() => ({ message: 'Upload failed' }));
      throw new Error(err.message);
    }
    return response.json();
  }

  async deletePhoto(photoIndex: number): Promise<PhotoDeleteResult> {
    const response = await fetch(
      `${this.transport.baseUrl}/uploads/photos?userId=${this.userId}&photoIndex=${photoIndex}`,
      { method: 'DELETE' }
    );
    if (!response.ok) {
      const err = await response.json().catch(() => ({ message: 'Delete failed' }));
      throw new Error(err.message);
    }
    return response.json();
  }

  async reorderPhotos(photoUrls: string[]): Promise<any> {
    const response = await fetch(`${this.transport.baseUrl}/uploads/photos/reorder`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ userId: this.userId, photo_urls: photoUrls }),
    });
    if (!response.ok) throw new Error('Reorder failed');
    return response.json();
  }

  resolvePhotoUrl(url: string): string {
    return this.transport.resolvePhotoUrl(url);
  }

  // ── SwipeHistoryService RPCs ─────────────────────────
  async getSwipeHistory(direction = 'ALL', namePrefix = '', interest = ''): Promise<SwipeHistoryResponse> {
    return this.transport.call<SwipeHistoryResponse>('SwipeHistoryService', 'GetHistory', {
      user_id: this.userId, direction, name_prefix: namePrefix, interest
    });
  }

  async autocompleteName(prefix: string): Promise<AutocompleteResponse> {
    return this.transport.call<AutocompleteResponse>('SwipeHistoryService', 'AutocompleteName', {
      user_id: this.userId, prefix
    });
  }
}

// ── Types ────────────────────────────────────────────────
export interface RegisterPayload {
  email: string; password: string; displayName: string;
  dateOfBirth: string; gender: string;
}

export interface ProfileData {
  user_id: string; display_name: string; age: number; bio: string;
  gender: string; photo_urls: string[]; interests: string[];
  city: string; distance_km: number; latitude: number; longitude: number;
  max_distance_km: number; min_age_preference: number; max_age_preference: number;
  weight: number; weight_unit: string; height: number; height_unit: string;
  min_weight_preference: number; max_weight_preference: number;
  min_height_preference: number; max_height_preference: number;
  show_men: boolean; show_women: boolean;
  show_mtf_trans: boolean; show_ftm_trans: boolean; show_non_binary: boolean;
}

export interface ProfileUpdate {
  display_name?: string; bio?: string; gender_preference?: string;
  max_distance_km?: number; min_age_preference?: number; max_age_preference?: number;
  photo_urls?: string[]; interests?: string[];
  weight?: number; weight_unit?: string; height?: number; height_unit?: string;
  min_weight_preference?: number; max_weight_preference?: number;
  min_height_preference?: number; max_height_preference?: number;
  show_men?: boolean; show_women?: boolean;
  show_mtf_trans?: boolean; show_ftm_trans?: boolean; show_non_binary?: boolean;
}

export interface ProfileCard {
  user_id: string; display_name: string; age: number; bio: string;
  photo_urls: string[]; distance_km: number; interests: string[]; location_city: string;
}

export interface DiscoverResponse { profiles: ProfileCard[]; }

export interface SwipeResult {
  is_match: boolean; match_id?: string; matched_profile?: ProfileCard;
}

export interface MatchEntry {
  match_id: string; profile: ProfileCard; matched_at: string;
  last_message?: ChatMsg; unread_count: number;
}

export interface MatchesResponse { matches: MatchEntry[]; total: number; }

export interface ChatMsg {
  message_id: string; sender_id: string; content: string;
  type: string; sent_at: string; read: boolean;
}

export interface SendMessageResult { message_id: string; sent_at: string; delivered: boolean; }
export interface ConversationResult { messages: ChatMsg[]; total: number; }
export interface PhotoUploadResult { url: string; photo_urls: string[]; total_photos: number; }
export interface PhotoDeleteResult { removed: string; photo_urls: string[]; }

export interface SwipeHistoryEntry {
  swipe_id: string;
  direction: string;
  swiped_at: string;
  profile: ProfileCard;
}

export interface SwipeHistoryResponse { swipes: SwipeHistoryEntry[]; total: number; }
export interface AutocompleteResponse { names: string[]; }
