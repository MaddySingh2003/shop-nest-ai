import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Observable } from 'rxjs';
import { ProductService } from '../../services/product.service';
import { CartService } from '../../services/cart.service';
import { Navbar } from '../../components/navbar/navbar';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, Navbar],
  templateUrl: './home.html',
})
export class HomeComponent {

  products$!: Observable<any[]>;
  message = '';

  constructor(
    private productService: ProductService,
    private cartService: CartService
  ) {
    this.products$ = this.productService.getProduct();
  }

  addToCart(productId: number) {
  this.cartService.addToCart(productId, 1).subscribe({
    next: (res) => {
      console.log('Added to cart', res);
      alert('Product added to cart');
    },
    error: (err) => {
      console.error(err);
      alert('Failed to add');
    }
  });
}
}
