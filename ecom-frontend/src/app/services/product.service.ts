import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { map, Observable } from "rxjs";
import { Product } from "../core/guards/models/product.model";

@Injectable({ providedIn: 'root' })
export class ProductService {

  private API = 'http://localhost:8080/products';

  constructor(private http: HttpClient) {}

  getProducts(page:number){
    return this.http.get<any>(`${this.API}?page=${page}&size=12`);
  }

  getById(id:number){
    return this.http.get(`${this.API}/id/${id}`);
  }

  getRecommendation(id:number){
    return this.http.get<any[]>(`${this.API}/${id}/recommendations`);
  }

  addProduct(product:any){
    return this.http.post(`${this.API}/add`,product);
  }
  getSearch(keyword:string,page:number=0){
  return this.http.get<any>(
    `${this.API}/search?keyword=${keyword}&page=${page}&size=12`
  );
}
}