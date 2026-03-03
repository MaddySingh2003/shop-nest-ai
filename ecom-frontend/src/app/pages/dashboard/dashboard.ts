import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderService } from '../../services/order.service';
import { Navbar } from '../../components/navbar/navbar';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.html'
})
export class DashboardComponent implements OnInit {

  stats:any = null;

  constructor(private orderService:OrderService,
    private cdr:ChangeDetectorRef
  ){}

  ngOnInit(){
    this.orderService.getStats().subscribe(res=>{
      this.stats = res;
      this.cdr.detectChanges();
    });
  }
}