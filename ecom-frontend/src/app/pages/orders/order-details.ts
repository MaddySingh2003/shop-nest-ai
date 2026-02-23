import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { OrderService } from '../../services/order.service';
import { Navbar } from '../../components/navbar/navbar';

@Component({
  selector: 'app-order-details',
  standalone: true,
  imports: [CommonModule, RouterModule,Navbar],
  templateUrl: './order-details.html'
})
export class OrderDetailsComponent implements OnInit {

  order:any = null;
  loading = true;

  statuses = ['PENDING','CONFIRMED','SHIPPED','DELIVERED'];

  constructor(
    private route:ActivatedRoute,
    private orderService:OrderService,
    private cdr:ChangeDetectorRef
  ){}

  intervalId:any;
  ngOnInit(){
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.loadOrder(id);
    
    this.intervalId=setInterval(()=>{
      this.loadOrder(id);
    },5000);
  }
  

  ngOnDestroy(){
    if(this.intervalId){
      clearInterval(this.intervalId);
    }
  }

  loadOrder(id:number){
  this.orderService.getOrderById(id).subscribe({
    next: (res)=>{
      this.order = res;
      this.loading = false;
      this.cdr.detectChanges();
    },
    error: ()=>{
      alert("Failed to load order");
      this.loading = false;
      this.cdr.detectChanges();
    }
  });
}
  isCompleted(step: string) {
  if (!this.order) return false;

  if (this.order.status === 'CANCELLED') return false;

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
