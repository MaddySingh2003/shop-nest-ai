import { CommonModule } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { RouterModule } from "@angular/router";
import { WishlistService } from "../../services/wishlist.service";


@Component({
    standalone:true,
    imports:[CommonModule,RouterModule],
    templateUrl:'./wishlist.html'
})
export class WishlistComponent implements OnInit{
    
    list:any[]=[];

    constructor(private wishlist:WishlistService){}

    ngOnInit(){
        this.wishlist.getMy().subscribe(res=>{
            this.list=res;
        });
    }

    remove(id:number){
        this.wishlist.remove(id).subscribe(()=>{
            this.list=this.list.filter(x=>x.productId!==id);
        });
    }
}