import { Component, Inject, OnInit , PLATFORM_ID} from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule,isPlatformBrowser } from '@angular/common';
import { ProductService } from '../../services/product.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.html',
  styleUrls:['../../../../src/styles.css']
})
export class HomeComponent  {

  
  products$!: Observable<any[]>;

  constructor(private productService: ProductService) {
    this.products$ = this.productService.getProduct();
  }



ngOnDestroy() {
  console.log('HOME DESTROYED');
}

}
