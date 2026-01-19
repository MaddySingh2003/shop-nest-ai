import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderService } from '../../services/order.service';
import { Observable, map, BehaviorSubject } from 'rxjs';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-orders',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './orders.html'
})
export class OrdersComponent {

  private ordersSubject = new BehaviorSubject<any[]>([]);
  orders$ = this.ordersSubject.asObservable();

  constructor(private orderService: OrderService) {
    this.loadOrders();
  }

  loadOrders(){
    this.orderService.getMyOrders().subscribe((res:any)=>{
      const list = res.content.map((o:any)=>{
        if(o.status==='PENDING'){
          o.status='CONFIRMED';
        }
        return o;
      });
      this.ordersSubject.next(list);
    });
  }

  cancelOrder(id:number){
    if(!confirm("Cancel this order?")) return;

    this.orderService.cancelOrder(id).subscribe(()=>{
      alert("Order cancelled");

      // 🔥 REMOVE FROM UI
      const updated = this.ordersSubject.value.filter(o=>o.id!==id);
      this.ordersSubject.next(updated);
    });
  }
}
