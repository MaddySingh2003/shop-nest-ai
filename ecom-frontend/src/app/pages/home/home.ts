import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductService } from '../../services/product.service';
import { CartService } from '../../services/cart.service';
import { Navbar } from '../../components/navbar/navbar';
import { Router } from '@angular/router';
import { WishlistService } from '../../services/wishlist.service';
import { Footer } from '../../components/footer/footer';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, Navbar, Footer],
  templateUrl: './home.html',
})
export class HomeComponent implements OnInit {

  products: any[] = [];
  page = 0;
  totalPages = 0;
  message = '';

  constructor(
    private productService: ProductService,
    private cartService: CartService,
    private router: Router,
    private wishlistService: WishlistService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.loadProducts();
  }

  loadProducts() {
    this.productService.getProducts(this.page).subscribe(res => {
      this.products = res.content;
      this.totalPages = res.totalPages;
      this.cdr.detectChanges();
    });
  }

  nextPage() {
    if (this.page < this.totalPages - 1) {
      this.page++;
      this.loadProducts();
    }
  }

  prevPage() {
    if (this.page > 0) {
      this.page--;
      this.loadProducts();
    }
  }

  goToPage(p: number) {
    this.page = p;
    this.loadProducts();
  }

  goToCart() {
    this.router.navigate(['/cart']);
  }

  goToDetail(id: number) {
    this.router.navigate(['/product', id]);
  }

  addWishlist(id: number) {
    this.wishlistService.add(id).subscribe(() => {
      alert('Added to wishlist ❤️');
    });
  }
}