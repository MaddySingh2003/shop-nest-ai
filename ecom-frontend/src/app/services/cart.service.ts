import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

@Injectable({providedIn:'root'})
export class CartService{
    private API='http://localhost:8080/cart';
    
    constructor(private http:HttpClient){}

   addToCart(productId: number, qty: number = 1) {
  return this.http.post(
    `http://localhost:8080/cart/add/${productId}?qty=${qty}`,
    {}
  );
}

}