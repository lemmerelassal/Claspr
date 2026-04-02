import { Routes, CanActivateFn } from '@angular/router';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthComponent, DiscoverComponent, MatchesComponent, ChatComponent, ProfileComponent } from './app.component';
import { GrpcClientService } from './services/grpc-client.service';

// Functional guard — no separate file needed
const authGuard: CanActivateFn = () => {
  const grpc = inject(GrpcClientService);
  const router = inject(Router);
  if (grpc.isLoggedIn) return true;
  router.navigate(['/login']);
  return false;
};

export const routes: Routes = [
  { path: '', redirectTo: '/discover', pathMatch: 'full' },
  { path: 'login', component: AuthComponent },
  { path: 'discover', component: DiscoverComponent, canActivate: [authGuard] },
  { path: 'matches', component: MatchesComponent, canActivate: [authGuard] },
  { path: 'chat/:matchId', component: ChatComponent, canActivate: [authGuard] },
  { path: 'profile', component: ProfileComponent, canActivate: [authGuard] },
  { path: '**', redirectTo: '/discover' }
];
