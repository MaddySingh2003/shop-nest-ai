import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('token');
  const router = inject(Router);

  let authReq = req;

  if (token) {
    authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

 
  return next(authReq).pipe(
    catchError((err: any) => {
      if (err.status === 401 || err.status === 403) {
        console.warn('JWT expired or invalid. Logging out...');
        localStorage.removeItem('token');
        router.navigate(['/login']);
      }

      return throwError(() => err);
    })
  );
};
