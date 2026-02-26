import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderService } from '../../services/order.service';

@Component({
  selector: 'app-gift',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './gift.html'
})
export class GiftComponent {

  loading = false;
  message = '';

  constructor(private orderService: OrderService) {}
tryCoupon(){
  this.loading = true;

  this.orderService.tryGiftCoupon().subscribe({
    next:(res:any)=>{
      this.loading = false;
      this.message = "🎉 You got coupon: " + res.code;
    },
    error:(err)=>{
      this.loading = false;

      const msg =
        err?.error?.message ||
        err?.error ||
        "Try failed";

      this.message = msg;
    }
  });
}
}