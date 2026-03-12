import { CommonModule } from '@angular/common';
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Navbar } from '../../components/navbar/navbar';
import { ProfileService } from '../../services/profile.service';
import { AuthService } from '../../services/auth.service';
import { Footer } from '../../components/footer/footer';

@Component({
  selector: 'app-account-layout',
  standalone: true,
  imports: [RouterModule, CommonModule, Navbar,Footer],
  templateUrl: './account-layout.html',
  styleUrl: './account-layout.css',
})
export class AccountLayout implements OnInit {

  user: any = null;
  loading = true;

  constructor(
    private profileService: ProfileService,
    private cdr: ChangeDetectorRef,
    public auth:AuthService
  ) {}

  ngOnInit() {
    this.loadProfile();
  }

  loadProfile() {
    this.profileService.getProfile().subscribe(res => {
      this.user = res;
      this.loading = false;
      this.cdr.detectChanges();
    });
  }

}