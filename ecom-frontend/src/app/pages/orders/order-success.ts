import { CommonModule } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, RouterModule } from "@angular/router";


@Component({
    standalone:true,
    imports:[CommonModule,RouterModule],
    templateUrl:'./order-success.html'
})
export class OrderSuccessComponenet implements OnInit{

    orderId:any;

    constructor(private router:ActivatedRoute){}

    ngOnInit(){
        this.orderId=this.router.snapshot.paramMap.get('id');
    }
}