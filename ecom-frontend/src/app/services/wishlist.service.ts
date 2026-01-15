import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

@Injectable({providedIn:'root'})
export class WishlistService{
    private API='http://localhost:8080/wishlist';
    
    constructor(private http:HttpClient){}

    add(productId:number){
        return this.http.post(`${this.API}/add/${productId}`,{});
    }
    getMy(){
        return this.http.get<any>(`${this.API}/my`);
    }

    remove(productId:number){
        return this.http.delete(`${this.API}/remove/${productId}`);
    }
}