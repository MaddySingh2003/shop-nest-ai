import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class LoginComponent {

  email: string = '';
  password: string = '';
  error: string = '';
  loading = false;

  constructor(
    private auth: AuthService,
    private router: Router
  ) {}

  submit() {
    this.loading = true;
    this.error = '';

    this.auth.login(this.email, this.password).subscribe({
      next: (res: any) => {
        this.auth.loginSuccess(res.token);
      },
      error: () => {
        this.error = 'Invalid email or password';
        this.loading = false;
      }
    });
  }
}
