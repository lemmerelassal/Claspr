import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NavbarComponent } from './components/navbar/navbar.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterModule, NavbarComponent],
  template: `<router-outlet></router-outlet><app-navbar></app-navbar>`,
  styles: [`
    :host { display:block; background:#0f0c29; min-height:100vh; }
  `]
})
export class AppComponent {}
