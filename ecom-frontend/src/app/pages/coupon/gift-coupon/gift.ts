import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderService } from '../../../services/order.service';

@Component({
  selector: 'app-gift',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './gift.html'
})

export class GiftComponent implements OnInit {

  loading = false;
  message = '';

  attempts = 0;
  maxAttempts = 2;

  constructor(private orderService: OrderService,
    
  ) {}

  ngOnInit() {
   

    // optional: load attempts from backend later
  }
tryCoupon(){

  if(this.attempts >= this.maxAttempts){
    this.message = "❌ You used all attempts. Try after 5 days.";
    window.location.reload();
    return;
  }

  this.loading = true;

  this.orderService.tryGiftCoupon().subscribe({
    next:(res:any)=>{
      this.loading = false;
      this.attempts++;

      alert("🎉 You got coupon: " + res.code);

      if(this.attempts >= this.maxAttempts){
        this.message = "❌ You used all attempts. Try after 5 days.";
      }window.location.reload();
    },

    error:(err)=>{
      this.loading = false;
      this.attempts++;

      const msg =
        err?.error?.error ||
        err?.error?.message ||
        err?.error ||
        "Try failed";

      alert(msg);

      if(this.attempts >= this.maxAttempts){
        this.message = "❌ You used all attempts. Try after 5 days.";
      }window.location.reload();
    }
  });

}isDisabled(): boolean {
  return this.loading || this.attempts >= this.maxAttempts;
}
}