import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { Navbar } from '../../components/navbar/navbar';
import { CartService } from '../../services/cart.service';


@Component({
  selector: 'app-product-details',
  standalone: true,
  imports: [CommonModule, RouterModule, Navbar],
  templateUrl: './product-details.html'
})
export class ProductDetailsComponent implements OnInit {

  product: any = null;
  recommendations: any[] = [];
  loading = true;
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private cdr: ChangeDetectorRef,
    private cartService:CartService
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
      this.loadRecommendations(id);
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

}
