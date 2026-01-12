import { CommonModule } from '@angular/common';
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CartService } from '../../services/cart.service';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule,RouterModule],
  templateUrl: './cart.html',
  styleUrls: ['./cart.css']
})
export class Cart implements OnInit {

  cart:any = null;
  total = 0;

  constructor(
    private cartService: CartService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadCart();
  }

  loadCart() {
    this.cartService.getCart().subscribe(res => {
      console.log('CART RESPONSE', res);

      this.cart = res;
      this.calculateTotal();

      // 🔥 FORCE UI UPDATE
      this.cdr.detectChanges();
    });
  }

  calculateTotal() {
    if (!this.cart?.items) return;

    this.total = this.cart.items.reduce(
      (sum: number, i: any) => sum + i.price * i.quantity,
      0
    );
  }

  increase(item:any) {
    this.cartService.updateQty(item.itemId, item.quantity+1)
      .subscribe(()=> this.loadCart());
  }

  decrease(item:any) {
    if(item.quantity===1) return;
    this.cartService.updateQty(item.itemId, item.quantity-1)
      .subscribe(()=> this.loadCart());
  }

  remove(itemId:number){
    this.cartService.removeItem(itemId)
      .subscribe(()=> this.loadCart());
  }
  clearCart(){
  this.cartService.clearCart()
    .subscribe(()=>{
      this.loadCart();
    });
}
}
