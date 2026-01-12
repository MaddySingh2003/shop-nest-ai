import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

@Injectable({ providedIn: 'root' })
export class OrderService {

  private API = 'http://localhost:8080/orders';

  constructor(private http: HttpClient) {}

 getMyOrders() {
    return this.http.get<any[]>(`${this.API}/my`);
  }


  placeOrder(addressId: number): Observable<any> {
    return this.http.post(`${this.API}/place/${addressId}`, {});
  }
}
