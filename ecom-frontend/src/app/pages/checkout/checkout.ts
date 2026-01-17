import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { OrderService } from '../../services/order.service';
import { AddressService } from '../../services/adress.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './checkout.html'
})
export class CheckoutComponent implements OnInit {

  addresses:any[]=[];
  addressId:number|null=null;
  loading=true;
  error='';

  constructor(
    private orderService:OrderService,
    private addressService:AddressService,
    private router:Router,
    private cdr: ChangeDetectorRef
  ){}

  showForm = false;

newAddress:any = {
  fullName:'',
  phone:'',
  street:'',
  city:'',
  state:'',
  zipCode:'',
  country:'India',
  defaultAddress:false   // VERY IMPORTANT
};

saveAddress(){
  this.newAddress.defaultAddress=false;

  this.addressService.addAddress(this.newAddress).subscribe(()=>{
    this.showForm=false;
    this.newAddress={};
    this.loadAddresses();
  });
}

editAddress(a:any){
  this.newAddress={...a};
  this.showForm=true;
}

deleteAddress(id:number){
  this.addressService.deleteAddress(id).subscribe(()=>{
    this.loadAddresses();
  });
}

loadAddresses(){
  this.cdr.detectChanges();
}


  ngOnInit(){
    console.log("Checkout init");

    this.addressService.getMyAddresses().subscribe({
      next:res=>{
        console.log("ADDRESS API RESPONSE:", res);

        this.addresses = res || [];
        this.loading = false;

        // 🔥 FORCE UI UPDATE
        this.cdr.detectChanges();
      },
      error:(err)=>{
        console.error(err);
        this.loading=false;
        this.cdr.detectChanges();
      }
    });
  }

  placeOrder(){
    if(!this.addressId){
      alert("Select address");
      return;
    }

    this.orderService.placeOrder(this.addressId).subscribe({
      next:()=>{
        alert("Order placed successfully");
        this.router.navigate(['/home']);
      },
      error:(err)=>{
        this.error=err.error?.error || 'Order failed';
        alert(this.error);
      }
    })
  }
}
