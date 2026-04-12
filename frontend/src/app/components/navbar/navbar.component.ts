import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { GrpcClientService } from '../../services/grpc-client.service';

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
        <div class="icon-wrap">
          <svg width="22" height="22" viewBox="0 0 24 24" fill="currentColor"><path d="M20 2H4c-1.1 0-2 .9-2 2v18l4-4h14c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zm0 14H6l-2 2V4h16v12z"/></svg>
          <span class="nav-badge" *ngIf="unreadCount > 0">{{ unreadCount > 99 ? '99+' : unreadCount }}</span>
        </div>
        <span>Matches</span>
      </a>
      <a routerLink="/history" routerLinkActive="active" class="nav-item">
        <svg width="22" height="22" viewBox="0 0 24 24" fill="currentColor"><path d="M13 3a9 9 0 00-9 9H1l3.89 3.89.07.14L9 12H6c0-3.87 3.13-7 7-7s7 3.13 7 7-3.13 7-7 7c-1.93 0-3.68-.79-4.94-2.06l-1.42 1.42A8.954 8.954 0 0013 21a9 9 0 000-18zm-1 5v5l4.28 2.54.72-1.21-3.5-2.08V8H12z"/></svg>
        <span>History</span>
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
    .icon-wrap { position:relative; display:inline-flex; }
    .nav-badge {
      position:absolute; top:-6px; right:-10px;
      min-width:16px; height:16px; padding:0 4px;
      background:linear-gradient(135deg, #FF6B6B, #EE5A24);
      border-radius:8px; border:2px solid rgba(15,12,41,0.95);
      font-size:0.55rem; font-weight:700; color:#fff;
      display:flex; align-items:center; justify-content:center;
      line-height:1;
    }
  `]
})
export class NavbarComponent implements OnInit, OnDestroy {
  unreadCount = 0;
  private pollTimer: any;

  constructor(private grpc: GrpcClientService) {}

  get show(): boolean { return this.grpc.isLoggedIn && !window.location.pathname.includes('/chat/'); }

  ngOnInit(): void {
    if (this.grpc.isLoggedIn) {
      this.fetchUnread();
      this.pollTimer = setInterval(() => this.fetchUnread(), 15000);
    }
  }

  ngOnDestroy(): void {
    if (this.pollTimer) clearInterval(this.pollTimer);
  }

  private async fetchUnread(): Promise<void> {
    if (!this.grpc.isLoggedIn) return;
    try {
      const res = await this.grpc.getMatches();
      this.unreadCount = (res.matches || []).reduce((sum, m) => sum + (m.unread_count || 0), 0);
    } catch (e) { /* silent */ }
  }
}
