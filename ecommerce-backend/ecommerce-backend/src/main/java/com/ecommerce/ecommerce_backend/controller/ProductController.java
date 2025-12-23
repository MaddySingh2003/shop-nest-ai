package com.ecommerce.ecommerce_backend.controller;

import org.springframework.web.bind.annotation.*;

import com.ecommerce.ecommerce_backend.model.Product;
import com.ecommerce.ecommerce_backend.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin("*")
public class ProductController {
   
    private final ProductService service;
    
    public ProductController (ProductService service){
        this.service=service;
    }

    @GetMapping 
    public List<Product> getAllproduct(){
        return service.getAllproduct();
    }

    @PostMapping
    public Product createProduct (@RequestBody Product product){
        return service.saveProduct(product);
    }



}
