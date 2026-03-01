import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderService } from '../../../services/order.service';
import { Navbar } from '../../../components/navbar/navbar';

@Component({
  selector: 'app-gift',
  standalone: true,
  imports: [CommonModule,Navbar],
  templateUrl: './gift.html'
})

export class GiftComponent implements OnInit {

  loading = false;
  message = '';

  attempts = 0;
  maxAttempts = 2;

  constructor(private orderService: OrderService,
    private cdr:ChangeDetectorRef
    
  ) {}

 myCoupons:any[] = [];

ngOnInit(){
  this.loadMyCoupons();
  this.cdr.detectChanges();
}

loadMyCoupons(){
  this.orderService.getMyCoupons().subscribe((res:any)=>{
    this.myCoupons = res;
    this.cdr.detectChanges();
  });
}tryCoupon(){

  if(this.attempts >= this.maxAttempts){
    this.message = "❌ You used all attempts. Try after 5 days.";
    return;
  }

  this.loading = true;

  this.orderService.tryGiftCoupon().subscribe({
    next:(res:any)=>{
      this.loading = false;
      this.attempts++;

      alert("🎉 You got coupon: " + res.code);

      // ✅ IMPORTANT: reload coupons from backend
      this.loadMyCoupons();

      if(this.attempts >= this.maxAttempts){
        this.message = "❌ You used all attempts. Try after 5 days.";
      }
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
      }
    }
  });
}isDisabled(): boolean {
  return this.loading || this.attempts >= this.maxAttempts;
}
}