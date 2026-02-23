import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderService } from '../../services/order.service';
import { Navbar } from '../../components/navbar/navbar';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, Navbar],
  templateUrl: './dashboard.html'
})
export class DashboardComponent implements OnInit {

  stats:any = null;

  constructor(private orderService:OrderService){}

  ngOnInit(){
    this.orderService.getStats().subscribe(res=>{
      this.stats = res;
    });
  }
}