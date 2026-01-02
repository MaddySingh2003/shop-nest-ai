import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.html'
})
export class HomeComponent implements OnInit {

  products: any[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.http
      .get<any[]>('http://localhost:8080/products/all')
      .subscribe({
        next: (res) => {
          console.log('PRODUCTS:', res); // DEBUG
          this.products = res;           // ✅ ARRAY ASSIGNMENT
        },
        error: (err) => {
          console.error(err);
          alert('Failed to load products');
        }
      });
  }
}
