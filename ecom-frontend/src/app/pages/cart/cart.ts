import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Component, Inject, OnInit, PLATFORM_ID } from '@angular/core';
import { CartService } from '../../services/cart.service';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './cart.html',
  styleUrls: ['./cart.css'],
})
export class Cart implements OnInit {

  cart: any=null;
  total = 0;

   constructor(
    private cartService: CartService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
     if (isPlatformBrowser(this.platformId)) {
      this.loadCart();
    }
  }

    loadCart() {
    this.cartService.getCart().subscribe(res => {
      this.cart = res;
      this.calculateTotal();
    });
  }

 calculateTotal() {
    if (!this.cart?.items) return;

    this.total = this.cart.items.reduce(
      (sum: number, i: any) => sum + i.price * i.quantity,
      0
    );
  }

  increase(item: any): void {
    this.cartService.updateQty(item.itemId, item.quantity + 1)
      .subscribe(() => this.loadCart());
  }

  decrease(item: any): void {
    if (item.quantity === 1) return;

    this.cartService.updateQty(item.itemId, item.quantity - 1)
      .subscribe(() => this.loadCart());
  }

  remove(itemId: number): void {
    this.cartService.removeItem(itemId)
      .subscribe(() => this.loadCart());
  }
}
