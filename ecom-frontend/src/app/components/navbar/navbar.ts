import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-navbar',
  imports: [CommonModule,RouterModule],
  standalone:true,
  templateUrl: './navbar.html',
 
})
export class Navbar {
  constructor(
    public auth:AuthService,
    private router:Router,
    
  ){}

  logout(){
    this.auth.logout();
    this.router.navigate(['./login']);
  }

}
