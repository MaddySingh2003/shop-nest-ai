import { ChangeDetectorRef, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderService } from '../../services/order.service';

@Component({
  selector: 'app-admin-orders',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-orders.html'
})
export class AdminOrdersComponent {

  orders:any[] = [];

  constructor(private orderService:OrderService,
    private cdr:ChangeDetectorRef,
  ){
    this.loadOrders();
  }

  loadOrders(){
    this.orderService.getAllOrdersAdmin().subscribe((res:any)=>{
      this.orders = res.content;
      this.cdr.detectChanges();   // 🔥 IMPORTANT
      console.log("ADMIN ORDERS:", this.orders);
    });
  }

  updateStatus(id:number, status:string){
    this.orderService.updateStatus(id,status).subscribe(()=>{
      this.loadOrders();
      this.cdr.detectChanges();
    });
  }
}
