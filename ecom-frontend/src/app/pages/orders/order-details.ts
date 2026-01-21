import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { OrderService } from '../../services/order.service';

@Component({
  selector: 'app-order-details',
  standalone: true,
  imports: [CommonModule,RouterModule],
  templateUrl: './order-details.html'
})
export class OrderDetailsComponent implements OnInit {

  order:any = null;
  loading = true;

  

  constructor(
    private route:ActivatedRoute,
    private orderService:OrderService,
    private cdr:ChangeDetectorRef
  ){}

  ngOnInit(){
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.loadOrder(id);
  }

  loadOrder(id:number){
    this.orderService.getOrderById(id).subscribe(res=>{
      this.order = res;

      // 🔥 YOUR RULE: Order page never shows PENDING
      
      this.loading = false;
      this.cdr.detectChanges();
    });
  }

  statuses = ['PENDING','CONFIRMED','SHIPPED','DELIVERED'];

isCompleted(step:string){
  if(!this.order) return false;
  return this.statuses.indexOf(this.order.status)
       >= this.statuses.indexOf(step);
       
}


  cancelOrder(){
    if(!confirm("Cancel this order?")) return;

    this.orderService.cancelOrder(this.order.id).subscribe(()=>{
      alert("Order cancelled");
      this.loadOrder(this.order.id);
    });
  }

  payNow(){
    alert("Redirecting to payment gateway...");
  }

  downloadInvoice(){
    this.orderService.downloadInvoice(this.order.id).subscribe(blob=>{
      const url = window.URL.createObjectURL(blob);
      const a=document.createElement('a');
      a.href=url;
      a.download=`Invoice_${this.order.id}.pdf`;
      a.click();
    });
  }
}
