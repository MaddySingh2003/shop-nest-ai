import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CartService } from '../../services/cart.service';
import { OrderService } from '../../services/order.service';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './checkout.html'
})
export class CheckoutComponent implements OnInit {

  cart:any;
  addressId:number = 1; // temp default
  placing = false;

  constructor(
    private cartService:CartService,
    private orderService:OrderService
  ){}

  ngOnInit(){
    this.cartService.getCart().subscribe(res=>{
      this.cart = res;
    });
  }

  placeOrder(){
    this.placing = true;

    this.orderService.placeOrder(this.addressId).subscribe({
      next:()=>{
        alert('Order placed successfully');
        this.placing = false;
      },
      error:()=>{
        alert('Order failed');
        this.placing = false;
      }
    });
  }
}
