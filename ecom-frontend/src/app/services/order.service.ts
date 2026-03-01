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

  placeOrder(addressId:number,couponCode?:string){
   return this.http.post('/orders/place', {
    addressId,
    couponCode
  });
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
 confirmAfterPayment(id:number, method:string){
  return this.http.put(
    `${this.API}/confirm/${id}?method=${method}`,
    null
  );
}


getAllOrders(){
  return this.http.get(`${this.API}/admin/all`);
}

deleteAdminOrder(id:number){
  return this.http.delete(`${this.API}/admin/delete/${id}`);
}


getStats(){
  return this.http.get('http://localhost:8080/admin/stats');
}
validateCoupon(code:string){
  return this.http.get(`http://localhost:8080/coupon/validate/${code}`);
}

tryGiftCoupon(){
  return this.http.get(`http://localhost:8080/coupon/gift`);
}getMyCoupons(){
  return this.http.get('http://localhost:8080/coupon/my');
}
}
