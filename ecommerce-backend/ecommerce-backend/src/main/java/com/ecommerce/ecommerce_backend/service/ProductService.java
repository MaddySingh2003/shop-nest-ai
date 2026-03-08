package com.ecommerce.ecommerce_backend.service;

import com.ecommerce.ecommerce_backend.dto.PricePredictionRequest;
import com.ecommerce.ecommerce_backend.dto.ProductRequest;
import com.ecommerce.ecommerce_backend.model.Product;
import com.ecommerce.ecommerce_backend.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
                .brand(request.getBrand())
                .category(request.getCategory())
                .price(request.getPrice())
                .stock(request.getStock())
                .imageUrl(request.getImageUrl())
                .build();

        try{

            PricePredictionRequest mlRequest = new PricePredictionRequest();
            mlRequest.setCategory(product.getCategory());
            mlRequest.setBrand(product.getBrand());
            mlRequest.setBase_price(product.getPrice());
            mlRequest.setDemand_score(0.7);
            mlRequest.setRating(4.0);

            Double predicted=mlService.getPredictedPrice(mlRequest);
            if(predicted==null || predicted<=0||predicted.isNaN()){
                predicted=product.getPrice();
            }

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

        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
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
                mlService.getRecommendations(product.getDescription());

        if(mlResults == null || mlResults.isEmpty()){
            return List.of();
        }

        String category = mlResults.get(0).get("category");
        String brand = mlResults.get(0).get("brand");

        return productRepository
                .findByCategoryAndBrand(category,brand)
                .stream()
                .filter(p -> !p.getId().equals(productId))
                .limit(5)
                .toList();
    }
}