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
  ){}

  ngOnInit(){

    const id = Number(this.route.snapshot.paramMap.get('id'));
    console.log("ORDER ID:", id);

    this.orderService.getOrderById(id).subscribe({
      next:(res:any)=>{
        console.log("ORDER DETAILS RESPONSE:", res);

        this.order = res;
        this.loading = false;

        // 🔥 FORCE UI REFRESH
        this.cdr.detectChanges();
      },
      error:(err)=>{
        console.error(err);
        this.loading = false;
        this.cdr.detectChanges();
      }
    })
  }
}
