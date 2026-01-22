import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderService } from '../../services/order.service';

@Component({
  selector:'app-admin-orders',
  standalone:true,
  imports:[CommonModule],
  templateUrl:'./admin-orders.html'
})
export class AdminOrdersComponent implements OnInit {

  orders:any[]=[];

  constructor(private orderService:OrderService,
    private cdr:ChangeDetectorRef
  ){}

  ngOnInit(){
    this.loadOrders();
  }

  loadOrders(){
    this.orderService.getAllOrders().subscribe((res:any)=>{
      this.orders = res.content;
      this.cdr.detectChanges();
    });
  }

  updateStatus(id:number,status:string){
    this.orderService.updateStatus(id,status).subscribe(()=>{
      this.loadOrders();
    });
  }

 deleteOrder(id:number){
  this.orderService.deleteAdminOrder(id).subscribe({
    next:()=>{
      // 🔥 remove from UI immediately
      this.orders = this.orders.filter(o => o.id !== id);
    },
    error:(err)=>{
      console.error(err);
      alert("Delete failed");
    }
    
  });
  this.loadOrders();
}


}
