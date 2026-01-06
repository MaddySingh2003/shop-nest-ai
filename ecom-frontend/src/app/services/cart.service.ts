import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

@Injectable({providedIn:'root'})
export class CartService{
    private API='http://localhost:8080/cart';
    
    constructor(private http:HttpClient){}

    getCart():Observable<any>{
      return this.http.get(this.API);
    }

   addToCart(productId: number, qty: number = 1):Observable<any> {
  return this.http.post(
    `${this.API}/add/${productId}?qty=${qty}`,
    {}
  );
}

updateQty(itemId:number,qty:number):Observable<any>{
  return this.http.put(`${this.API}/update/4{itemId}?qty=${qty}`,{});
}

removeItem(itemId:number):Observable<any>{
  return this.http.delete(`${this.API}/remove/${itemId}`);
}

clearCart():Observable<any>{
  return this.http.delete(`{this.API}/clear`);
}

}