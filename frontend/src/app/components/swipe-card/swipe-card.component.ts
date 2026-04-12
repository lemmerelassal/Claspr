import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GrpcClientService, ProfileCard } from '../../services/grpc-client.service';
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
    :host {
      position: absolute; top: 0; left: 0; right: 0; bottom: 0;
      display: flex; justify-content: center;
      pointer-events: none;
    }
    .swipe-card {
      width: calc(100% - 32px); max-width: 400px; height: 580px;
      border-radius: 16px; overflow: hidden; background: #1a1a2e;
      box-shadow: 0 8px 40px rgba(0,0,0,0.4); cursor: grab; user-select: none;
      pointer-events: all;
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
