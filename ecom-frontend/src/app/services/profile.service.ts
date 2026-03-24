import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

@Injectable({providedIn:'root'})
export class ProfileService{

    private API='https://shop-nest-ai-1.onrender.com/user';

    constructor(private http:HttpClient){}

    getProfile(){
        return this.http.get(`${this.API}/me`);
    }
    updateProfile(data:any){
  return this.http.put('https://shop-nest-ai-1.onrender.com/user/update',data);
}

}