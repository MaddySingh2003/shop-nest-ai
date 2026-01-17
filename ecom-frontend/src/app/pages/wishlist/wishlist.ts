import { CommonModule } from "@angular/common";
import { ChangeDetectorRef, Component, OnInit } from "@angular/core";
import { RouterModule } from "@angular/router";
import { WishlistService } from "../../services/wishlist.service";


@Component({
    standalone:true,
    imports:[CommonModule,RouterModule],
    templateUrl:'./wishlist.html'
})
export class WishlistComponent implements OnInit{

    constructor(
      private cdr:ChangeDetectorRef,
      private wishlistService:WishlistService,){}
    



wishlist: any[] = [];
loading = true;

ngOnInit(){
  this.wishlistService.getMy().subscribe({
    next: (res:any)=>{
      console.log("WISHLIST RESPONSE", res);

      this.wishlist = res.products;   // 👈 MATCH TEMPLATE
      this.loading = false;
      this.cdr.detectChanges();
    },
    error: err=>{
      console.error(err);
      this.loading = false;
    this.cdr.detectChanges();
    }
  })
}


}