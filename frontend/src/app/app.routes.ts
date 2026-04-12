import { Routes, CanActivateFn } from '@angular/router';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { GrpcClientService } from './services/grpc-client.service';
import { AuthComponent } from './components/auth/auth.component';
import { DiscoverComponent } from './components/discover/discover.component';
import { MatchesComponent } from './components/matches/matches.component';
import { ChatComponent } from './components/chat/chat.component';
import { ProfileComponent } from './components/profile/profile.component';
import { SettingsComponent } from './components/settings/settings.component';
import { HistoryComponent } from './components/history/history.component';

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
  { path: 'profile/:userId', component: ProfileComponent, canActivate: [authGuard] },
  { path: 'settings', component: SettingsComponent, canActivate: [authGuard] },
  { path: 'history', component: HistoryComponent, canActivate: [authGuard] },
  { path: '**', redirectTo: '/discover' }
];
