
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WishlistService } from '../../services/wishlist.service';
import { CartService } from '../../services/cart.service';
import { Navbar } from '../../components/navbar/navbar';

@Component({
  selector: 'app-wishlist',
  standalone: true,
  imports: [CommonModule,Navbar],
  templateUrl: './wishlist.html'
})
export class WishlistComponent implements OnInit {

  wishlist:any[]=[];
  loading=true;

  constructor(
    private wishlistService:WishlistService,
    private cartService:CartService,
    private cdr:ChangeDetectorRef
  ){}

  ngOnInit(){
    this.loadWishlist();
  }

  loadWishlist(){
    this.wishlistService.getWishlist().subscribe(res=>{
      console.log("WISHLIST",res);
      this.wishlist = res.products;
      this.loading=false;
      this.cdr.detectChanges();
    })
  }

  moveToCart(product:any){
    this.cartService.addToCart(product.id,1).subscribe(()=>{
      this.remove(product.id);
    })
  }

  remove(id:number){
    this.wishlistService.removeFromWishlist(id).subscribe(()=>{
      this.loadWishlist();
    })
  }
}
