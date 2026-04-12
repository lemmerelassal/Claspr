import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { GrpcClientService } from '../../services/grpc-client.service';

@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="settings-page">
      <h2>Discovery Settings</h2>

      <!-- Show Me -->
      <div class="setting-section">
        <h3>Show Me</h3>
        <div class="toggle-row" (click)="showMen = !showMen">
          <span>Men</span>
          <div class="toggle" [class.active]="showMen">
            <div class="toggle-knob"></div>
          </div>
        </div>
        <div class="toggle-row" (click)="showWomen = !showWomen">
          <span>Women</span>
          <div class="toggle" [class.active]="showWomen">
            <div class="toggle-knob"></div>
          </div>
        </div>
        <div class="toggle-row" (click)="showMtfTrans = !showMtfTrans">
          <span>Trans women (MTF)</span>
          <div class="toggle" [class.active]="showMtfTrans">
            <div class="toggle-knob"></div>
          </div>
        </div>
        <div class="toggle-row" (click)="showFtmTrans = !showFtmTrans">
          <span>Trans men (FTM)</span>
          <div class="toggle" [class.active]="showFtmTrans">
            <div class="toggle-knob"></div>
          </div>
        </div>
        <div class="toggle-row" (click)="showNonBinary = !showNonBinary">
          <span>Non-binary</span>
          <div class="toggle" [class.active]="showNonBinary">
            <div class="toggle-knob"></div>
          </div>
        </div>
      </div>

      <!-- Max Distance -->
      <div class="setting-section">
        <div class="setting-label">
          <span>Max Distance</span>
          <span class="setting-value">{{ maxDistance }} km</span>
        </div>
        <input type="range" [(ngModel)]="maxDistance" min="5" max="200" class="slider" />
      </div>

      <!-- Age Range -->
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

      <!-- Weight Range -->
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

      <!-- Height Range -->
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
    .settings-page { min-height:calc(100vh - 60px); background:#0f0c29; color:#fff; padding:24px 16px 100px; max-width:480px; margin:0 auto; }
    .settings-page > h2 { font-size:1.5rem; font-weight:800; margin-bottom:24px; }
    .setting-section { padding:16px 0; border-bottom:1px solid rgba(255,255,255,0.06); }
    .setting-section h3 { font-size:0.85rem; text-transform:uppercase; letter-spacing:1px; color:rgba(255,255,255,0.5); margin-bottom:12px; }
    .setting-label { display:flex; justify-content:space-between; align-items:center; margin-bottom:12px; }
    .setting-label span:first-child { font-size:1rem; font-weight:600; }
    .setting-value { font-size:0.9rem; color:#EE5A24; font-weight:500; }

    .toggle-row {
      display:flex; justify-content:space-between; align-items:center;
      padding:12px 0; cursor:pointer; user-select:none;
    }
    .toggle-row span { font-size:1rem; font-weight:500; }
    .toggle {
      width:48px; height:28px; border-radius:14px;
      background:rgba(255,255,255,0.15); position:relative;
      transition:background 0.25s;
    }
    .toggle.active { background:#EE5A24; }
    .toggle-knob {
      width:22px; height:22px; border-radius:50%;
      background:#fff; position:absolute; top:3px; left:3px;
      transition:transform 0.25s;
      box-shadow:0 1px 3px rgba(0,0,0,0.3);
    }
    .toggle.active .toggle-knob { transform:translateX(20px); }

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
  showMen = true;
  showWomen = true;
  showMtfTrans = true;
  showFtmTrans = true;
  showNonBinary = true;
  maxDistance = 50; minAge = 18; maxAge = 45;
  weightPrefUnit = 'kg'; heightPrefUnit = 'cm';
  minWeightDisplay = 0; maxWeightDisplay = 0;
  minHeightDisplay = 0; maxHeightDisplay = 0;
  saving = false;

  constructor(private grpc: GrpcClientService) {}

  async ngOnInit(): Promise<void> {
    try {
      const profile = await this.grpc.getMyProfile();
      this.showMen = profile.show_men ?? true;
      this.showWomen = profile.show_women ?? true;
      this.showMtfTrans = profile.show_mtf_trans ?? true;
      this.showFtmTrans = profile.show_ftm_trans ?? true;
      this.showNonBinary = profile.show_non_binary ?? true;
      this.maxDistance = profile.max_distance_km || 50;
      this.minAge = profile.min_age_preference || 18;
      this.maxAge = profile.max_age_preference || 45;
      this.minWeightDisplay = profile.min_weight_preference || 0;
      this.maxWeightDisplay = profile.max_weight_preference || 0;
      this.minHeightDisplay = profile.min_height_preference || 0;
      this.maxHeightDisplay = profile.max_height_preference || 0;
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
    const minWKg = this.weightPrefUnit === 'lb' ? this.minWeightDisplay / 2.20462 : this.minWeightDisplay;
    const maxWKg = this.weightPrefUnit === 'lb' ? this.maxWeightDisplay / 2.20462 : this.maxWeightDisplay;
    const minHCm = this.heightPrefUnit === 'ft' ? this.minHeightDisplay * 30.48 : this.minHeightDisplay;
    const maxHCm = this.heightPrefUnit === 'ft' ? this.maxHeightDisplay * 30.48 : this.maxHeightDisplay;
    try {
      await this.grpc.updateProfile({
        show_men: this.showMen,
        show_women: this.showWomen,
        show_mtf_trans: this.showMtfTrans,
        show_ftm_trans: this.showFtmTrans,
        show_non_binary: this.showNonBinary,
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
