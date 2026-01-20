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

  orders$!: Observable<any[]>;

  constructor(private orderService: OrderService) {
    this.orders$ = this.orderService.getMyOrders()
  .pipe(
    map((res:any)=>
      res.content
        .filter((o:any)=>o.status!=='CANCELLED')
        .map((o:any)=>({
          ...o,
          displayStatus: o.status === 'PENDING' ? 'CONFIRMED' : o.status
        }))
    )
  );


  }

  cancelOrder(id:number){
    if(!confirm("Cancel this order?")) return;

    this.orderService.cancelOrder(id).subscribe(()=>{
      // reload list automatically
      this.orders$ = this.orderService.getMyOrders()
        .pipe(
          map((res:any)=>res.content.filter((o:any)=>o.status!=='CANCELLED'))
        );
    });
  }
}
