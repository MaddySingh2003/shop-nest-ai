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

  addresses:any[] = [];
  addressId:number | null = null;
  loading = true;
  error = '';

  showForm = false;

  newAddress:any = {
    fullName:'',
    phone:'',
    street:'',
    city:'',
    state:'',
    zipCode:'',
    country:'India',
    defaultAddress:false
  };

  constructor(
    private orderService:OrderService,
    private addressService:AddressService,
    private router:Router,
    private cdr: ChangeDetectorRef
  ){}

  ngOnInit(){
    console.log("Checkout init");
    this.loadAddresses();
  }

  // ------------------------
  loadAddresses(){
    this.loading = true;

    this.addressService.getMyAddresses().subscribe({
      next:(res:any)=>{
        console.log("ADDRESS API RESPONSE:", res);
        this.addresses = res || [];
        this.loading = false;
        this.cdr.detectChanges();
      },
      error:(err)=>{
        console.error(err);
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  // ------------------------
  saveAddress(){
    this.addressService.addAddress(this.newAddress).subscribe(()=>{
      alert("Address saved");
      this.showForm = false;
      this.resetForm();
      this.loadAddresses();
    });
  }

  editAddress(a:any){
    this.newAddress = {...a};
    this.showForm = true;
  }

  deleteAddress(id:number){
    if(!confirm("Delete this address?")) return;

    this.addressService.deleteAddress(id).subscribe(()=>{
      this.loadAddresses();
    });
  }

  resetForm(){
    this.newAddress = {
      fullName:'',
      phone:'',
      street:'',
      city:'',
      state:'',
      zipCode:'',
      country:'India',
      defaultAddress:false
    };
  }

  // ------------------------
  placeOrder(){
    console.log("PLACE ORDER CLICKED");

    if(!this.addressId){
      alert("Select address");
      return;
    }

    this.orderService.placeOrder(this.addressId).subscribe({
      next:(res:any)=>{
        console.log("ORDER RESPONSE",res);
        alert("Order placed successfully");
        this.router.navigate(['/orders-success',res.id]);
      },
      error:(err)=>{
        console.log("ORDER ERROR",err);
        alert(err.error?.error || "Order failed");
      }
    });
  }
}
