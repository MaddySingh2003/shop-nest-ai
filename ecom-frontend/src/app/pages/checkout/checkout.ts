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
  showForm = false;
 couponCode: string='';
  discount:number=0;
  cartItems: any[] = [];
  total: number = 0;

  outOfStock: boolean = false;

  newAddress: any = {
    fullName: '',
    phone: '',
    street: '',
    city: '',
    state: '',
    zipCode: '',
    country: 'India',
    defaultAddress: false
  };

  constructor(
    private orderService: OrderService,
    private addressService: AddressService,
    private router: Router,
    private cartService: CartService,
    private cdr:ChangeDetectorRef,
  ) {}

  ngOnInit() {
    this.loadAddresses();
    this.loadCart();
    this.cdr.detectChanges();
   
  }


  // ------------------------
  loadAddresses() {
    this.loading = true;

    this.addressService.getMyAddresses().subscribe({
      next: (res: any) => {
        this.addresses = res || [];
        this.loading = false;

        // ✅ Auto-select first address (UX improvement)
        if (this.addresses.length > 0 && !this.addressId) {
          this.addressId = this.addresses[0].id;
        }
      },
      error: () => {
        this.loading = false;
      }
    });
     this.cdr.detectChanges();
  }

  // ------------------------
  loadCart() {
  this.cartService.getCart().subscribe({
    next: (res: any) => {
      console.log("RAW CART RESPONSE:", res);

      const items = Array.isArray(res) ? res : res.items || [];

      console.log("EXTRACTED ITEMS:", items);

      this.cartItems = items;

      // 🔥 IMPORTANT: call AFTER assignment
      this.calculateTotal();
      this.checkStock();
    },
    error: (err) => {
      console.error(err);
      this.cartItems = [];
      this.total = 0;
      this.outOfStock = false;
    }
  });
  this.cdr.detectChanges();
}
  // ------------------------
  checkStock() {
    this.outOfStock = this.cartItems.some(item => {
      const stock = item.product?.stock ?? item.stock ?? 0;
      return stock <= 0;
    });
     this.cdr.detectChanges();

    console.log("OUT OF STOCK:", this.outOfStock);
  }

  // ------------------------
  calculateTotal() {
    if (!this.cartItems || this.cartItems.length === 0) {
      this.total = 0;
      return;
    }

    this.total = this.cartItems.reduce((acc: number, item: any) => {
      const price = item.product?.price ?? item.price ?? 0;
      return acc + (price * item.quantity);
    }, 0);
 this.cdr.detectChanges();
    console.log("TOTAL:", this.total);
  }

  // ------------------------
  placeOrder() {

    console.log("Selected addressId:", this.addressId);

    if (!this.addressId) {
      alert("Please select delivery address");
      return;
    }

    if (this.outOfStock) {
      alert("Some items are out of stock");
      return;
    }

    if (this.total === 0) {
      alert("Cart is empty");
      return;
    }

    console.log("CALLING ORDER API...");

  this.orderService.placeOrder(this.addressId, this.couponCode).subscribe({
      next: (res: any) => {
        console.log("ORDER RESPONSE:", res);

        const orderId = res?.id || res?.orderId;

        if (!orderId) {
          alert("Order created but ID missing");
          return;
        }

        alert("Order placed successfully");

        this.router.navigate(['/order-success', orderId]);
      },
      error: (err) => {
        console.error("ORDER ERROR:", err);

        const msg = err?.error?.error || "Order failed";
        alert(msg);
      }
    });
   this.cdr.detectChanges();
  }

  // ------------------------
  saveAddress() {
    this.addressService.addAddress(this.newAddress).subscribe(() => {
      this.showForm = false;
      this.resetForm();
      this.loadAddresses();
    });
     this.cdr.detectChanges();
  }

  editAddress(a: any) {
    this.newAddress = { ...a };
    this.showForm = true;
  }

  deleteAddress(id: number) {
    if (!confirm("Delete this address?")) return;

    this.addressService.deleteAddress(id).subscribe(() => {
      this.loadAddresses();
    });
  }

  resetForm() {
    this.newAddress = {
      fullName: '',
      phone: '',
      street: '',
      city: '',
      state: '',
      zipCode: '',
      country: 'India',
      defaultAddress: false
    };
  }
  applyCoupon(){
    if(!this.couponCode){
      alert("Enter coupon");
      return;
    }
    alert("Coupon appled");
  }
}