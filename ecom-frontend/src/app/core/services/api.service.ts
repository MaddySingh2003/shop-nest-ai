
import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";


@Injectable({
    providedIn: 'root'
})
export class ApiService{
    private BASE_URL='http://localhost:8080'

    constructor (private http:HttpClient){}

    get<T>(url:string){
        return this.http.get<T>(`${this.BASE_URL}${url}`);
    }

    post<T>(url:string, body:any){
        return this.http.post<T>(`${this.BASE_URL}${url}`,body);
    }

    put<T>(url:String,body:any){
        return this.http.put<T>(`${this.BASE_URL}${url}`,body);
    }

    delete<T>(url:string){
        return this.http.delete<T>(`${this.BASE_URL}${url}`);
    }
}