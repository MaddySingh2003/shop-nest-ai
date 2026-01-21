import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { OrderService } from '../../services/order.service';

@Component({
  selector: 'app-invoice',
  standalone: true,
  imports: [CommonModule,RouterModule],
  templateUrl: './invoice.html'
})
export class InvoiceComponent implements OnInit {

  order:any=null;
  loading=true;

  constructor(
    private route:ActivatedRoute,
    private orderService:OrderService,
    private cdr:ChangeDetectorRef
  ){}

  ngOnInit(){
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.load(id);
  }

  load(id:number){
    this.orderService.getOrderById(id).subscribe(res=>{
      this.order=res;
      this.loading=false;
      this.cdr.detectChanges();
    });
  }

  downloadInvoice(){
    this.orderService.downloadInvoice(this.order.id).subscribe(blob=>{
      const url=window.URL.createObjectURL(blob);
      const a=document.createElement('a');
      a.href=url;
      a.download=`Invoice_${this.order.id}.pdf`;
      a.click();
    });
  }
}
