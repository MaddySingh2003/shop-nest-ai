import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'app-login',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.html'
})
export class LoginComponent {

  form!: FormGroup;
  error = '';

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router
  ) {
    // ✅ FIX IS HERE
    this.form = this.fb.group({
      email: [''],
      password: ['']
    });
  }

  submit() {
    this.error = '';

    this.auth.login(this.form.value as any).subscribe({
      next: () => {
        this.router.navigateByUrl('/home');
      },
      error: () => {
        this.error = 'Invalid email or password';
      }
    });
  }
}
