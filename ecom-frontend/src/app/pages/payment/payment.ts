import { CommonModule } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";

@Component({
    selector:'app-payment',
    standalone:true,
    imports:[CommonModule,FormsModule],
    templateUrl:'./payment.html'
})
export class PaymentComponenet implements OnInit{
    orderId!:number;
    loading=false;
    paymentMethod='UPI'

    constructor(
        private route:ActivatedRoute,
        private router:Router
    ){}

    ngOnInit(){
        this.orderId=Number(this.route.snapshot.paramMap.get('id'));

    }

    pay(){
  console.log("Payment Method:", this.paymentMethod);

  this.loading=true;

  setTimeout(()=>{
    alert(`Payment successful via ${this.paymentMethod}`);
    this.router.navigate(['/invoice', this.orderId]);
  },1500);
}

}