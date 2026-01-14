import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { OrderService } from '../../services/order.service';

@Component({
  selector: 'app-order-details',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './order-details.html'
})
export class OrderDetailsComponent implements OnInit {

  order:any = null;
  loading = true;

  constructor(
    private route: ActivatedRoute,
    private orderService: OrderService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const id =Number( this.route.snapshot.paramMap.get('id'));

    this.orderService.getOrderById(id!).subscribe({
      next: (res:any) => {
        console.log('ORDER RESPONSE RAW', res);

        this.order = res;
        this.loading = false;

        // 🔥 force UI update
        this.cdr.detectChanges();
      },
      error: err => {
        console.error(err);
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }
steps = ['PENDING','CONFIRMED','SHIPPED','DELIVERED'];

isCompleted(step:string): boolean {
  return this.steps.indexOf(this.order.status) >= this.steps.indexOf(step);
}

cancelOrder() {
  if (!confirm('Are you sure you want to cancel this order?')) return;

  this.orderService.cancelOrder(this.order.id).subscribe({
    next: () => {
      alert('Order cancelled successfully');
      this.order.status = 'CANCELLED';
    },
    error: err => {
      alert(err.error?.error || 'Cancel failed');
    }
  });
}


}
