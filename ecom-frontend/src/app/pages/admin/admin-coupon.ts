import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CouponService } from '../../services/coupon.service';


@Component({
  selector: 'app-admin-coupon',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-coupon.html'
})
export class AdminCouponComponent {

  coupon:any = {
    code: '',
    discountPercent: 0,
    maxDiscount: 0,
    expiryDate: '',
    usageLimit: 10,
    productId: null
  };

  constructor(private couponService: CouponService) {}

  create(){
    this.couponService.createCoupon(this.coupon)
      .subscribe({
        next:()=>{
          alert("Coupon created ✅");
        },
        error:(err)=>{
          alert(err?.error?.message || "Error creating coupon");
        }
      });
  }
}