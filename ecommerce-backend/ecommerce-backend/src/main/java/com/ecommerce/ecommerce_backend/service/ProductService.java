package com.ecommerce.ecommerce_backend.service;

import com.ecommerce.ecommerce_backend.model.Product;
import com.ecommerce.ecommerce_backend.repository.ProductRepository;

import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class ProductService {
    
    private final ProductRepository repo;
   
    public ProductService(ProductRepository repo){
        this.repo=repo;
    }

    public Product saveProduct(Product product){
        return repo.save(product);
    }

    public List<Product> getAllproduct(){
        return repo.findAll();
    }

}
