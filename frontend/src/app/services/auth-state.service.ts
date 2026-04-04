import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

/**
 * Auth state management — single responsibility: store/retrieve auth tokens.
 * Decoupled from transport layer.
 */
@Injectable({ providedIn: 'root' })
export class AuthStateService {
  private currentUser$ = new BehaviorSubject<AuthState | null>(null);

  constructor() {
    const stored = localStorage.getItem('auth');
    if (stored) this.currentUser$.next(JSON.parse(stored));
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

  store(res: AuthState): void {
    const normalized: AuthState = {
      token: res.token,
      userId: res.user_id || res.userId,
      displayName: res.display_name || res.displayName,
    };
    localStorage.setItem('auth', JSON.stringify(normalized));
    this.currentUser$.next(normalized);
  }

  clear(): void {
    localStorage.removeItem('auth');
    this.currentUser$.next(null);
  }
}

export interface AuthState {
  token: string;
  userId: string;
  displayName: string;
  user_id?: string;
  display_name?: string;
}
