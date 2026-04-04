import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { trigger, transition, style, animate } from '@angular/animations';
import { GrpcClientService, ProfileCard } from '../../services/grpc-client.service';
import { SwipeCardComponent } from '../swipe-card/swipe-card.component';
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
          [style.transform]="i===1?'scale(0.95)':''"
          [style.top]="i===1?'12px':'0'"
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
    .discover-page { display:flex; flex-direction:column; align-items:center; padding:24px 16px 100px; min-height:calc(100vh - 60px); background:linear-gradient(180deg,#0f0c29 0%,#1a1a2e 100%); max-width:480px; margin:0 auto; }
    .transport-indicator { display:flex; align-items:center; gap:6px; padding:4px 12px; background:rgba(0,176,255,0.1); border:1px solid rgba(0,176,255,0.2); border-radius:20px; font-size:0.7rem; color:#00b0ff; margin-bottom:16px; font-weight:500; }
    .dot { width:6px; height:6px; border-radius:50%; background:#00e676; animation:pulse 2s infinite; }
    @keyframes pulse { 0%,100% { opacity:1; } 50% { opacity:0.4; } }
    .card-stack { position:relative; width:100%; max-width:400px; height:580px; margin:0 auto; display:flex; justify-content:center; }
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
