import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private API = 'http://localhost:8080/auth';

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  // ✅ LOGIN API (email + password)
  login(email: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.API}/login`, {
      email,
      password
    });
  }

  // ✅ SAVE TOKEN & REDIRECT
  loginSuccess(token: string) {
    localStorage.setItem('token', token);
    this.router.navigate(['/home']);
  }

  // ✅ LOGOUT
  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }

  // ✅ AUTH CHECK
  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }
}
