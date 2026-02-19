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

  // LOGIN
  login(email: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.API}/login`, {
      email,
      password
    });
  }

  // REGISTER
  register(name: string, email: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.API}/register`, {
      name,
      email,
      password
    });
  }

  // SAVE TOKEN
  loginSuccess(token: string) {
    localStorage.setItem('token', token);
    this.router.navigate(['/home']);
  }

  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }

  isLoggedIn(): boolean {
    if (typeof window === 'undefined') {
      return false;
    }
    return !!localStorage.getItem('token');
  }
}
