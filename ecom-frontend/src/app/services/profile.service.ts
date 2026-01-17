import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

@Injectable({providedIn:'root'})
export class ProfileService{

    private API='http://localhost:8080/user';

    constructor(private http:HttpClient){}

    getProfile(){
        return this.http.get(`${this.API}/me`);
    }
    updateProfile(data:any){
  return this.http.put('http://localhost:8080/user/update',data);
}

}