import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { GrpcClientService, SwipeHistoryEntry } from '../../services/grpc-client.service';

@Component({
  selector: 'app-history',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  template: `
    <div class="history-page">
      <h2>Swipe History</h2>

      <!-- Filters -->
      <div class="filters">
        <!-- Name autocomplete -->
        <div class="filter-group">
          <label>Search by name</label>
          <div class="autocomplete-wrap">
            <input type="text" [(ngModel)]="nameFilter" placeholder="Start typing a name..."
                   (input)="onNameInput()" (focus)="showSuggestions = true" />
            <div class="suggestions" *ngIf="showSuggestions && suggestions.length > 0">
              <div class="suggestion" *ngFor="let name of suggestions"
                   (mousedown)="selectName(name)">{{ name }}</div>
            </div>
          </div>
        </div>

        <!-- Direction radio -->
        <div class="filter-group">
          <label>Direction</label>
          <div class="radio-row">
            <button [class.active]="directionFilter === 'ALL'" (click)="directionFilter = 'ALL'; search()">All</button>
            <button [class.active]="directionFilter === 'RIGHT'" (click)="directionFilter = 'RIGHT'; search()" class="right-btn">Liked</button>
            <button [class.active]="directionFilter === 'LEFT'" (click)="directionFilter = 'LEFT'; search()" class="left-btn">Passed</button>
          </div>
        </div>

        <!-- Interest filter -->
        <div class="filter-group">
          <label>Interest</label>
          <input type="text" [(ngModel)]="interestFilter" placeholder="e.g. Photography"
                 (input)="search()" />
        </div>
      </div>

      <!-- Results -->
      <div class="results-count" *ngIf="!loading">{{ swipes.length }} result{{ swipes.length !== 1 ? 's' : '' }}</div>

      <div class="swipe-list">
        <a class="swipe-entry" *ngFor="let s of swipes"
             [routerLink]="['/profile', s.profile.user_id]"
             [class.liked]="s.direction !== 'LEFT'" [class.passed]="s.direction === 'LEFT'">
          <img [src]="resolvePhoto(s.profile.photo_urls[0] || '')" class="entry-photo"
               (error)="onImgError($event)" />
          <div class="entry-info">
            <div class="entry-name">
              {{ s.profile.display_name }}, {{ s.profile.age }}
              <span class="entry-city" *ngIf="s.profile.location_city">· {{ s.profile.location_city }}</span>
            </div>
            <div class="entry-interests" *ngIf="s.profile.interests?.length">
              <span class="mini-tag" *ngFor="let i of s.profile.interests.slice(0, 3)">{{ i }}</span>
              <span class="more-tag" *ngIf="s.profile.interests.length > 3">+{{ s.profile.interests.length - 3 }}</span>
            </div>
            <div class="entry-time">{{ formatTime(s.swiped_at) }}</div>
          </div>
          <div class="entry-direction">
            <div class="dir-badge" [class.liked]="s.direction !== 'LEFT'" [class.passed]="s.direction === 'LEFT'">
              {{ s.direction === 'LEFT' ? 'PASS' : s.direction === 'SUPER_LIKE' ? 'SUPER' : 'LIKE' }}
            </div>
          </div>
        </a>
      </div>

      <div class="empty-state" *ngIf="swipes.length === 0 && !loading">
        <p>No swipes yet. Start discovering!</p>
      </div>

      <div class="loading-state" *ngIf="loading">
        <div class="spinner"></div>
      </div>
    </div>
  `,
  styles: [`
    .history-page { min-height:calc(100vh - 60px); background:#0f0c29; color:#fff; padding:24px 16px 100px; max-width:480px; margin:0 auto; }
    .history-page > h2 { font-size:1.5rem; font-weight:800; margin-bottom:20px; }

    .filters { display:flex; flex-direction:column; gap:16px; margin-bottom:20px; padding-bottom:16px; border-bottom:1px solid rgba(255,255,255,0.06); }
    .filter-group label { display:block; font-size:0.75rem; text-transform:uppercase; letter-spacing:1px; color:rgba(255,255,255,0.4); margin-bottom:6px; }
    .filter-group input {
      width:100%; padding:10px 14px; background:rgba(255,255,255,0.06);
      border:1px solid rgba(255,255,255,0.1); border-radius:10px;
      color:#fff; font-size:0.9rem; outline:none; box-sizing:border-box;
    }
    .filter-group input::placeholder { color:rgba(255,255,255,0.3); }
    .filter-group input:focus { border-color:#EE5A24; }

    .autocomplete-wrap { position:relative; }
    .suggestions {
      position:absolute; top:100%; left:0; right:0; z-index:20;
      background:#1a1a2e; border:1px solid rgba(255,255,255,0.1);
      border-radius:0 0 10px 10px; max-height:200px; overflow-y:auto;
    }
    .suggestion {
      padding:10px 14px; cursor:pointer; font-size:0.9rem;
      transition:background 0.15s;
    }
    .suggestion:hover { background:rgba(238,90,36,0.15); }

    .radio-row { display:flex; gap:0; border-radius:10px; overflow:hidden; border:1px solid rgba(255,255,255,0.1); }
    .radio-row button {
      flex:1; padding:10px; border:none; background:rgba(255,255,255,0.04);
      color:rgba(255,255,255,0.5); font-size:0.85rem; font-weight:600;
      cursor:pointer; transition:all 0.2s;
    }
    .radio-row button.active { background:rgba(238,90,36,0.2); color:#EE5A24; }
    .radio-row button.right-btn.active { background:rgba(0,230,118,0.15); color:#00e676; }
    .radio-row button.left-btn.active { background:rgba(255,82,82,0.15); color:#ff5252; }

    .results-count { font-size:0.8rem; color:rgba(255,255,255,0.35); margin-bottom:12px; }

    .swipe-list { display:flex; flex-direction:column; gap:2px; }
    .swipe-entry {
      display:flex; align-items:center; gap:12px; padding:12px 0;
      border-bottom:1px solid rgba(255,255,255,0.04);
      text-decoration:none; color:inherit; cursor:pointer;
    }
    .entry-photo { width:52px; height:52px; border-radius:50%; object-fit:cover; flex-shrink:0; }
    .swipe-entry.liked .entry-photo { border:2px solid rgba(0,230,118,0.4); }
    .swipe-entry.passed .entry-photo { border:2px solid rgba(255,82,82,0.3); opacity:0.7; }
    .entry-info { flex:1; min-width:0; }
    .entry-name { font-weight:600; font-size:0.95rem; white-space:nowrap; overflow:hidden; text-overflow:ellipsis; }
    .entry-city { font-weight:400; color:rgba(255,255,255,0.5); font-size:0.85rem; }
    .entry-interests { display:flex; gap:4px; margin-top:4px; flex-wrap:wrap; }
    .mini-tag { padding:2px 8px; background:rgba(255,255,255,0.08); border-radius:10px; font-size:0.7rem; color:rgba(255,255,255,0.6); }
    .more-tag { font-size:0.7rem; color:rgba(255,255,255,0.35); padding:2px 4px; }
    .entry-time { font-size:0.7rem; color:rgba(255,255,255,0.25); margin-top:2px; }
    .entry-direction { flex-shrink:0; }
    .dir-badge {
      padding:4px 10px; border-radius:6px; font-size:0.65rem;
      font-weight:700; letter-spacing:0.5px;
    }
    .dir-badge.liked { background:rgba(0,230,118,0.15); color:#00e676; }
    .dir-badge.passed { background:rgba(255,82,82,0.1); color:#ff5252; }

    .empty-state { text-align:center; color:rgba(255,255,255,0.4); margin-top:60px; }
    .loading-state { text-align:center; margin-top:40px; }
    .spinner { width:30px; height:30px; border:3px solid rgba(255,255,255,0.1); border-top-color:#EE5A24; border-radius:50%; animation:spin 0.8s linear infinite; margin:0 auto; }
    @keyframes spin { to { transform:rotate(360deg); } }
  `]
})
export class HistoryComponent implements OnInit {
  swipes: SwipeHistoryEntry[] = [];
  loading = false;
  nameFilter = '';
  directionFilter = 'ALL';
  interestFilter = '';
  suggestions: string[] = [];
  showSuggestions = false;
  private debounceTimer: any;

  constructor(private grpc: GrpcClientService) {}

  ngOnInit(): void { this.search(); }

  async search(): Promise<void> {
    this.loading = true;
    this.showSuggestions = false;
    try {
      const res = await this.grpc.getSwipeHistory(this.directionFilter, this.nameFilter, this.interestFilter);
      this.swipes = res.swipes || [];
    } catch (e) { console.error('GetHistory failed:', e); }
    finally { this.loading = false; }
  }

  onNameInput(): void {
    clearTimeout(this.debounceTimer);
    this.debounceTimer = setTimeout(async () => {
      if (this.nameFilter.length >= 1) {
        try {
          const res = await this.grpc.autocompleteName(this.nameFilter);
          this.suggestions = res.names || [];
          this.showSuggestions = true;
        } catch (e) { this.suggestions = []; }
      } else {
        this.suggestions = [];
        this.search();
      }
    }, 200);
  }

  selectName(name: string): void {
    this.nameFilter = name;
    this.showSuggestions = false;
    this.suggestions = [];
    this.search();
  }

  resolvePhoto(url: string): string { return this.grpc.resolvePhotoUrl(url); }
  onImgError(e: Event): void { (e.target as HTMLImageElement).src = 'https://via.placeholder.com/52?text=?'; }

  formatTime(iso: string): string {
    const d = new Date(iso);
    const diff = Date.now() - d.getTime();
    if (diff < 3600000) return Math.floor(diff / 60000) + 'm ago';
    if (diff < 86400000) return Math.floor(diff / 3600000) + 'h ago';
    return d.toLocaleDateString(undefined, { month: 'short', day: 'numeric' });
  }
}
