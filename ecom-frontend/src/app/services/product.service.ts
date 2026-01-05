import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { map, Observable } from "rxjs";
import { Product } from "../core/guards/models/product.model";

@Injectable({ providedIn: 'root' })
export class ProductService {

  private API = 'http://localhost:8080/products/all';

  constructor(private http: HttpClient) {}
 getProduct(): Observable<any[]> {
    return this.http.get<any>(this.API).pipe(
      map(res => res.content ?? res)
    );}

}
