import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

@Injectable({providedIn:'root'})
export class OrderService {

  private API = 'http://localhost:8080/orders';

  constructor(private http:HttpClient){}

  getMyOrders(){
    return this.http.get(`${this.API}/my`);
  }

  placeOrder(addressId:number){
    return this.http.post(`${this.API}/place/${addressId}`, {});
  }
  getOrderById(id:number){
    return this.http.get(`${this.API}/${id}`)
  }
}
