import { Component, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class LoginComponent {

  email = '';
  password = '';

  error: string | null = null;
  loading = false;

  constructor(
    private auth: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  submit(event?: Event) {

    if(event){
      event.preventDefault();
    }

    this.error = null;

    if(!this.email || !this.password){
      this.error = "Email and password required";
      return;
    }

    this.loading = true;

    this.auth.login(this.email, this.password).subscribe({

      next: (res: any) => {

        this.loading = false;

        this.auth.loginSuccess(res.token, res.role);

        this.cdr.detectChanges();

      },

      error: (err) => {

        this.loading = false;

        this.error =
          err?.error ||
          "Invalid email or password";

        this.cdr.detectChanges();

      }

    });

  }

  registerR(){
    this.router.navigate(['/register']);
  }

}