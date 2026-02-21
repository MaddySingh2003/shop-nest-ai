import { CommonModule } from '@angular/common';
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Navbar } from '../../components/navbar/navbar';
import { ProfileService } from '../../services/profile.service';

@Component({
  selector: 'app-account-layout',
  standalone: true,
  imports: [RouterModule, CommonModule, Navbar],
  templateUrl: './account-layout.html',
  styleUrl: './account-layout.css',
})
export class AccountLayout implements OnInit {

  user: any = null;
  loading = true;

  constructor(
    private profileService: ProfileService,
    private cdr: ChangeDetectorRef
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