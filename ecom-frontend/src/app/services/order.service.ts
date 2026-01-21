import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

@Injectable({ providedIn: 'root' })
export class OrderService {

  private API = 'http://localhost:8080/orders';

  constructor(private http: HttpClient) {}

  getMyOrders(){
    return this.http.get(`${this.API}/my`);
  }

  getOrderById(id:number){
    return this.http.get(`${this.API}/${id}`);
  }

  placeOrder(addressId:number){
    return this.http.post(`${this.API}/place/${addressId}`, {});
  }

  cancelOrder(id:number){
    return this.http.put(`${this.API}/cancel/${id}`, null);
  }

  downloadInvoice(id:number){
    return this.http.get(`${this.API}/invoice/${id}`, { responseType:'blob' });
  }

  // ADMIN ONLY
  updateStatus(orderId:number, status:string){
    return this.http.put(
      `${this.API}/update/${orderId}?status=${status}`,
      null
    );
  }
  confirmAfterPayment(id:number){
  return this.http.put(`${this.API}/confirm/${id}`, null);
}

getAllOrdersAdmin(){
  return this.http.get('http://localhost:8080/orders/admin/all');
}


}
