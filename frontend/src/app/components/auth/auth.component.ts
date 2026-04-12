import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { GrpcClientService } from '../../services/grpc-client.service';
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
            <option value="MTF_TRANS">Trans Woman (MTF)</option>
            <option value="FTM_TRANS">Trans Man (FTM)</option>
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
