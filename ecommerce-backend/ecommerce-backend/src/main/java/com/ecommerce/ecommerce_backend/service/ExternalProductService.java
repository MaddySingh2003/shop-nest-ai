package com.ecommerce.ecommerce_backend.service;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ecommerce.ecommerce_backend.model.Product;
import com.ecommerce.ecommerce_backend.repository.ProductRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExternalProductService {

    private final ProductRepository productRepository;
    private final RestTemplate restTemplate = new RestTemplate();
public void syncProducts(){

    int limit = 30;
    int skip = 0;
    boolean hasMore = true;

    while(hasMore){

        String url =
        "https://dummyjson.com/products?limit=" + limit + "&skip=" + skip;

        Map<String,Object> response =
                restTemplate.getForObject(url,Map.class);

        if(response == null){
            break;
        }

        List<?> rawProducts =
                (List<?>) response.get("products");

        if(rawProducts == null || rawProducts.isEmpty()){
            break;
        }

        for(Object obj : rawProducts){

            Map<?,?> p = (Map<?,?>) obj;

            String name = (String)p.get("title");
            String brand = (String)p.get("brand");

            if(brand == null){
                brand = "Unknown";
            }

            Optional<Product> existing =
                    productRepository.findByNameAndBrand(name,brand);

            if(existing.isPresent()){
                continue;
            }

            List<?> images = (List<?>)p.get("images");

            String imageUrl =
                    images!=null && !images.isEmpty()
                    ? images.get(0).toString()
                    : "";

            Product product = Product.builder()
                    .name(name)
                    .brand(brand)
                    .description((String)p.get("description"))
                    .price(Double.parseDouble(p.get("price").toString()))
                    .category((String)p.get("category"))
                    .stock(Integer.parseInt(p.get("stock").toString()))
                    .imageUrl(imageUrl)
                    .externalProduct(true)
                    .active(true)
                    .build();

            productRepository.save(product);
        }

        skip += limit;

        Integer total = (Integer) response.get("total");

        if(skip >= total){
            hasMore = false;
        }
    }
}
@PostConstruct
public void init(){
    if(productRepository.countByExternalProductTrue() == 0){
        syncProducts();
    }
}
}