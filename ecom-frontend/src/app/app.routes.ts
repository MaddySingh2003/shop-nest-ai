import { Routes } from '@angular/router';
import { LoginComponent } from './modules/auth/login/login';
import { RegisterComponent } from './modules/auth/register/register';
import { authGuard } from './core/guards/auth.guard';
import { adminGuard } from './core/guards/admin.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },

  {
    path: 'admin',
    canActivate: [adminGuard],
    children: []   // placeholder
  },

  {
    path: '',
    canActivate: [authGuard],
    children: []   // placeholder
  },

  { path: '**', redirectTo: 'login' }
];
