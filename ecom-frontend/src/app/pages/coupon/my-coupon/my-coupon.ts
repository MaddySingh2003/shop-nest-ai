import { Component } from "@angular/core";
import { OrderService } from "../../../services/order.service";
import { CommonModule } from "@angular/common";

@Component({
  selector: 'app-my-coupon',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './my-coupon.html'
})
export class MyCouponsComponent {

  coupons:any[]=[];

  constructor(private orderService:OrderService){}

  ngOnInit(){
    this.orderService.getMyCoupons().subscribe((res:any)=>{
      this.coupons = res;
    });
  }
}