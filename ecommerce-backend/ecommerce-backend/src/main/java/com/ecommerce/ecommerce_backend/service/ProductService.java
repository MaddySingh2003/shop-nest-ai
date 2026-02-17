package com.ecommerce.ecommerce_backend.service;

import com.ecommerce.ecommerce_backend.dto.PricePredictionRequest;
import com.ecommerce.ecommerce_backend.dto.ProductRequest;
import com.ecommerce.ecommerce_backend.model.Product;
import com.ecommerce.ecommerce_backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final MlService mlService;


      public Product addProduct(ProductRequest request){

    Product product = Product.builder()
            .name(request.getName())
            .description(request.getDescription())
            .brand(request.getBrand())
            .category(request.getCategory())   // ⚠ make sure category exists
            .price(request.getPrice())
            .imageUrl(request.getImageUrl())
            .build();

    // 🔥 CALL ML SERVICE
    PricePredictionRequest mlRequest = new PricePredictionRequest();
    mlRequest.setCategory(product.getCategory());
    mlRequest.setBrand(product.getBrand());
    mlRequest.setBase_price(product.getPrice());
    mlRequest.setDemand_score(0.7);   // temporary static value
    mlRequest.setRating(4.0);         // temporary static value

    try {
        Double predicted = mlService.getPredictedPrice(mlRequest);
        product.setPredictedPrice(predicted);
    } catch (Exception e) {
        System.out.println("ML Service Failed: " + e.getMessage());
        product.setPredictedPrice(product.getPrice()); // fallback
    }

    return productRepository.save(product);
}


    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product getById(Long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product Not Found"));
    }

    public Page<Product> getAllPaged(Pageable pageable){
    return productRepository.findAll(pageable);
}

public Page<Product> search(String keyword, Pageable pageable){
    return productRepository.findByNameContainingIgnoreCase(keyword, pageable);
}

public Page<Product> filterByPrice(double min, double max, Pageable pageable){
    return productRepository.findByPriceBetween(min, max, pageable);
}

    public Product create(Product product){
        product.setCreatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

     public Product updateProduct(Long id, ProductRequest request){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setBrand(request.getBrand());
        product.setPrice(request.getPrice());
        product.setImageUrl(request.getImageUrl());

        return productRepository.save(product);
    }

     public void deleteProduct(Long id){
        if(!productRepository.existsById(id)){
            throw new RuntimeException("Product not found");
        }
        productRepository.deleteById(id);
    }

    public Product updateStock(Long id, int stock){
    Product p = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));

    p.setStock(stock);
    return productRepository.save(p);
}
}
