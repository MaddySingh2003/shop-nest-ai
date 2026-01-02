import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { Product } from '../../core/guards/models/product.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.html',
  imports:[CommonModule],
  standalone:true
 })
export class HomeComponent {
products:Product[]=[];

  constructor (private router:Router,
    private productService: ProductService

  ){}


  ngOnInit(){
    this.productService.getProduct().subscribe({
      next: (data)=> this.products=data,
      error:()=>alert('Failed to load Products')
    })
  }



  logout(){
  localStorage.removeItem('token');
  this.router.navigate(['./login']);
  }
}
