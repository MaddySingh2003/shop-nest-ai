import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

@Injectable({providedIn:'root'})
export class CouponService{
    private API='https://shonest-backend-production.up.railway.app/admin/coupon';

    constructor(private http:HttpClient){}

    createCoupon(data:any){
        return this.http.post(`${this.API}/create`,data);
    }
    validateCoupon(code:string){
        return this.http.get(`${this.API}/validate/${code}`);
    }
}