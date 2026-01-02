import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login';
import { authGuard } from './core/guards/auth.guards';
import { HomeComponent } from './pages/home/home';

export const routes: Routes = [
   { path: 'login', component: LoginComponent },

  {
    path: 'home',
    component: HomeComponent,
    canActivate: [authGuard]
  },

  
  { path: '**', redirectTo: 'login' },
  {path:"",redirectTo:'login',pathMatch:'full'}
];
