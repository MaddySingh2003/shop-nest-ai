import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

@Injectable({ providedIn: 'root' })
export class AuthService {

  constructor(@Inject(PLATFORM_ID) private platformId: Object) {}

  // --------------------
  // AUTH CHECK
  // --------------------
  isLoggedIn(): boolean {
    if (isPlatformBrowser(this.platformId)) {
      return !!localStorage.getItem('token');
    }
    return false;
  }

  // --------------------
  // ROLE CHECK
  // --------------------
  isAdmin(): boolean {
    if (isPlatformBrowser(this.platformId)) {
      return localStorage.getItem('role') === 'ADMIN';
    }
    return false;
  }

  // --------------------
  // TOKEN HELPERS
  // --------------------
  setSession(token: string, role: string) {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.setItem('token', token);
      localStorage.setItem('role', role);
    }
  }

  logout() {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem('token');
      localStorage.removeItem('role');
    }
  }
}
