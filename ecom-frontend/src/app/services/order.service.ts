import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

@Injectable({ providedIn: 'root' })
export class OrderService {

  private API = 'http://shopnest-env.eba-rnmmxa3z.eu-north-1.elasticbeanstalk.com/orders';

  constructor(private http: HttpClient) {}

  getMyOrders(){
    return this.http.get(`${this.API}/my`);
  }

  getOrderById(id:number){
    return this.http.get(`${this.API}/${id}`);
  }
placeOrder(addressId:number, couponCode?:string){
  return this.http.post(
    `http://shopnest-env.eba-rnmmxa3z.eu-north-1.elasticbeanstalk.com/orders/place/${addressId}?coupon=${couponCode || ''}`,
    {}
  );
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
  return this.http.get('http://shopnest-env.eba-rnmmxa3z.eu-north-1.elasticbeanstalk.comadmin/stats');
}
validateCoupon(code:string){
  return this.http.get(`http://shopnest-env.eba-rnmmxa3z.eu-north-1.elasticbeanstalk.com/coupon/validate/${code}`);
}

tryGiftCoupon(){
  return this.http.get(`http://shopnest-env.eba-rnmmxa3z.eu-north-1.elasticbeanstalk.com/coupon/gift`);
}getMyCoupons(){
  return this.http.get('http://shopnest-env.eba-rnmmxa3z.eu-north-1.elasticbeanstalk.com/coupon/my');
}
}
