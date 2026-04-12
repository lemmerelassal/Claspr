import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { GrpcClientService, MatchEntry } from '../../services/grpc-client.service';

@Component({
  selector: 'app-matches',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="matches-page">
      <div class="page-header">
        <h2>Messages
          <span class="total-unread" *ngIf="totalUnread > 0">{{ totalUnread }}</span>
        </h2>
      </div>
      <div class="new-matches" *ngIf="newMatches.length > 0">
        <h3>New Matches</h3>
        <div class="match-circles">
          <a *ngFor="let m of newMatches" [routerLink]="['/chat', m.match_id]" class="match-circle">
            <div class="avatar-wrap">
              <img [src]="resolvePhoto(m.profile.photo_urls[0])" [alt]="m.profile.display_name" (error)="onImgError($event)" />
              <span class="unread-dot" *ngIf="m.unread_count > 0"></span>
            </div>
            <span>{{ m.profile.display_name }}</span>
          </a>
        </div>
      </div>
      <div class="conversations">
        <a *ngFor="let m of conversations" [routerLink]="['/chat', m.match_id]" class="convo-row"
           [class.has-unread]="m.unread_count > 0">
          <div class="avatar-wrap">
            <img [src]="resolvePhoto(m.profile.photo_urls[0])" class="convo-avatar" (error)="onImgError($event)" />
            <span class="unread-badge" *ngIf="m.unread_count > 0">{{ m.unread_count > 99 ? '99+' : m.unread_count }}</span>
          </div>
          <div class="convo-info">
            <div class="convo-name">
              {{ m.profile.display_name }}
              <span class="unread-indicator" *ngIf="m.unread_count > 0"></span>
            </div>
            <div class="convo-last" [class.unread-text]="m.unread_count > 0" *ngIf="m.last_message">
              {{ m.last_message.sender_id === myId ? 'You: ' : '' }}{{ m.last_message.content }}
            </div>
            <div class="convo-last" *ngIf="!m.last_message">Matched! Say hello 👋</div>
          </div>
          <div class="convo-right">
            <div class="convo-time" *ngIf="m.last_message">{{ formatTime(m.last_message.sent_at) }}</div>
          </div>
        </a>
      </div>
      <div class="empty-state" *ngIf="matches.length === 0 && !loading"><p>No matches yet. Keep swiping! 💫</p></div>
    </div>
  `,
  styles: [`
    .matches-page { min-height:calc(100vh - 60px); background:#0f0c29; padding:24px 16px 80px; color:#fff; max-width:480px; margin:0 auto; }
    .page-header { display:flex; justify-content:space-between; align-items:center; margin-bottom:20px; }
    .page-header h2 { font-size:1.5rem; font-weight:800; display:flex; align-items:center; gap:8px; }
    .total-unread {
      display:inline-flex; align-items:center; justify-content:center;
      min-width:22px; height:22px; padding:0 6px;
      background:linear-gradient(135deg, #FF6B6B, #EE5A24);
      border-radius:11px; font-size:0.7rem; font-weight:700;
    }

    .new-matches h3 { font-size:0.85rem; text-transform:uppercase; letter-spacing:1px; color:rgba(255,255,255,0.5); margin-bottom:12px; }
    .match-circles { display:flex; gap:16px; overflow-x:auto; padding-bottom:16px; margin-bottom:20px; border-bottom:1px solid rgba(255,255,255,0.08); }
    .match-circle { display:flex; flex-direction:column; align-items:center; gap:6px; text-decoration:none; flex-shrink:0; }
    .match-circle .avatar-wrap img { width:64px; height:64px; border-radius:50%; object-fit:cover; border:2px solid #EE5A24; }
    .match-circle span { font-size:0.75rem; color:rgba(255,255,255,0.7); }

    .avatar-wrap { position:relative; flex-shrink:0; }
    .unread-dot {
      position:absolute; top:2px; right:2px;
      width:12px; height:12px; border-radius:50%;
      background:linear-gradient(135deg, #FF6B6B, #EE5A24);
      border:2px solid #0f0c29;
    }
    .unread-badge {
      position:absolute; top:-4px; right:-4px;
      min-width:20px; height:20px; padding:0 5px;
      background:linear-gradient(135deg, #FF6B6B, #EE5A24);
      border-radius:10px; border:2px solid #0f0c29;
      font-size:0.65rem; font-weight:700; color:#fff;
      display:flex; align-items:center; justify-content:center;
    }

    .convo-row {
      display:flex; align-items:center; gap:14px; padding:14px 0;
      border-bottom:1px solid rgba(255,255,255,0.06);
      text-decoration:none; color:inherit; cursor:pointer; transition:background 0.15s;
    }
    .convo-row:hover { background:rgba(255,255,255,0.02); }
    .convo-row.has-unread { background:rgba(238,90,36,0.04); }
    .convo-avatar { width:56px; height:56px; border-radius:50%; object-fit:cover; }
    .convo-info { flex:1; min-width:0; }
    .convo-name { font-weight:600; display:flex; align-items:center; gap:6px; }
    .unread-indicator {
      width:8px; height:8px; border-radius:50%;
      background:linear-gradient(135deg, #FF6B6B, #EE5A24);
    }
    .convo-last { font-size:0.85rem; color:rgba(255,255,255,0.5); white-space:nowrap; overflow:hidden; text-overflow:ellipsis; margin-top:2px; }
    .convo-last.unread-text { color:rgba(255,255,255,0.85); font-weight:500; }
    .convo-right { display:flex; flex-direction:column; align-items:flex-end; gap:4px; flex-shrink:0; }
    .convo-time { font-size:0.75rem; color:rgba(255,255,255,0.35); }
    .empty-state { text-align:center; color:rgba(255,255,255,0.4); margin-top:80px; }
  `]
})
export class MatchesComponent implements OnInit {
  matches: MatchEntry[] = [];
  loading = false;
  myId: string | null = null;

  get newMatches() { return this.matches.filter(m => !m.last_message); }
  get conversations() { return this.matches.filter(m => m.last_message); }
  get totalUnread(): number {
    return this.matches.reduce((sum, m) => sum + (m.unread_count || 0), 0);
  }

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
    return d.toLocaleDateString(undefined, { month: 'short', day: 'numeric' });
  }
}
