import { Component, Inject, OnInit, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Observable, of } from 'rxjs';
import { ProductService } from '../../services/product.service';
import { CartService } from '../../services/cart.service';
import { Navbar } from '../../components/navbar/navbar';
import { Router } from '@angular/router';
import { WishlistService } from '../../services/wishlist.service';



@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, Navbar],
  templateUrl: './home.html',
})
export class HomeComponent implements OnInit {

  products$!: Observable<any[]>;
  message = '';

 constructor(
  private productService:ProductService,
  private cartService:CartService,
   private router:Router,
   private wishlistService:WishlistService
 
 ){
 
 }
  ngOnInit(): void {
   
      this.products$ = this.productService.getProduct();
    
  }

   

  goToCart(){
   this.router.navigate(['./cart'])
  }
  
  goToDetail(id: number) {
  this.router.navigate(['/product', id]);
}

addWishlist(id:number){
  this.wishlistService.add(id).subscribe(()=>{
    alert('added to wishlist');
  });
}

}
