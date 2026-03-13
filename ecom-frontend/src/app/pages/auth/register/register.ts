import { Component, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './register.html'
})
export class RegisterComponent {

  name = '';
  email = '';
  password = '';
  confirmPassword = '';

  error: string | null = null;
  success: boolean = false;
  loading = false;

  constructor(
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  register(event?: Event) {

    if(event){
      event.preventDefault();
    }

    this.error = null;

    if (!this.name || !this.email || !this.password) {
      this.error = "All fields required";
      return;
    }

    if (!this.email.includes('@')) {
      this.error = "Invalid email";
      return;
    }

    if (this.password !== this.confirmPassword) {
      this.error = "Passwords do not match";
      return;
    }

    this.loading = true;

    this.authService.register(this.name, this.email, this.password)
      .subscribe({

        next: (res:any) => {

          this.loading = false;

          // show success UI
          this.success = true;

          // force UI refresh
          this.cdr.detectChanges();

        },

        error: (err) => {

          this.loading = false;

          this.error = err?.error || "Registration failed";

          this.cdr.detectChanges();
        }

      });
  }

  goLogin(){
    this.router.navigate(['/login']);
  }
}