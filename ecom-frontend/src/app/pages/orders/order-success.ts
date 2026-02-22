import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-order-success',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './order-success.html'
})
export class OrderSuccessComponent implements OnInit {

  orderId:number = 0;

  constructor(
    private route: ActivatedRoute,
    private router:Router
  ){}

  ngOnInit(){
    this.orderId = Number(this.route.snapshot.paramMap.get('id'));
  }

  goOrders(){
    this.router.navigate(['/account/orders']);
  }

  goHome(){
    this.router.navigate(['/home']);
  }
}
