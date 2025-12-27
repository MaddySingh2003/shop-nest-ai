package com.ecommerce.ecommerce_backend.service;

import com.ecommerce.ecommerce_backend.model.Product;
import com.ecommerce.ecommerce_backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product addProduct(Product product) {
        product.setCreatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

public Product updateProduct(Long id,Product updated){
    return productRepository.findById(id).map(product ->{
        product.setName(updated.getName());
        product.setBrand((updated.getBrand()));
        product.setDescription(updated.getDescription());
        product.setPrice(updated.getPrice());
        product.setImageUrl(updated.getImageUrl());
        product.setPredictedPrice(updated.getPredictedPrice());
        return productRepository.save(product);
    }).orElseThrow(()-> new RuntimeException("Product not Found"));
}

public void deleteProduct(Long id){
    if(!productRepository.existsById(id)){
        throw new RuntimeException("Product not Found");
    }
    productRepository.deleteById(id);
}
public Product getProduct(Long id){
    return productRepository.findById(id).orElseThrow(()-> new RuntimeException("Product not found"));
}

}
