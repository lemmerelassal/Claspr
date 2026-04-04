import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { GrpcClientService } from '../../services/grpc-client.service';
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
    .profile-page { min-height:100vh; background:#0f0c29; color:#fff; padding-bottom:100px; max-width:480px; margin:0 auto; }
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
