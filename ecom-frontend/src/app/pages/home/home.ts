import { Component, Inject, OnInit, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Observable, of } from 'rxjs';
import { ProductService } from '../../services/product.service';
import { CartService } from '../../services/cart.service';
import { Navbar } from '../../components/navbar/navbar';
import { Router } from '@angular/router';



@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, Navbar],
  templateUrl: './home.html',
})
export class HomeComponent implements OnInit {

  products$!: Observable<any[]>;
  message = '';

 constructor(
  private productService:ProductService,
  private cartService:CartService,
   private router:Router
 
 ){
 
 }
  ngOnInit(): void {
   
      this.products$ = this.productService.getProduct();
    
  }

   addToCart(productId: number) {
    this.cartService.addToCart(productId, 1).subscribe({
      next: () => alert('Product added to cart'),
      error: () => alert('Failed to add')
    });
  }

  goToCart(){
   this.router.navigate(['./cart'])
  }
}
