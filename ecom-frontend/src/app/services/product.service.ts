import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Product } from "../core/guards/models/product.model";


@Injectable({providedIn:'root'})
export class ProductService{

    private API='http://localhost:8080/ptoducts';

    constructor(private http:HttpClient){}

    getProduct(): Observable<Product[]>{
        return this.http.get<Product[]>(this.API);
    }
}