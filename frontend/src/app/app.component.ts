import { Component, OnInit, Input, Output, EventEmitter, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule, ActivatedRoute } from '@angular/router';
import { trigger, transition, style, animate } from '@angular/animations';
import {
  GrpcClientService, ProfileCard, MatchEntry, ChatMsg
} from './services/grpc-client.service';

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// SWIPE CARD COMPONENT
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
@Component({
  selector: 'app-swipe-card',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="swipe-card"
         [class.swiping]="isSwiping"
         [style.transform]="cardTransform"
         [style.opacity]="cardOpacity"
         (mousedown)="onDragStart($event)"
         (touchstart)="onTouchStart($event)">
      <div class="photo-container">
        <img [src]="resolveUrl(profile.photo_urls[currentPhoto])"
             [alt]="profile.display_name"
             class="profile-photo"
             (error)="onImageError($event)">
        <div class="photo-indicators" *ngIf="profile.photo_urls.length > 1">
          <div *ngFor="let photo of profile.photo_urls; let i = index"
               class="indicator" [class.active]="i === currentPhoto"
               (click)="currentPhoto = i; $event.stopPropagation()"></div>
        </div>
        <div class="photo-nav left" (click)="prevPhoto($event)"></div>
        <div class="photo-nav right" (click)="nextPhoto($event)"></div>
        <div class="swipe-indicator like" [style.opacity]="likeOpacity"><span>LIKE</span></div>
        <div class="swipe-indicator nope" [style.opacity]="nopeOpacity"><span>NOPE</span></div>
        <div class="swipe-indicator super" [style.opacity]="superOpacity"><span>SUPER</span></div>
        <div class="gradient-overlay"></div>
      </div>
      <div class="profile-info" (click)="toggleDetails()">
        <div class="name-row">
          <h2>{{ profile.display_name }}<span class="age">, {{ profile.age }}</span></h2>
          <span class="distance" *ngIf="profile.distance_km">{{ profile.distance_km }} km</span>
        </div>
        <p class="city" *ngIf="profile.location_city">📍 {{ profile.location_city }}</p>
        <p class="bio" *ngIf="showDetails && profile.bio">{{ profile.bio }}</p>
        <div class="interests" *ngIf="showDetails && profile.interests?.length">
          <span class="tag" *ngFor="let interest of profile.interests">{{ interest }}</span>
        </div>
        <div class="grpc-badge">gRPC</div>
      </div>
    </div>
  `,
  styles: [`
    .swipe-card {
      position: absolute; width: 100%; max-width: 400px; height: 580px;
      border-radius: 16px; overflow: hidden; background: #1a1a2e;
      box-shadow: 0 8px 40px rgba(0,0,0,0.4); cursor: grab; user-select: none;
    }
    .swipe-card.swiping { cursor: grabbing; }
    .photo-container { position: relative; width: 100%; height: 420px; overflow: hidden; }
    .profile-photo { width: 100%; height: 100%; object-fit: cover; pointer-events: none; }
    .photo-indicators {
      position: absolute; top: 8px; left: 12px; right: 12px;
      display: flex; gap: 4px; z-index: 10;
    }
    .indicator {
      flex: 1; height: 3px; background: rgba(255,255,255,0.4);
      border-radius: 2px; cursor: pointer;
    }
    .indicator.active { background: #fff; }
    .photo-nav { position: absolute; top: 0; bottom: 0; width: 40%; z-index: 5; }
    .photo-nav.left { left: 0; }
    .photo-nav.right { right: 0; }
    .swipe-indicator {
      position: absolute; top: 50%; padding: 12px 24px; border: 4px solid;
      border-radius: 12px; font-size: 2.2rem; font-weight: 900;
      letter-spacing: 4px; pointer-events: none; z-index: 15;
    }
    .swipe-indicator.like { left: 24px; color: #00e676; border-color: #00e676; transform: translateY(-50%) rotate(-15deg); }
    .swipe-indicator.nope { right: 24px; color: #ff1744; border-color: #ff1744; transform: translateY(-50%) rotate(15deg); }
    .swipe-indicator.super { left: 50%; top: 40%; color: #00b0ff; border-color: #00b0ff; transform: translate(-50%, -50%); }
    .gradient-overlay {
      position: absolute; bottom: 0; left: 0; right: 0; height: 50%;
      background: linear-gradient(transparent, rgba(26,26,46,0.95)); pointer-events: none;
    }
    .profile-info { padding: 16px 20px; color: #fff; cursor: pointer; position: relative; }
    .name-row { display: flex; justify-content: space-between; align-items: baseline; }
    .name-row h2 { margin: 0; font-size: 1.6rem; font-weight: 700; }
    .age { font-weight: 400; opacity: 0.85; }
    .distance { font-size: 0.85rem; color: rgba(255,255,255,0.6); }
    .city { margin: 4px 0 0; font-size: 0.9rem; color: rgba(255,255,255,0.7); }
    .bio { margin: 8px 0 0; font-size: 0.9rem; line-height: 1.4; color: rgba(255,255,255,0.8); }
    .interests { display: flex; flex-wrap: wrap; gap: 6px; margin-top: 10px; }
    .tag {
      padding: 4px 12px; background: rgba(255,255,255,0.12);
      border-radius: 20px; font-size: 0.78rem; color: rgba(255,255,255,0.85);
    }
    .grpc-badge {
      position: absolute; top: 12px; right: 16px;
      padding: 2px 8px; background: rgba(0,176,255,0.2); border: 1px solid rgba(0,176,255,0.4);
      border-radius: 4px; font-size: 0.65rem; font-weight: 700; color: #00b0ff;
      letter-spacing: 1px;
    }
  `]
})
export class SwipeCardComponent {
  @Input() profile!: ProfileCard;
  @Output() swiped = new EventEmitter<string>();
  currentPhoto = 0;
  showDetails = false;
  isSwiping = false;
  private startX = 0; private startY = 0;
  private deltaX = 0; private deltaY = 0;

  constructor(private grpc: GrpcClientService) {}

  resolveUrl(url: string): string { return this.grpc.resolvePhotoUrl(url); }

  get cardTransform(): string {
    if (!this.isSwiping) return '';
    return `translate(${this.deltaX}px, ${this.deltaY}px) rotate(${this.deltaX * 0.08}deg)`;
  }
  get cardOpacity(): number { return this.isSwiping ? Math.max(0.4, 1 - Math.abs(this.deltaX) / 500) : 1; }
  get likeOpacity(): number { return this.deltaX > 30 ? Math.min(1, (this.deltaX - 30) / 80) : 0; }
  get nopeOpacity(): number { return this.deltaX < -30 ? Math.min(1, (-this.deltaX - 30) / 80) : 0; }
  get superOpacity(): number { return this.deltaY < -60 ? Math.min(1, (-this.deltaY - 60) / 80) : 0; }

  nextPhoto(e: Event): void { e.stopPropagation(); if (this.currentPhoto < this.profile.photo_urls.length - 1) this.currentPhoto++; }
  prevPhoto(e: Event): void { e.stopPropagation(); if (this.currentPhoto > 0) this.currentPhoto--; }
  toggleDetails(): void { this.showDetails = !this.showDetails; }
  onImageError(e: Event): void { (e.target as HTMLImageElement).src = 'https://via.placeholder.com/400x500?text=No+Photo'; }

  onDragStart(e: MouseEvent): void {
    this.startX = e.clientX; this.startY = e.clientY; this.isSwiping = true;
    const onMove = (ev: MouseEvent) => { this.deltaX = ev.clientX - this.startX; this.deltaY = ev.clientY - this.startY; };
    const onEnd = () => { document.removeEventListener('mousemove', onMove); document.removeEventListener('mouseup', onEnd); this.finishSwipe(); };
    document.addEventListener('mousemove', onMove);
    document.addEventListener('mouseup', onEnd);
  }

  onTouchStart(e: TouchEvent): void {
    const t = e.touches[0]; this.startX = t.clientX; this.startY = t.clientY; this.isSwiping = true;
    const onMove = (ev: TouchEvent) => { this.deltaX = ev.touches[0].clientX - this.startX; this.deltaY = ev.touches[0].clientY - this.startY; };
    const onEnd = () => { document.removeEventListener('touchmove', onMove); document.removeEventListener('touchend', onEnd); this.finishSwipe(); };
    document.addEventListener('touchmove', onMove);
    document.addEventListener('touchend', onEnd);
  }

  private finishSwipe(): void {
    const t = 100;
    if (this.deltaX > t) this.swiped.emit('RIGHT');
    else if (this.deltaX < -t) this.swiped.emit('LEFT');
    else if (this.deltaY < -t) this.swiped.emit('SUPER_LIKE');
    this.isSwiping = false; this.deltaX = 0; this.deltaY = 0;
  }
}

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// AUTH COMPONENT (gRPC: AuthService.Login / AuthService.Register)
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="auth-container">
      <div class="auth-card">
        <div class="logo">
          <svg width="48" height="48" viewBox="0 0 48 48">
            <path d="M24 44s-18-11-18-26c0-8 6-14 12-14 4 0 6 3 6 3s2-3 6-3c6 0 12 6 12 14 0 15-18 26-18 26z"
                  fill="url(#flame)" />
            <defs>
              <linearGradient id="flame" x1="0" y1="0" x2="0" y2="1">
                <stop offset="0%" stop-color="#FF6B6B"/><stop offset="100%" stop-color="#EE5A24"/>
              </linearGradient>
            </defs>
          </svg>
          <h1>Claspr</h1>
          <div class="proto-badge">Powered by gRPC</div>
        </div>
        <div class="tabs">
          <button [class.active]="mode === 'login'" (click)="mode = 'login'">Sign In</button>
          <button [class.active]="mode === 'register'" (click)="mode = 'register'">Sign Up</button>
        </div>
        <div class="form" *ngIf="mode === 'login'">
          <input type="email" [(ngModel)]="email" placeholder="Email" />
          <input type="password" [(ngModel)]="password" placeholder="Password" />
          <button class="btn-primary" (click)="login()" [disabled]="loading">
            {{ loading ? 'Calling AuthService.Login...' : 'Sign In' }}
          </button>
        </div>
        <div class="form" *ngIf="mode === 'register'">
          <input type="text" [(ngModel)]="displayName" placeholder="Display Name" />
          <input type="email" [(ngModel)]="email" placeholder="Email" />
          <input type="password" [(ngModel)]="password" placeholder="Password" />
          <input type="date" [(ngModel)]="dob" />
          <select [(ngModel)]="gender">
            <option value="">Select Gender</option>
            <option value="MALE">Male</option>
            <option value="FEMALE">Female</option>
            <option value="NON_BINARY">Non-Binary</option>
            <option value="OTHER">Other</option>
          </select>
          <button class="btn-primary" (click)="register()" [disabled]="loading">
            {{ loading ? 'Calling AuthService.Register...' : 'Create Account' }}
          </button>
        </div>
        <p class="error" *ngIf="error">{{ error }}</p>
        <p class="demo-hint">Demo: sophie&#64;demo.com / password123</p>
      </div>
    </div>
  `,
  styles: [`
    .auth-container { display:flex; align-items:center; justify-content:center; min-height:100vh; background:linear-gradient(135deg,#0f0c29 0%,#302b63 50%,#24243e 100%); padding:20px; }
    .auth-card { width:100%; max-width:380px; background:rgba(255,255,255,0.06); backdrop-filter:blur(20px); border:1px solid rgba(255,255,255,0.1); border-radius:24px; padding:40px 32px; }
    .logo { text-align:center; margin-bottom:32px; }
    .logo h1 { font-size:2rem; font-weight:800; background:linear-gradient(135deg,#FF6B6B,#EE5A24); -webkit-background-clip:text; -webkit-text-fill-color:transparent; margin-top:8px; }
    .proto-badge { display:inline-block; margin-top:8px; padding:3px 10px; background:rgba(0,176,255,0.15); border:1px solid rgba(0,176,255,0.3); border-radius:6px; font-size:0.72rem; font-weight:600; color:#00b0ff; letter-spacing:0.5px; }
    .tabs { display:flex; margin-bottom:24px; background:rgba(255,255,255,0.05); border-radius:12px; overflow:hidden; }
    .tabs button { flex:1; padding:12px; border:none; background:transparent; color:rgba(255,255,255,0.5); font-size:0.95rem; font-weight:600; cursor:pointer; transition:all 0.3s; }
    .tabs button.active { background:rgba(255,255,255,0.1); color:#fff; }
    .form { display:flex; flex-direction:column; gap:14px; }
    .form input,.form select { padding:14px 16px; background:rgba(255,255,255,0.08); border:1px solid rgba(255,255,255,0.12); border-radius:12px; color:#fff; font-size:0.95rem; outline:none; }
    .form input::placeholder { color:rgba(255,255,255,0.35); }
    .form input:focus,.form select:focus { border-color:#EE5A24; }
    .form select option { background:#1a1a2e; }
    .btn-primary { padding:14px; background:linear-gradient(135deg,#FF6B6B,#EE5A24); border:none; border-radius:12px; color:#fff; font-size:1rem; font-weight:700; cursor:pointer; margin-top:6px; }
    .btn-primary:disabled { opacity:0.6; }
    .error { color:#ff5252; text-align:center; margin-top:12px; font-size:0.85rem; }
    .demo-hint { text-align:center; color:rgba(255,255,255,0.3); font-size:0.78rem; margin-top:20px; }
  `]
})
export class AuthComponent {
  mode: 'login' | 'register' = 'login';
  email = ''; password = ''; displayName = ''; dob = ''; gender = '';
  loading = false; error = '';

  constructor(private grpc: GrpcClientService, private router: Router) {}

  async login(): Promise<void> {
    this.loading = true; this.error = '';
    try {
      await this.grpc.login(this.email, this.password);
      this.router.navigate(['/discover']);
    } catch (e: any) { this.error = e.message || 'Login failed'; }
    finally { this.loading = false; }
  }

  async register(): Promise<void> {
    this.loading = true; this.error = '';
    try {
      await this.grpc.register({
        email: this.email, password: this.password,
        displayName: this.displayName, dateOfBirth: this.dob, gender: this.gender
      });
      this.router.navigate(['/discover']);
    } catch (e: any) { this.error = e.message || 'Registration failed'; }
    finally { this.loading = false; }
  }
}

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// DISCOVER PAGE (gRPC: MatchingService.GetPotentialMatches / RecordSwipe)
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
@Component({
  selector: 'app-discover',
  standalone: true,
  imports: [CommonModule, SwipeCardComponent],
  animations: [
    trigger('matchPop', [
      transition(':enter', [
        style({ transform: 'scale(0)', opacity: 0 }),
        animate('600ms cubic-bezier(0.175,0.885,0.32,1.275)', style({ transform: 'scale(1)', opacity: 1 }))
      ])
    ])
  ],
  template: `
    <div class="discover-page">
      <div class="transport-indicator">
        <span class="dot"></span> gRPC-Web · MatchingService
      </div>

      <div class="card-stack" *ngIf="profiles.length > 0">
        <app-swipe-card *ngFor="let p of profiles.slice(0,2); let i = index"
          [profile]="p" [style.z-index]="10-i"
          [style.transform]="i===1?'scale(0.95) translateY(12px)':''"
          (swiped)="onSwipe($event, p)">
        </app-swipe-card>
      </div>

      <div class="empty-state" *ngIf="profiles.length === 0 && !loading">
        <div class="empty-icon">💫</div>
        <h3>No more profiles</h3>
        <p>Check back soon for new people!</p>
        <button class="btn-refresh" (click)="loadProfiles()">Refresh</button>
      </div>
      <div class="loading-state" *ngIf="loading">
        <div class="spinner"></div>
        <p>GetPotentialMatches...</p>
      </div>

      <div class="actions" *ngIf="profiles.length > 0">
        <button class="action-btn nope" (click)="buttonSwipe('LEFT')">
          <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>
        </button>
        <button class="action-btn super" (click)="buttonSwipe('SUPER_LIKE')">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="currentColor"><polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26"/></svg>
        </button>
        <button class="action-btn like" (click)="buttonSwipe('RIGHT')">
          <svg width="28" height="28" viewBox="0 0 24 24" fill="currentColor"><path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/></svg>
        </button>
      </div>

      <div class="match-overlay" *ngIf="matchedProfile" (click)="dismissMatch()">
        <div class="match-content" @matchPop>
          <h2>It's a Match! 🎉</h2>
          <p>You and {{ matchedProfile.display_name }} liked each other</p>
          <img [src]="resolveMatchPhoto()" class="match-photo" />
          <div class="match-actions">
            <button class="btn-message" (click)="goToChat($event)">Send Message</button>
            <button class="btn-later" (click)="dismissMatch()">Keep Swiping</button>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .discover-page { display:flex; flex-direction:column; align-items:center; padding:24px 16px 100px; min-height:calc(100vh - 60px); background:linear-gradient(180deg,#0f0c29 0%,#1a1a2e 100%); }
    .transport-indicator { display:flex; align-items:center; gap:6px; padding:4px 12px; background:rgba(0,176,255,0.1); border:1px solid rgba(0,176,255,0.2); border-radius:20px; font-size:0.7rem; color:#00b0ff; margin-bottom:16px; font-weight:500; }
    .dot { width:6px; height:6px; border-radius:50%; background:#00e676; animation:pulse 2s infinite; }
    @keyframes pulse { 0%,100% { opacity:1; } 50% { opacity:0.4; } }
    .card-stack { position:relative; width:100%; max-width:400px; height:580px; display:flex; justify-content:center; }
    .actions { display:flex; gap:20px; margin-top:24px; }
    .action-btn { width:60px; height:60px; border-radius:50%; border:2px solid; background:rgba(255,255,255,0.05); cursor:pointer; display:flex; align-items:center; justify-content:center; transition:all 0.25s; }
    .action-btn.nope { color:#ff5252; border-color:#ff5252; }
    .action-btn.nope:hover { background:#ff5252; color:#fff; transform:scale(1.1); }
    .action-btn.like { color:#00e676; border-color:#00e676; }
    .action-btn.like:hover { background:#00e676; color:#fff; transform:scale(1.1); }
    .action-btn.super { color:#00b0ff; border-color:#00b0ff; width:50px; height:50px; }
    .action-btn.super:hover { background:#00b0ff; color:#fff; transform:scale(1.1); }
    .empty-state,.loading-state { text-align:center; color:rgba(255,255,255,0.6); margin-top:120px; }
    .empty-icon { font-size:4rem; margin-bottom:16px; }
    .empty-state h3 { color:#fff; font-size:1.3rem; margin-bottom:8px; }
    .btn-refresh { margin-top:16px; padding:12px 32px; background:linear-gradient(135deg,#FF6B6B,#EE5A24); border:none; border-radius:24px; color:#fff; font-weight:600; cursor:pointer; }
    .spinner { width:40px; height:40px; border:3px solid rgba(255,255,255,0.1); border-top-color:#EE5A24; border-radius:50%; animation:spin 0.8s linear infinite; margin:0 auto 16px; }
    @keyframes spin { to { transform:rotate(360deg); } }
    .match-overlay { position:fixed; inset:0; background:rgba(0,0,0,0.85); display:flex; align-items:center; justify-content:center; z-index:1000; }
    .match-content { text-align:center; color:#fff; padding:40px; }
    .match-content h2 { font-size:2.2rem; background:linear-gradient(135deg,#FF6B6B,#EE5A24); -webkit-background-clip:text; -webkit-text-fill-color:transparent; }
    .match-content p { color:rgba(255,255,255,0.7); margin:8px 0 24px; }
    .match-photo { width:120px; height:120px; border-radius:50%; object-fit:cover; border:3px solid #EE5A24; margin-bottom:24px; }
    .match-actions { display:flex; flex-direction:column; gap:12px; align-items:center; }
    .btn-message { padding:14px 40px; background:linear-gradient(135deg,#FF6B6B,#EE5A24); border:none; border-radius:24px; color:#fff; font-weight:700; cursor:pointer; }
    .btn-later { background:transparent; border:1px solid rgba(255,255,255,0.3); padding:10px 32px; border-radius:24px; color:rgba(255,255,255,0.6); cursor:pointer; }
  `]
})
export class DiscoverComponent implements OnInit {
  profiles: ProfileCard[] = [];
  loading = false;
  matchedProfile: ProfileCard | null = null;
  private matchId: string | null = null;

  constructor(private grpc: GrpcClientService, private router: Router) {}

  ngOnInit(): void { this.loadProfiles(); }

  async loadProfiles(): Promise<void> {
    this.loading = true;
    try {
      const res = await this.grpc.discover(20);
      this.profiles = res.profiles || [];
    } catch (e) { console.error('GetPotentialMatches failed:', e); }
    finally { this.loading = false; }
  }

  async onSwipe(direction: string, profile: ProfileCard): Promise<void> {
    this.profiles = this.profiles.filter(p => p.user_id !== profile.user_id);
    try {
      const res = await this.grpc.swipe(profile.user_id, direction);
      if (res.is_match) {
        this.matchedProfile = profile;
        this.matchId = res.match_id ?? null;
      }
    } catch (e) { console.error('RecordSwipe failed:', e); }
    if (this.profiles.length < 3) this.loadProfiles();
  }

  buttonSwipe(dir: string): void { if (this.profiles.length > 0) this.onSwipe(dir, this.profiles[0]); }
  dismissMatch(): void { this.matchedProfile = null; }
  goToChat(e: Event): void { e.stopPropagation(); if (this.matchId) this.router.navigate(['/chat', this.matchId]); this.matchedProfile = null; }

  resolveMatchPhoto(): string {
    if (!this.matchedProfile) return 'https://via.placeholder.com/120?text=?';
    const url = this.matchedProfile.photo_urls?.[0];
    if (!url) return 'https://via.placeholder.com/120?text=?';
    return this.grpc.resolvePhotoUrl(url);
  }
}

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// MATCHES PAGE (gRPC: MatchingService.GetMatches)
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
@Component({
  selector: 'app-matches',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="matches-page">
      <div class="page-header">
        <h2>Messages</h2>
        <div class="proto-badge">gRPC-Web · MatchingService</div>
      </div>
      <div class="new-matches" *ngIf="newMatches.length > 0">
        <h3>New Matches</h3>
        <div class="match-circles">
          <a *ngFor="let m of newMatches" [routerLink]="['/chat', m.match_id]" class="match-circle">
            <img [src]="resolvePhoto(m.profile.photo_urls[0])" [alt]="m.profile.display_name" (error)="onImgError($event)" />
            <span>{{ m.profile.display_name }}</span>
          </a>
        </div>
      </div>
      <div class="conversations">
        <a *ngFor="let m of conversations" [routerLink]="['/chat', m.match_id]" class="convo-row">
          <img [src]="resolvePhoto(m.profile.photo_urls[0])" class="convo-avatar" (error)="onImgError($event)" />
          <div class="convo-info">
            <div class="convo-name">{{ m.profile.display_name }}</div>
            <div class="convo-last" *ngIf="m.last_message">
              {{ m.last_message.sender_id === myId ? 'You: ' : '' }}{{ m.last_message.content }}
            </div>
            <div class="convo-last" *ngIf="!m.last_message">Matched! Say hello 👋</div>
          </div>
          <div class="convo-time" *ngIf="m.last_message">{{ formatTime(m.last_message.sent_at) }}</div>
        </a>
      </div>
      <div class="empty-state" *ngIf="matches.length === 0 && !loading"><p>No matches yet. Keep swiping! 💫</p></div>
    </div>
  `,
  styles: [`
    .matches-page { min-height:calc(100vh - 60px); background:#0f0c29; padding:24px 16px 80px; color:#fff; }
    .page-header { display:flex; justify-content:space-between; align-items:center; margin-bottom:20px; }
    .page-header h2 { font-size:1.5rem; font-weight:800; }
    .proto-badge { padding:3px 10px; background:rgba(0,176,255,0.15); border:1px solid rgba(0,176,255,0.3); border-radius:6px; font-size:0.65rem; font-weight:600; color:#00b0ff; }
    .new-matches h3 { font-size:0.85rem; text-transform:uppercase; letter-spacing:1px; color:rgba(255,255,255,0.5); margin-bottom:12px; }
    .match-circles { display:flex; gap:16px; overflow-x:auto; padding-bottom:16px; margin-bottom:20px; border-bottom:1px solid rgba(255,255,255,0.08); }
    .match-circle { display:flex; flex-direction:column; align-items:center; gap:6px; text-decoration:none; flex-shrink:0; }
    .match-circle img { width:64px; height:64px; border-radius:50%; object-fit:cover; border:2px solid #EE5A24; }
    .match-circle span { font-size:0.75rem; color:rgba(255,255,255,0.7); }
    .convo-row { display:flex; align-items:center; gap:14px; padding:14px 0; border-bottom:1px solid rgba(255,255,255,0.06); text-decoration:none; color:inherit; cursor:pointer; }
    .convo-avatar { width:56px; height:56px; border-radius:50%; object-fit:cover; flex-shrink:0; }
    .convo-info { flex:1; min-width:0; }
    .convo-name { font-weight:600; }
    .convo-last { font-size:0.85rem; color:rgba(255,255,255,0.5); white-space:nowrap; overflow:hidden; text-overflow:ellipsis; margin-top:2px; }
    .convo-time { font-size:0.75rem; color:rgba(255,255,255,0.35); flex-shrink:0; }
    .empty-state { text-align:center; color:rgba(255,255,255,0.4); margin-top:80px; }
  `]
})
export class MatchesComponent implements OnInit {
  matches: MatchEntry[] = [];
  loading = false;
  myId: string | null = null;

  get newMatches() { return this.matches.filter(m => !m.last_message); }
  get conversations() { return this.matches.filter(m => m.last_message); }

  constructor(private grpc: GrpcClientService) { this.myId = grpc.userId; }

  async ngOnInit(): Promise<void> {
    this.loading = true;
    try {
      const res = await this.grpc.getMatches();
      this.matches = res.matches || [];
    } catch (e) { console.error('GetMatches failed:', e); }
    finally { this.loading = false; }
  }

  onImgError(e: Event): void { (e.target as HTMLImageElement).src = 'https://via.placeholder.com/64?text=?'; }
  resolvePhoto(url: string): string { return this.grpc.resolvePhotoUrl(url); }
  formatTime(iso: string): string {
    const d = new Date(iso); const diff = Date.now() - d.getTime();
    if (diff < 3600000) return Math.floor(diff / 60000) + 'm';
    if (diff < 86400000) return Math.floor(diff / 3600000) + 'h';
    return d.toLocaleDateString(undefined, { month:'short', day:'numeric' });
  }
}

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// CHAT (gRPC: ChatService.SendMessage / GetConversation / MarkRead)
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  template: `
    <div class="chat-page">
      <div class="chat-header">
        <a routerLink="/matches" class="back-btn">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="15 18 9 12 15 6"/></svg>
        </a>
        <div class="chat-title">Chat <span class="grpc-tag">ChatService RPC</span></div>
      </div>
      <div class="messages-container" #messagesContainer>
        <div *ngFor="let msg of messages" class="message"
             [class.mine]="msg.sender_id === myId" [class.theirs]="msg.sender_id !== myId">
          <div class="bubble">{{ msg.content }}</div>
          <div class="msg-time">{{ formatTime(msg.sent_at) }}</div>
        </div>
      </div>
      <div class="chat-input">
        <input type="text" [(ngModel)]="newMessage" placeholder="Type a message..."
               (keydown.enter)="send()" />
        <button class="send-btn" (click)="send()" [disabled]="!newMessage.trim()">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor"><path d="M2.01 21L23 12 2.01 3 2 10l15 2-15 2z"/></svg>
        </button>
      </div>
    </div>
  `,
  styles: [`
    .chat-page { display:flex; flex-direction:column; height:100vh; background:#0f0c29; color:#fff; }
    .chat-header { display:flex; align-items:center; gap:12px; padding:12px 16px; background:rgba(255,255,255,0.04); border-bottom:1px solid rgba(255,255,255,0.06); }
    .back-btn { color:#fff; text-decoration:none; display:flex; padding:4px; }
    .chat-title { font-weight:600; font-size:1rem; display:flex; align-items:center; gap:8px; }
    .grpc-tag { padding:2px 6px; background:rgba(0,176,255,0.15); border:1px solid rgba(0,176,255,0.3); border-radius:4px; font-size:0.6rem; color:#00b0ff; font-weight:600; }
    .messages-container { flex:1; overflow-y:auto; padding:16px; display:flex; flex-direction:column; gap:8px; }
    .message { display:flex; flex-direction:column; max-width:75%; }
    .message.mine { align-self:flex-end; align-items:flex-end; }
    .message.theirs { align-self:flex-start; }
    .bubble { padding:10px 16px; border-radius:20px; font-size:0.95rem; line-height:1.4; word-break:break-word; }
    .mine .bubble { background:linear-gradient(135deg,#EE5A24,#FF6B6B); border-bottom-right-radius:6px; }
    .theirs .bubble { background:rgba(255,255,255,0.1); border-bottom-left-radius:6px; }
    .msg-time { font-size:0.7rem; color:rgba(255,255,255,0.3); margin-top:4px; padding:0 4px; }
    .chat-input { display:flex; gap:10px; padding:12px 16px; background:rgba(255,255,255,0.04); border-top:1px solid rgba(255,255,255,0.06); }
    .chat-input input { flex:1; padding:12px 18px; background:rgba(255,255,255,0.08); border:1px solid rgba(255,255,255,0.1); border-radius:24px; color:#fff; font-size:0.95rem; outline:none; }
    .chat-input input::placeholder { color:rgba(255,255,255,0.3); }
    .send-btn { width:44px; height:44px; border-radius:50%; border:none; background:linear-gradient(135deg,#EE5A24,#FF6B6B); color:#fff; cursor:pointer; display:flex; align-items:center; justify-content:center; }
    .send-btn:disabled { opacity:0.4; }
  `]
})
export class ChatComponent implements OnInit {
  messages: ChatMsg[] = [];
  newMessage = '';
  myId: string | null = null;
  private matchId = '';
  private pollTimer: any;
  @ViewChild('messagesContainer') messagesContainer!: ElementRef;

  constructor(private grpc: GrpcClientService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.myId = this.grpc.userId;
    this.matchId = this.route.snapshot.paramMap.get('matchId') || '';
    this.loadMessages();
    this.pollTimer = setInterval(() => this.loadMessages(), 3000);
  }

  ngOnDestroy(): void { clearInterval(this.pollTimer); }

  async loadMessages(): Promise<void> {
    if (!this.matchId) return;
    try {
      const res = await this.grpc.getConversation(this.matchId);
      this.messages = (res.messages || []).reverse();
      this.scrollToBottom();
    } catch (e) { console.error('GetConversation failed:', e); }
  }

  async send(): Promise<void> {
    if (!this.newMessage.trim()) return;
    try {
      await this.grpc.sendMessage(this.matchId, this.newMessage);
      this.newMessage = '';
      await this.loadMessages();
    } catch (e) { console.error('SendMessage failed:', e); }
  }

  formatTime(iso: string): string { return new Date(iso).toLocaleTimeString([], { hour:'2-digit', minute:'2-digit' }); }
  private scrollToBottom(): void {
    setTimeout(() => { if (this.messagesContainer) { const el = this.messagesContainer.nativeElement; el.scrollTop = el.scrollHeight; } }, 50);
  }
}

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// PROFILE PAGE (gRPC: ProfileService + REST: Image Upload)
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="profile-page" *ngIf="profile">
      <div class="profile-header">
        <img [src]="resolvePhoto(profile.photo_urls[0])" class="profile-hero" (error)="onImgError($event)" />
        <div class="hero-overlay">
          <h2>{{ profile.display_name }}, {{ profile.age }}</h2>
          <p *ngIf="profile.city">📍 {{ profile.city }}</p>
        </div>
      </div>

      <!-- Photo Manager (Drag & Drop) -->
      <div class="profile-section">
        <h3>Photos <span class="drag-hint">Drag to reorder</span></h3>
        <div class="photo-grid">
          <div class="photo-slot"
               *ngFor="let url of profile.photo_urls; let i = index"
               [class.main]="i === 0"
               [class.dragging]="dragIndex === i"
               [class.drag-over]="dragOverIndex === i"
               draggable="true"
               (dragstart)="onDragStart(i, $event)"
               (dragend)="onDragEnd()"
               (dragover)="onDragOver(i, $event)"
               (dragleave)="onDragLeave()"
               (drop)="onDrop(i, $event)">
            <img [src]="resolvePhoto(url)" (error)="onImgError($event)" draggable="false" />
            <div class="photo-badge" *ngIf="i === 0">Main</div>
            <div class="photo-number">{{ i + 1 }}</div>
            <button class="photo-delete" (click)="deletePhoto(i, $event)">×</button>
            <div class="drag-handle">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor"><circle cx="9" cy="6" r="1.5"/><circle cx="15" cy="6" r="1.5"/><circle cx="9" cy="12" r="1.5"/><circle cx="15" cy="12" r="1.5"/><circle cx="9" cy="18" r="1.5"/><circle cx="15" cy="18" r="1.5"/></svg>
            </div>
          </div>

          <label class="photo-slot upload-slot" *ngIf="profile.photo_urls.length < 6">
            <input type="file" accept="image/jpeg,image/png,image/webp,image/gif"
                   (change)="onFileSelected($event)" hidden />
            <div class="upload-content">
              <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                <line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/>
              </svg>
              <span>Add Photo</span>
            </div>
          </label>
        </div>
        <div class="upload-status" *ngIf="uploading">Uploading...</div>
        <div class="upload-error" *ngIf="uploadError">{{ uploadError }}</div>
      </div>

      <div class="profile-section">
        <h3>About</h3>
        <textarea [(ngModel)]="bio" rows="3" placeholder="Tell people about yourself..."></textarea>
      </div>

      <!-- Weight & Height -->
      <div class="profile-section">
        <h3>Body</h3>
        <div class="body-row">
          <div class="body-field">
            <label>Weight</label>
            <div class="input-with-unit">
              <input type="number" [(ngModel)]="weightDisplay" placeholder="0" min="0" step="0.1" />
              <select [(ngModel)]="weightUnit" (ngModelChange)="onWeightUnitChange()">
                <option value="kg">kg</option>
                <option value="lb">lb</option>
              </select>
            </div>
          </div>
          <div class="body-field">
            <label>Height</label>
            <div class="input-with-unit">
              <input type="number" [(ngModel)]="heightDisplay" placeholder="0" min="0" step="0.01" />
              <select [(ngModel)]="heightUnit" (ngModelChange)="onHeightUnitChange()">
                <option value="cm">cm</option>
                <option value="ft">ft</option>
              </select>
            </div>
          </div>
        </div>
      </div>

      <div class="profile-section">
        <h3>Interests</h3>
        <div class="interest-tags">
          <span class="tag" *ngFor="let i of profile.interests; let idx = index">
            {{ i }}
            <button class="tag-remove" (click)="removeInterest(idx)">×</button>
          </span>
        </div>
        <div class="add-interest">
          <input type="text" [(ngModel)]="newInterest" placeholder="Add interest..." (keydown.enter)="addInterest()"/>
        </div>
      </div>

      <button class="btn-save" (click)="saveProfile()">{{ saving ? 'Saving...' : 'Save Changes' }}</button>
      <button class="btn-logout" (click)="logout()">Log Out</button>
    </div>
  `,
  styles: [`
    .profile-page { min-height:100vh; background:#0f0c29; color:#fff; padding-bottom:100px; }
    .profile-header { position:relative; height:280px; overflow:hidden; }
    .profile-hero { width:100%; height:100%; object-fit:cover; }
    .hero-overlay { position:absolute; bottom:0; left:0; right:0; padding:24px; background:linear-gradient(transparent,rgba(15,12,41,0.95)); }
    .hero-overlay h2 { font-size:1.6rem; margin:0; }
    .hero-overlay p { color:rgba(255,255,255,0.6); margin:4px 0 0; }
    .profile-section { padding:20px 16px; border-bottom:1px solid rgba(255,255,255,0.06); }
    .profile-section h3 { font-size:0.85rem; text-transform:uppercase; letter-spacing:1px; color:rgba(255,255,255,0.5); margin-bottom:12px; }
    .photo-grid { display:grid; grid-template-columns:repeat(3, 1fr); gap:10px; }
    .photo-slot { position:relative; aspect-ratio:3/4; border-radius:12px; overflow:hidden; border:2px solid rgba(255,255,255,0.1); cursor:grab; transition:all 0.2s; }
    .photo-slot:active { cursor:grabbing; }
    .photo-slot.main { border-color:#EE5A24; }
    .photo-slot.dragging { opacity:0.3; transform:scale(0.95); }
    .photo-slot.drag-over { border-color:#00b0ff; border-style:dashed; transform:scale(1.03); box-shadow:0 0 20px rgba(0,176,255,0.3); }
    .photo-slot img { width:100%; height:100%; object-fit:cover; pointer-events:none; }
    .photo-badge { position:absolute; top:6px; left:6px; padding:2px 8px; background:linear-gradient(135deg,#EE5A24,#FF6B6B); border-radius:4px; font-size:0.65rem; font-weight:700; }
    .photo-number { position:absolute; bottom:6px; left:6px; width:22px; height:22px; border-radius:50%; background:rgba(0,0,0,0.6); display:flex; align-items:center; justify-content:center; font-size:0.7rem; font-weight:700; }
    .photo-delete { position:absolute; top:6px; right:6px; width:24px; height:24px; border-radius:50%; border:none; background:rgba(0,0,0,0.6); color:#ff5252; font-size:1rem; cursor:pointer; display:flex; align-items:center; justify-content:center; opacity:0; transition:opacity 0.2s; z-index:2; }
    .photo-slot:hover .photo-delete { opacity:1; }
    .drag-handle { position:absolute; bottom:6px; right:6px; width:24px; height:24px; display:flex; align-items:center; justify-content:center; color:rgba(255,255,255,0.5); opacity:0; transition:opacity 0.2s; }
    .photo-slot:hover .drag-handle { opacity:1; }
    .drag-hint { font-size:0.7rem; font-weight:400; color:rgba(255,255,255,0.3); text-transform:none; letter-spacing:0; margin-left:8px; }
    .upload-slot { display:flex; align-items:center; justify-content:center; border:2px dashed rgba(255,255,255,0.2); background:rgba(255,255,255,0.03); }
    .upload-content { display:flex; flex-direction:column; align-items:center; gap:6px; color:rgba(255,255,255,0.4); }
    .upload-content span { font-size:0.75rem; }
    .upload-status { margin-top:10px; font-size:0.85rem; color:#00b0ff; }
    .upload-error { margin-top:10px; font-size:0.85rem; color:#ff5252; }
    .body-row { display:flex; gap:16px; }
    .body-field { flex:1; }
    .body-field label { display:block; font-size:0.85rem; color:rgba(255,255,255,0.6); margin-bottom:6px; }
    .input-with-unit { display:flex; gap:0; border-radius:12px; overflow:hidden; border:1px solid rgba(255,255,255,0.1); }
    .input-with-unit input { flex:1; padding:12px; background:rgba(255,255,255,0.06); border:none; color:#fff; font-size:0.95rem; outline:none; min-width:0; }
    .input-with-unit select { padding:8px 12px; background:rgba(255,255,255,0.1); border:none; border-left:1px solid rgba(255,255,255,0.1); color:#fff; font-size:0.85rem; outline:none; cursor:pointer; }
    .input-with-unit select option { background:#1a1a2e; }
    textarea,.add-interest input { width:100%; padding:12px; background:rgba(255,255,255,0.06); border:1px solid rgba(255,255,255,0.1); border-radius:12px; color:#fff; font-size:0.95rem; resize:none; outline:none; box-sizing:border-box; }
    .interest-tags { display:flex; flex-wrap:wrap; gap:8px; margin-bottom:12px; }
    .tag { padding:6px 14px; background:rgba(238,90,36,0.2); border:1px solid rgba(238,90,36,0.4); border-radius:20px; font-size:0.85rem; display:flex; align-items:center; gap:6px; }
    .tag-remove { background:none; border:none; color:rgba(255,255,255,0.4); cursor:pointer; font-size:1rem; padding:0; }
    .tag-remove:hover { color:#ff5252; }
    .btn-save { display:block; width:calc(100% - 32px); margin:24px 16px 12px; padding:14px; background:linear-gradient(135deg,#FF6B6B,#EE5A24); border:none; border-radius:12px; color:#fff; font-weight:700; font-size:1rem; cursor:pointer; }
    .btn-logout { display:block; width:calc(100% - 32px); margin:0 16px; padding:14px; background:transparent; border:1px solid rgba(255,255,255,0.15); border-radius:12px; color:rgba(255,255,255,0.5); cursor:pointer; }
  `]
})
export class ProfileComponent implements OnInit {
  profile: any = null;
  bio = ''; newInterest = ''; saving = false;
  uploading = false; uploadError = '';
  weightDisplay = 0; weightUnit = 'kg';
  heightDisplay = 0; heightUnit = 'cm';
  dragIndex: number | null = null;
  dragOverIndex: number | null = null;

  constructor(private grpc: GrpcClientService, private router: Router) {}

  async ngOnInit(): Promise<void> {
    try {
      this.profile = await this.grpc.getMyProfile();
      this.bio = this.profile.bio || '';
      this.weightUnit = this.profile.weight_unit || 'kg';
      this.heightUnit = this.profile.height_unit || 'cm';
      const wKg = this.profile.weight || 0;
      this.weightDisplay = this.weightUnit === 'lb' ? Math.round(wKg * 2.20462 * 10) / 10 : wKg;
      const hCm = this.profile.height || 0;
      this.heightDisplay = this.heightUnit === 'ft' ? Math.round(hCm / 30.48 * 100) / 100 : hCm;
    } catch (e) { console.error('GetMyProfile failed:', e); }
  }

  resolvePhoto(url: string): string { return this.grpc.resolvePhotoUrl(url); }

  // ── Drag & Drop ──────────────────────────────────────
  onDragStart(index: number, event: DragEvent): void {
    this.dragIndex = index;
    if (event.dataTransfer) {
      event.dataTransfer.effectAllowed = 'move';
      event.dataTransfer.setData('text/plain', index.toString());
      // Make the drag image slightly transparent
      const el = event.target as HTMLElement;
      setTimeout(() => el.style.opacity = '0.3', 0);
    }
  }

  onDragEnd(): void {
    this.dragIndex = null;
    this.dragOverIndex = null;
  }

  onDragOver(index: number, event: DragEvent): void {
    event.preventDefault();
    if (event.dataTransfer) event.dataTransfer.dropEffect = 'move';
    if (this.dragIndex !== null && this.dragIndex !== index) {
      this.dragOverIndex = index;
    }
  }

  onDragLeave(): void {
    this.dragOverIndex = null;
  }

  async onDrop(targetIndex: number, event: DragEvent): Promise<void> {
    event.preventDefault();
    this.dragOverIndex = null;
    if (this.dragIndex === null || this.dragIndex === targetIndex) return;

    const urls = [...this.profile.photo_urls];
    const [moved] = urls.splice(this.dragIndex, 1);
    urls.splice(targetIndex, 0, moved);
    this.dragIndex = null;

    // Update UI immediately for responsiveness
    this.profile.photo_urls = urls;

    // Persist to backend
    try { await this.grpc.reorderPhotos(urls); }
    catch (e) { console.error('Reorder failed:', e); }
  }

  onWeightUnitChange(): void {
    if (this.weightUnit === 'lb') {
      this.weightDisplay = Math.round(this.weightDisplay * 2.20462 * 10) / 10;
    } else {
      this.weightDisplay = Math.round(this.weightDisplay / 2.20462 * 10) / 10;
    }
  }

  onHeightUnitChange(): void {
    if (this.heightUnit === 'ft') {
      this.heightDisplay = Math.round(this.heightDisplay / 30.48 * 100) / 100;
    } else {
      this.heightDisplay = Math.round(this.heightDisplay * 30.48 * 10) / 10;
    }
  }

  async onFileSelected(event: Event): Promise<void> {
    const input = event.target as HTMLInputElement;
    if (!input.files?.length) return;
    this.uploading = true; this.uploadError = '';
    try {
      const result = await this.grpc.uploadPhoto(input.files[0]);
      this.profile.photo_urls = result.photo_urls;
    } catch (e: any) { this.uploadError = e.message || 'Upload failed'; }
    finally { this.uploading = false; input.value = ''; }
  }

  async deletePhoto(index: number, event: Event): Promise<void> {
    event.stopPropagation();
    if (this.profile.photo_urls.length <= 1) { this.uploadError = 'You need at least one photo.'; return; }
    try { const r = await this.grpc.deletePhoto(index); this.profile.photo_urls = r.photo_urls; }
    catch (e: any) { this.uploadError = e.message; }
  }

  addInterest(): void {
    if (this.newInterest.trim() && this.profile) {
      this.profile.interests = [...(this.profile.interests || []), this.newInterest.trim()];
      this.newInterest = '';
    }
  }

  removeInterest(index: number): void {
    if (this.profile?.interests) this.profile.interests = this.profile.interests.filter((_: any, i: number) => i !== index);
  }

  async saveProfile(): Promise<void> {
    this.saving = true;
    // Convert display values to storage units (kg, cm)
    const weightKg = this.weightUnit === 'lb' ? this.weightDisplay / 2.20462 : this.weightDisplay;
    const heightCm = this.heightUnit === 'ft' ? this.heightDisplay * 30.48 : this.heightDisplay;
    try {
      await this.grpc.updateProfile({
        bio: this.bio,
        interests: this.profile?.interests || [],
        weight: Math.round(weightKg * 10) / 10,
        weight_unit: this.weightUnit,
        height: Math.round(heightCm * 10) / 10,
        height_unit: this.heightUnit,
      });
    } catch (e) { console.error('UpdateProfile failed:', e); }
    finally { this.saving = false; }
  }

  logout(): void { this.grpc.logout(); this.router.navigate(['/login']); }
  onImgError(e: Event): void { (e.target as HTMLImageElement).src = 'https://via.placeholder.com/400x300?text=No+Photo'; }
}

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// SETTINGS PAGE (Discovery preferences)
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="settings-page">
      <h2>Discovery Settings</h2>

      <div class="setting-section">
        <div class="setting-label">
          <span>Max Distance</span>
          <span class="setting-value">{{ maxDistance }} km</span>
        </div>
        <input type="range" [(ngModel)]="maxDistance" min="5" max="200" class="slider" />
      </div>

      <div class="setting-section">
        <div class="setting-label">
          <span>Age Range</span>
          <span class="setting-value">{{ minAge }} – {{ maxAge }}</span>
        </div>
        <div class="dual-range">
          <label>Min</label>
          <input type="range" [(ngModel)]="minAge" min="18" [max]="maxAge" class="slider" />
          <label>Max</label>
          <input type="range" [(ngModel)]="maxAge" [min]="minAge" max="80" class="slider" />
        </div>
      </div>

      <div class="setting-section">
        <div class="setting-label">
          <span>Weight Range</span>
          <span class="setting-value">
            {{ minWeightDisplay || 'Any' }}{{ minWeightDisplay ? ' – ' : '' }}{{ maxWeightDisplay || '' }}{{ (minWeightDisplay || maxWeightDisplay) ? (' ' + weightPrefUnit) : '' }}
          </span>
        </div>
        <div class="unit-toggle">
          <button [class.active]="weightPrefUnit === 'kg'" (click)="weightPrefUnit = 'kg'; convertWeightPref()">kg</button>
          <button [class.active]="weightPrefUnit === 'lb'" (click)="weightPrefUnit = 'lb'; convertWeightPref()">lb</button>
        </div>
        <div class="dual-range">
          <label>Min</label>
          <input type="range" [(ngModel)]="minWeightDisplay" min="0" [max]="maxWeightDisplay || 200" class="slider" />
          <label>Max</label>
          <input type="range" [(ngModel)]="maxWeightDisplay" [min]="minWeightDisplay || 0" max="200" class="slider" />
        </div>
      </div>

      <div class="setting-section">
        <div class="setting-label">
          <span>Height Range</span>
          <span class="setting-value">
            {{ minHeightDisplay || 'Any' }}{{ minHeightDisplay ? ' – ' : '' }}{{ maxHeightDisplay || '' }}{{ (minHeightDisplay || maxHeightDisplay) ? (' ' + heightPrefUnit) : '' }}
          </span>
        </div>
        <div class="unit-toggle">
          <button [class.active]="heightPrefUnit === 'cm'" (click)="heightPrefUnit = 'cm'; convertHeightPref()">cm</button>
          <button [class.active]="heightPrefUnit === 'ft'" (click)="heightPrefUnit = 'ft'; convertHeightPref()">ft</button>
        </div>
        <div class="dual-range">
          <label>Min</label>
          <input type="range" [(ngModel)]="minHeightDisplay" min="0" [max]="maxHeightDisplay || (heightPrefUnit === 'cm' ? 220 : 7.5)" [step]="heightPrefUnit === 'ft' ? 0.1 : 1" class="slider" />
          <label>Max</label>
          <input type="range" [(ngModel)]="maxHeightDisplay" [min]="minHeightDisplay || 0" [max]="heightPrefUnit === 'cm' ? 220 : 7.5" [step]="heightPrefUnit === 'ft' ? 0.1 : 1" class="slider" />
        </div>
      </div>

      <button class="btn-save" (click)="saveSettings()">{{ saving ? 'Saving...' : 'Save Settings' }}</button>
    </div>
  `,
  styles: [`
    .settings-page { min-height:calc(100vh - 60px); background:#0f0c29; color:#fff; padding:24px 16px 100px; }
    .settings-page > h2 { font-size:1.5rem; font-weight:800; margin-bottom:24px; }
    .setting-section { padding:16px 0; border-bottom:1px solid rgba(255,255,255,0.06); }
    .setting-label { display:flex; justify-content:space-between; align-items:center; margin-bottom:12px; }
    .setting-label span:first-child { font-size:1rem; font-weight:600; }
    .setting-value { font-size:0.9rem; color:#EE5A24; font-weight:500; }
    .slider { width:100%; accent-color:#EE5A24; margin:4px 0; }
    .dual-range { display:flex; align-items:center; gap:8px; }
    .dual-range label { font-size:0.75rem; color:rgba(255,255,255,0.4); min-width:28px; }
    .dual-range .slider { flex:1; }
    .unit-toggle { display:flex; gap:0; margin-bottom:10px; background:rgba(255,255,255,0.05); border-radius:8px; overflow:hidden; width:fit-content; }
    .unit-toggle button { padding:6px 16px; border:none; background:transparent; color:rgba(255,255,255,0.4); font-size:0.85rem; font-weight:600; cursor:pointer; transition:all 0.2s; }
    .unit-toggle button.active { background:rgba(238,90,36,0.3); color:#EE5A24; }
    .btn-save { display:block; width:100%; margin-top:24px; padding:14px; background:linear-gradient(135deg,#FF6B6B,#EE5A24); border:none; border-radius:12px; color:#fff; font-weight:700; font-size:1rem; cursor:pointer; }
  `]
})
export class SettingsComponent implements OnInit {
  maxDistance = 50; minAge = 18; maxAge = 45;
  weightPrefUnit = 'kg'; heightPrefUnit = 'cm';
  minWeightDisplay = 0; maxWeightDisplay = 0;
  minHeightDisplay = 0; maxHeightDisplay = 0;
  saving = false;
  private profile: any = null;

  constructor(private grpc: GrpcClientService) {}

  async ngOnInit(): Promise<void> {
    try {
      this.profile = await this.grpc.getMyProfile();
      this.maxDistance = this.profile.max_distance_km || 50;
      this.minAge = this.profile.min_age_preference || 18;
      this.maxAge = this.profile.max_age_preference || 45;
      // Weight prefs (stored in kg)
      const minW = this.profile.min_weight_preference || 0;
      const maxW = this.profile.max_weight_preference || 0;
      this.minWeightDisplay = minW;
      this.maxWeightDisplay = maxW;
      // Height prefs (stored in cm)
      const minH = this.profile.min_height_preference || 0;
      const maxH = this.profile.max_height_preference || 0;
      this.minHeightDisplay = minH;
      this.maxHeightDisplay = maxH;
    } catch (e) { console.error('GetMyProfile failed:', e); }
  }

  convertWeightPref(): void {
    if (this.weightPrefUnit === 'lb') {
      this.minWeightDisplay = Math.round(this.minWeightDisplay * 2.20462);
      this.maxWeightDisplay = Math.round(this.maxWeightDisplay * 2.20462);
    } else {
      this.minWeightDisplay = Math.round(this.minWeightDisplay / 2.20462);
      this.maxWeightDisplay = Math.round(this.maxWeightDisplay / 2.20462);
    }
  }

  convertHeightPref(): void {
    if (this.heightPrefUnit === 'ft') {
      this.minHeightDisplay = Math.round(this.minHeightDisplay / 30.48 * 10) / 10;
      this.maxHeightDisplay = Math.round(this.maxHeightDisplay / 30.48 * 10) / 10;
    } else {
      this.minHeightDisplay = Math.round(this.minHeightDisplay * 30.48);
      this.maxHeightDisplay = Math.round(this.maxHeightDisplay * 30.48);
    }
  }

  async saveSettings(): Promise<void> {
    this.saving = true;
    // Convert to storage units (kg, cm)
    const minWKg = this.weightPrefUnit === 'lb' ? this.minWeightDisplay / 2.20462 : this.minWeightDisplay;
    const maxWKg = this.weightPrefUnit === 'lb' ? this.maxWeightDisplay / 2.20462 : this.maxWeightDisplay;
    const minHCm = this.heightPrefUnit === 'ft' ? this.minHeightDisplay * 30.48 : this.minHeightDisplay;
    const maxHCm = this.heightPrefUnit === 'ft' ? this.maxHeightDisplay * 30.48 : this.maxHeightDisplay;
    try {
      await this.grpc.updateProfile({
        max_distance_km: this.maxDistance,
        min_age_preference: this.minAge,
        max_age_preference: this.maxAge,
        min_weight_preference: Math.round(minWKg * 10) / 10,
        max_weight_preference: Math.round(maxWKg * 10) / 10,
        min_height_preference: Math.round(minHCm),
        max_height_preference: Math.round(maxHCm),
      });
    } catch (e) { console.error('UpdateProfile failed:', e); }
    finally { this.saving = false; }
  }
}

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// NAVBAR (4 tabs: Discover, Matches, Profile, Settings)
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <nav class="navbar" *ngIf="show">
      <a routerLink="/discover" routerLinkActive="active" class="nav-item">
        <svg width="22" height="22" viewBox="0 0 24 24" fill="currentColor"><path d="M15.5 14h-.79l-.28-.27A6.471 6.471 0 0016 9.5 6.5 6.5 0 109.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z"/></svg>
        <span>Discover</span>
      </a>
      <a routerLink="/matches" routerLinkActive="active" class="nav-item">
        <svg width="22" height="22" viewBox="0 0 24 24" fill="currentColor"><path d="M20 2H4c-1.1 0-2 .9-2 2v18l4-4h14c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zm0 14H6l-2 2V4h16v12z"/></svg>
        <span>Matches</span>
      </a>
      <a routerLink="/profile" routerLinkActive="active" class="nav-item">
        <svg width="22" height="22" viewBox="0 0 24 24" fill="currentColor"><path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/></svg>
        <span>Profile</span>
      </a>
      <a routerLink="/settings" routerLinkActive="active" class="nav-item">
        <svg width="22" height="22" viewBox="0 0 24 24" fill="currentColor"><path d="M19.14 12.94c.04-.3.06-.61.06-.94 0-.32-.02-.64-.07-.94l2.03-1.58a.49.49 0 00.12-.61l-1.92-3.32a.49.49 0 00-.59-.22l-2.39.96c-.5-.38-1.03-.7-1.62-.94l-.36-2.54a.484.484 0 00-.48-.41h-3.84c-.24 0-.43.17-.47.41l-.36 2.54c-.59.24-1.13.57-1.62.94l-2.39-.96a.49.49 0 00-.59.22L2.74 8.87c-.12.21-.08.47.12.61l2.03 1.58c-.05.3-.07.62-.07.94s.02.64.07.94l-2.03 1.58a.49.49 0 00-.12.61l1.92 3.32c.12.22.37.29.59.22l2.39-.96c.5.38 1.03.7 1.62.94l.36 2.54c.05.24.24.41.48.41h3.84c.24 0 .44-.17.47-.41l.36-2.54c.59-.24 1.13-.56 1.62-.94l2.39.96c.22.08.47 0 .59-.22l1.92-3.32c.12-.22.07-.47-.12-.61l-2.01-1.58zM12 15.6A3.6 3.6 0 1115.6 12 3.6 3.6 0 0112 15.6z"/></svg>
        <span>Settings</span>
      </a>
    </nav>
  `,
  styles: [`
    .navbar { position:fixed; bottom:0; left:0; right:0; display:flex; justify-content:space-around; background:rgba(15,12,41,0.95); backdrop-filter:blur(10px); border-top:1px solid rgba(255,255,255,0.06); padding:8px 0 env(safe-area-inset-bottom,8px); z-index:100; }
    .nav-item { display:flex; flex-direction:column; align-items:center; gap:3px; text-decoration:none; color:rgba(255,255,255,0.4); font-size:0.65rem; padding:6px 12px; transition:color 0.2s; }
    .nav-item.active { color:#EE5A24; }
  `]
})
export class NavbarComponent {
  constructor(private grpc: GrpcClientService) {}
  get show(): boolean { return this.grpc.isLoggedIn && !window.location.pathname.includes('/chat/'); }
}

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// ROOT
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterModule, NavbarComponent],
  template: `<router-outlet></router-outlet><app-navbar></app-navbar>`,
  styles: [`
    :host { display:block; max-width:480px; margin:0 auto; background:#0f0c29; min-height:100vh; }
  `]
})
export class AppComponent {}
