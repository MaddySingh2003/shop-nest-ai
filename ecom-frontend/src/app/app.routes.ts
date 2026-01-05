import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login';
import { authGuard } from './core/guards/auth.guards';
import { HomeComponent } from './pages/home/home';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () =>
      import('./pages/login/login')
        .then(m => m.LoginComponent)
  },

  {
    path: 'home',
    // canActivate: [authGuard],
    loadComponent: () =>
      import('./pages/home/home')
        .then(m => m.HomeComponent)
  },

  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full' as const
  },

  {
    path: '**',
    redirectTo: 'home'
  }
];
