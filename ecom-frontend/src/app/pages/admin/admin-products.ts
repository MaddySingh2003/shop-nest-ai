import { CommonModule } from "@angular/common";
import { ChangeDetectorRef, Component } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { ProductService } from "../../services/product.service";
import { Navbar } from "../../components/navbar/navbar";

@Component({
    selector:'app-admin-products',
    standalone:true,
    imports:[CommonModule,FormsModule,Navbar],
    templateUrl:'./admin-products.html'
})
export class AdminProductsComponent{
     product:any={
    name:'',
    description:'',
    brand:'',
    category:'',
    price:0,
    stock:0,
    imageUrl:''};
    successMessage = '';

    constructor(private productService:ProductService,
       
    ){}
    createProduct(){
        this.productService.addProduct(this.product).subscribe(()=>{
            this.successMessage = "Product added successfully!";
            
            this.product={name:'',
        description:'',
        brand:'',
        category:'',
        price:0,
        stock:0,
        imageUrl:''};
        });
        if(this.successMessage!=""&& alert(this.product)){
        window.location.reload();}
    }
}