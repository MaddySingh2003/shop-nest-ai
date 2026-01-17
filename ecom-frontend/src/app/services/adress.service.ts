import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

@Injectable({providedIn:'root'})
export class AddressService{
    private API='http://localhost:8080/address';

    constructor(private http:HttpClient){}

    getMyAddresses(){
        return this.http.get<any[]>(`${this.API}/my`)
    }
    addAddress(address:any){
  return this.http.post(`${this.API}/add`,address);
}
deleteAddress(id:number){
  return this.http.delete(`${this.API}/delete/${id}`);
}

}