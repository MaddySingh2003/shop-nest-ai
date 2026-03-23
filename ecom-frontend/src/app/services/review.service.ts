import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

@Injectable({providedIn:'root'})
export class ReviewService{
    private API='http://shopnest-env.eba-rnmmxa3z.eu-north-1.elasticbeanstalk.com/reviews';

    constructor(private http: HttpClient){}

    getReview(productId:Number){
        return this.http.get(`${this.API}/product/${productId}`);
    }
    addReview(productId:number,review:any){
        return this.http.post(`${this.API}/add/${productId}`,review);
    }
    deleteReview(id:number){
        return this.http.delete(`${this.API}/${id}`);
    }
}