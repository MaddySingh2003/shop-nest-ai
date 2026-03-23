import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

@Injectable({providedIn:'root'})
export class ProfileService{

    private API='http://shopnest-env.eba-rnmmxa3z.eu-north-1.elasticbeanstalk.com/user';

    constructor(private http:HttpClient){}

    getProfile(){
        return this.http.get(`${this.API}/me`);
    }
    updateProfile(data:any){
  return this.http.put('http://shopnest-env.eba-rnmmxa3z.eu-north-1.elasticbeanstalk.com/user/update',data);
}

}