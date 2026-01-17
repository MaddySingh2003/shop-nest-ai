import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

@Injectable({providedIn:'root'})
export class ProfileService{

    private API='http://localhost:8080/user';

    constructor(private http:HttpClient){}

    getProfile(){
        return this.http.get(`${this.API}/me`);
    }
}