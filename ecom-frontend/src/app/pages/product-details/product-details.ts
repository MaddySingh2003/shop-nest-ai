import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { Navbar } from '../../components/navbar/navbar';
import { CartService } from '../../services/cart.service';
import { ReviewService } from '../../services/review.service';
import { FormsModule } from '@angular/forms';
import { jwtDecode } from 'jwt-decode';



@Component({
  selector: 'app-product-details',
  standalone: true,
  imports: [CommonModule, RouterModule, Navbar,FormsModule],
  templateUrl: './product-details.html'
})
export class ProductDetailsComponent implements OnInit {

  product: any = null;
  recommendations: any[] = [];
  loading = true;
  error: string | null = null;
  currentUserEmail: string = '';
isAdmin: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private cdr: ChangeDetectorRef,
    private cartService:CartService,
    private reviewService: ReviewService,
  ) {}

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      const id = Number(params.get('id'));

      if (!id || id <= 0) {
        this.error = "Invalid product ID";
        this.loading = false;
        return;
      }

      this.loadProduct(id);
      const token=localStorage.getItem('token');
      if(token){
        const decode:any=jwtDecode(token);
        this.currentUserEmail=decode.sub;
        this.isAdmin=decode.role==='ADMIN';
      }
      this.loadRecommendations(id);
      this.loadReviews(id);
    });
  }

  loadProduct(id: number) {
    this.productService.getById(id).subscribe({
      next: (res) => {
        console.log("PRODUCT RESPONSE:", res);
        this.product = res;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error("Product load error:", err);
        this.error = "Failed to load product";
        this.loading = false;
        this.cdr.detectChanges();
      }
      
    });
  }

  loadRecommendations(id: number) {
    this.productService.getRecommendation(id).subscribe({
      next: (res: any) => {
        this.recommendations = res || [];
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error("Recommendation error:", err);
        this.recommendations = [];
        this.cdr.detectChanges();
      }
    });
  }

     addToCart(productId: number) {
    this.cartService.addToCart(productId, 1).subscribe({
      next: () => alert('Product added to cart'),
      error: () => alert('Failed to add')
    });
  }
  reviews:any[]=[];
  newReview={
    rating:5,
    comment:''
  };

  loadReviews(productId:number){
    this.reviewService.getReview(productId).subscribe((res:any)=>{
      this.reviews=res;
      this.cdr.detectChanges();
    });
  }

  submitReview(productId:number){
    this.reviewService.addReview(productId,this.newReview).subscribe(()=>{
      alert("Review added");
      this.newReview={rating:5,comment:''};
      this.loadReviews(productId);
    })
  }
  deleteReview(id: number) {
  this.reviewService.deleteReview(id).subscribe({
    next: (res) => {
      console.log("DELETE RESPONSE:", res);

      this.reviews = this.reviews.filter(r => r.id !== id);

      alert("Review deleted ✅");
      this.cdr.detectChanges();
    },
    error: (err) => {
      console.error("DELETE ERROR:", err);

      // 🔥 IMPORTANT: Check if actually deleted
      this.reviews = this.reviews.filter(r => r.id !== id);

      alert("Deleted but response issue ⚠️");
    }
  });
}
  isOwner(r:any){
    return r.user?.email ===this.currentUserEmail;
  }

}
