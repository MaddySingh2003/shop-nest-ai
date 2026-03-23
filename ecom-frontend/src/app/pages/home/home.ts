import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductService } from '../../services/product.service';
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
  pages: number[] = [];
  message = '';

  constructor(
    private productService: ProductService,
    private router: Router,
    private wishlistService: WishlistService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.loadProducts();
  }
loadProducts() {
  this.productService.getProducts(this.page).subscribe({
    next: (res) => {
      this.products = res.content || [];
      this.totalPages = res.totalPages || 0;
      this.generatePages();
      this.cdr.detectChanges();
    },
    error: (err) => {
      console.error('API ERROR:', err);
      this.message = 'Failed to load products';
      this.products = []; // ensures UI updates
      this.cdr.detectChanges();
    }
  });
}
  generatePages() {

    const start = Math.max(0, this.page - 1);
    const end = Math.min(this.totalPages - 1, this.page + 1);

    this.pages = [];

    for (let i = start; i <= end; i++) {
      this.pages.push(i);
    }

  }

  scrollTop() {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  nextPage() {

    if (this.page < this.totalPages - 1) {
      this.page++;
      this.loadProducts();
      this.scrollTop();
    }

  }

  prevPage() {

    if (this.page > 0) {
      this.page--;
      this.loadProducts();
      this.scrollTop();
    }

  }

  goToPage(p: number) {

    this.page = p;
    this.loadProducts();
    this.scrollTop();

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