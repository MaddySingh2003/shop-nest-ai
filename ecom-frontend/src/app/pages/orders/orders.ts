import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderService } from '../../services/order.service';
import { Observable, map } from 'rxjs';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-orders',
  standalone: true,
  imports: [CommonModule,RouterModule],
  templateUrl: './orders.html'
})
export class OrdersComponent {

  orders$!: Observable<any[]>;

  constructor(private orderService: OrderService) {
    this.orders$ = this.orderService.getMyOrders()
      .pipe(map((res: any) => res.content));   // 🔥 FIX
  }
}
