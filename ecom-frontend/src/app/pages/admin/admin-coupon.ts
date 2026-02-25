import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-admin-coupon',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-coupon.html'
})
export class AdminCouponComponent {

  coupon = {
    code: '',
    discountPercent: 10,
    maxDiscount: 200,
    expiryDate: '',
    active: true
  };

  constructor(private http: HttpClient) {}

  createCoupon() {
    this.http.post('http://localhost:8080/admin/coupon/create', this.coupon)
      .subscribe(() => {
        alert("Coupon created ✅");
      });
  }
}