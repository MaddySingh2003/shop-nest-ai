import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router, RouterModule } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-navbar',
  imports: [CommonModule,RouterModule,FormsModule],
  standalone:true,
  templateUrl: './navbar.html',
 
})
export class Navbar {
  keyword:string='';

  suggestions:any[]=[];
  showSuggestions=false;

  constructor(
    public auth:AuthService,
    private router:Router,
    private productService:ProductService
    
  ){}

  logout(){
    this.auth.logout();
    this.router.navigate(['./login']);
  }

  search(){
  if(!this.keyword.trim()) return;

  this.router.navigate(['/search'],{queryParams:{keyword:this.keyword}})
  
  }
  onTyping(){
    if(!this.keyword.trim()){
      this.showSuggestions=false;
      return;
    }
    this.productService
    .getuggestions(this.keyword)
    .subscribe((res)=>{
      this.suggestions=res.content;
      this.showSuggestions=true;
    });
  }
  openProduct(id:number){
    this.showSuggestions=false;
    this.router.navigate(['/product',id]);
  }
}
