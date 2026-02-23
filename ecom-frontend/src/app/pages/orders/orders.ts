import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderService } from '../../services/order.service';
import { RouterModule } from '@angular/router';
import { Navbar } from '../../components/navbar/navbar';

@Component({
  selector: 'app-orders',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './orders.html'
})
export class OrdersComponent implements OnInit {

  orders: any[] = [];
  filteredOrders: any[] = [];

  filter: string = 'ALL';

  constructor(private orderService: OrderService) {}

  ngOnInit() {
    this.loadOrders();
  }

  // 🔥 Load orders
  loadOrders() {
    this.orderService.getMyOrders().subscribe((res: any) => {
      const data = res.content || res;

      // ❌ remove cancelled from main list
      this.orders = data.filter((o: any) => o.status !== 'CANCELLED');

      this.applyFilter();
    });
  }

  // 🔥 Filter logic
  applyFilter() {
    if (this.filter === 'ALL') {
      this.filteredOrders = this.orders;
    } else {
      this.filteredOrders = this.orders.filter(
        o => o.status === this.filter
      );
    }
  }

  // 🔥 Cancel order
  cancelOrder(id: number) {
    if (!confirm("Cancel this order?")) return;

    this.orderService.cancelOrder(id).subscribe(() => {
      this.loadOrders(); // reload after cancel
    });
  }
}