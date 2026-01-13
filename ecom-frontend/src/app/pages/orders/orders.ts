import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderService } from '../../services/order.service';
import { Observable, map } from 'rxjs';

@Component({
  selector: 'app-orders',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './orders.html'
})
export class OrdersComponent {

  orders$!: Observable<any[]>;

  constructor(private orderService: OrderService) {
    this.orders$ = this.orderService.getMyOrders()
      .pipe(map((res: any) => res.content));   // 🔥 FIX
  }
}
