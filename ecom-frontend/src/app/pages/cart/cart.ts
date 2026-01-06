import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { CartService } from '../../services/cart.service';

@Component({
  selector: 'app-cart',
  imports: [CommonModule],
  standalone:true,
  templateUrl: './cart.html',
  styleUrl: './cart.css',
})
export class Cart implements OnInit {

  cart:any;
  total=0;

  constructor(private cartService:CartService){}

  ngOnInit(){
    this.loadCart();
  }

  loadCart(){
    this.total=this.cart.items.reduce(
      (sum:number,i:any)=>sum +i.product.price*i.quantity,0
    );
  }

  calculateTotal() {
    this.total = this.cart.items.reduce(
      (sum: number, i: any) => sum + i.product.price * i.quantity,
      0
    );
  }

   increase(item: any) {
    this.cartService.updateQty(item.id, item.quantity + 1)
      .subscribe(() => this.loadCart());
  }

  decrease(item: any) {
    if (item.quantity > 1) {
      this.cartService.updateQty(item.id, item.quantity - 1)
        .subscribe(() => this.loadCart());
    }}
    remove(itemId: number) {
    this.cartService.removeItem(itemId)
      .subscribe(() => this.loadCart());
  }
}
