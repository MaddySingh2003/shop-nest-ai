import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.html'
})
export class LoginComponent {

  form;
  error: string | null = null;

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      email: [''],
      password: ['']
    });
  }

  submit() {
    if (this.form.invalid) {
      this.error = 'Please fill all fields';
      return;
    }

    const { email, password } = this.form.value;

    console.log('Login submit:', email, password);

    // TEMP — backend integration next
    this.error = null;
  }
}
