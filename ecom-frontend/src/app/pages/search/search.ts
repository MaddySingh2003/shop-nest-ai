import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { CommonModule } from '@angular/common';
import { Navbar } from '../../components/navbar/navbar';

@Component({
  selector: 'app-search',
  standalone: true,
  imports: [CommonModule, RouterModule,Navbar],
  templateUrl: './search.html'
})
export class SearchComponent implements OnInit {

  products:any[] = [];
  keyword:string = '';

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private cd: ChangeDetectorRef
  ){}

  ngOnInit(){

    this.route.queryParams.subscribe(params => {

      this.keyword = params['keyword'];

      if(this.keyword){

        this.productService.getSearch(this.keyword)
        .subscribe((res:any)=>{

          console.log("Search API:",res); // 👈 debug

          this.products = res.content;
          this.cd.detectChanges(); // ✅ IMPORTANT

        });

      }

    });

  }
  

}