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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final MlService mlService;

    public Product addProduct(ProductRequest request){

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .brand(request.getBrand()==null|| request.getBrand().isBlank()
                ?"Generic"
                :request.getBrand())
                .category(request.getCategory())
                .price(request.getPrice())
                .stock(request.getStock())
                .createdAt(LocalDateTime.now())
                .imageUrl(request.getImageUrl())
                .active(true)
                .build();

        try{

            PricePredictionRequest mlRequest = new PricePredictionRequest();
            mlRequest.setCategory(product.getCategory());
            mlRequest.setBrand(product.getBrand());
            mlRequest.setBasePrice(product.getPrice());
            mlRequest.setDemandScore(0.7);
            mlRequest.setRating(4.0);

            Double predicted=mlService.getPredictedPrice(mlRequest);
            if(predicted==null || predicted<=0||predicted.isNaN()){
                predicted=product.getPrice();
            }
            double min=product.getPrice()*0.7;
            double max=product.getPrice()*1.3;
            if(predicted<min)predicted=min;
            if(predicted>max)predicted=max;

            product.setPredictedPrice(predicted);

        }
        catch(Exception e){

            product.setPredictedPrice(product.getPrice());
        }

        return productRepository.save(product);
    }

    public Page<Product> getActiveProducts(Pageable pageable){

        return productRepository.findByActiveTrue(pageable);
    }

   public Product getById(Long id){

    Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));

    try {
        PricePredictionRequest mlRequest = new PricePredictionRequest();

        mlRequest.setCategory(product.getCategory());
        mlRequest.setBrand(product.getBrand());
        mlRequest.setBasePrice(product.getPrice());
        mlRequest.setDemandScore(0.7);
        mlRequest.setRating(4.0);

        Double predicted = mlService.getPredictedPrice(mlRequest);

        if(predicted == null || predicted <= 0 || predicted.isNaN()){
            predicted = product.getPrice();
        }

        product.setPredictedPrice(predicted);

    } catch(Exception e){
        System.out.println("ML FETCH ERROR: " + e.getMessage());
        product.setPredictedPrice(product.getPrice());
    }

    return product;
}
    public Page<Product> search(String keyword, Pageable pageable){

        return productRepository.findByNameContainingIgnoreCase(keyword,pageable);
    }

    public Page<Product> filterByPrice(double min,double max, Pageable pageable){

        return productRepository.findByPriceBetween(min,max,pageable);
    }

    public void deleteProduct(Long id){

        productRepository.deleteById(id);
    }

    public Product updateStock(Long id,int stock){

        Product p = getById(id);

        p.setStock(stock);

        return productRepository.save(p);
    }
public List<Product> getRecommendedProducts(Long productId){

    Product product = getById(productId);

    List<Map<String,String>> mlResults =
            mlService.getRecommendations(
    product.getName() + " " +
    product.getCategory() + " " +
    product.getBrand() + " " +
    product.getDescription()
);
    if(mlResults == null || mlResults.isEmpty()){
        return List.of();
    }

    List<Product> results = new ArrayList<>();

    for(Map<String,String> rec : mlResults){

        String name = rec.get("name");

        productRepository.findByNameIgnoreCase(name)
                .ifPresent(results::add);
    }

    return results.stream()
            .filter(p -> !p.getId().equals(productId))
            .distinct()
            .limit(5)
            .toList();
}
    
}