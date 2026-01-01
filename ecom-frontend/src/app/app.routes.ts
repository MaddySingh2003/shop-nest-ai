import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login';

export const routes: Routes = [
    { path: 'login', component: LoginComponent },
  { path: 'home', loadComponent: () => import('./home').then(m => m.HomeComponent) },
  { path: '**', redirectTo: 'login' }
];
