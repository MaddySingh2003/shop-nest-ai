import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderService } from '../../services/order.service';

@Component({
  selector: 'app-orders',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './orders.html'
})
export class OrdersComponent implements OnInit {

  orders:any[] = [];
  loading = true;
  error = '';

  constructor(private orderService:OrderService){}

  ngOnInit(){
    this.loadOrders();
  }

  loadOrders(){
    this.orderService.getMyOrders().subscribe({
      next:(res)=>{
        console.log("ORDERS",res);
        this.orders = res;
        this.loading = false;
      },
      error:(err)=>{
        console.error(err);
        this.error = "Failed to load orders";
        this.loading = false;
      }
    })
  }
}
