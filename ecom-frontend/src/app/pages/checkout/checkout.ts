import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { OrderService } from '../../services/order.service';
import { AddressService } from '../../services/adress.service';
import { Router } from '@angular/router';
import { Navbar } from '../../components/navbar/navbar';
import { CartService } from '../../services/cart.service';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [CommonModule, FormsModule, Navbar],
  templateUrl: './checkout.html'
})
export class CheckoutComponent implements OnInit {

  addresses: any[] = [];
  addressId: number | null = null;
  loading = true;
  showForm:boolean=false;

  cartItems: any[] = [];
  total: number = 0;

  couponCode: string = '';
  discount: number = 0;
  finalTotal: number = 0;

  outOfStock: boolean = false;

  constructor(
    private orderService: OrderService,
    private addressService: AddressService,
    private router: Router,
    private cartService: CartService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.loadAddresses();
    this.loadCart();
    this.cdr.detectChanges();
    
  }

  // ------------------------
  loadAddresses() {
    this.addressService.getMyAddresses().subscribe((res: any) => {
      this.addresses = res || [];

      if (this.addresses.length > 0) {
        this.addressId = this.addresses[0].id;
      }

      this.loading = false;
    });
    this.cdr.detectChanges();
  }

  // ------------------------
  loadCart() {
    this.cartService.getCart().subscribe({
      next: (res: any) => {
        const items = Array.isArray(res) ? res : res.items || [];
        this.cartItems = items;

        this.calculateTotal();
        this.checkStock();
      },
      error: () => {
        this.cartItems = [];
        this.total = 0;
      }
    });
    this.cdr.detectChanges();
  }

  // ------------------------
  calculateTotal() {
    this.total = this.cartItems.reduce((acc: number, item: any) => {
      const price = item.product?.price ?? item.price ?? 0;
      return acc + (price * item.quantity);
    }, 0);

    this.finalTotal = this.total; // default
 this.cdr.detectChanges();
  }


  // ------------------------
  checkStock() {
    this.outOfStock = this.cartItems.some(item => {
      const stock = item.product?.stock ?? item.stock ?? 0;
      return stock <= 0;
    });
    this.cdr.detectChanges();
  }

  // ------------------------
  applyCoupon(){
  if(!this.couponCode){
    alert("Enter coupon");
    return;
  }

  this.orderService.validateCoupon(this.couponCode)
    .subscribe({
      next:(coupon:any)=>{

        let discount = this.total * (coupon.discountPercent / 100);

        if(discount > coupon.maxDiscount){
          discount = coupon.maxDiscount;
        }

        this.discount = discount;
        this.finalTotal = this.total - discount;

        alert("Coupon applied ✅");
        this.cdr.detectChanges();
      },
      error:()=>{
        alert("Invalid coupon ❌");
        this.discount = 0;
        this.finalTotal = this.total;
      }
    });
}

  // ------------------------
  placeOrder() {

    if (!this.addressId) {
      alert("Select address");
      return;
    }

    if (this.outOfStock) {
      alert("Out of stock");
      return;
    }

    if (this.total === 0) {
      alert("Cart empty");
      return;
    }

    this.orderService.placeOrder(this.addressId, this.couponCode)
      .subscribe({
        next: (res: any) => {
          this.router.navigate(['/order-success', res.id]);
        },
        error: (err) => {
          alert(err?.error?.message || "Order failed");
        }
      });
      this.cdr.detectChanges();
  }
}