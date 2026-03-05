package com.ecommerce.ecommerce_backend.service;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ecommerce.ecommerce_backend.model.Product;
import com.ecommerce.ecommerce_backend.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExternalProductService {

    private final ProductRepository productRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Product> fetchProducts() {

        String url = "https://dummyjson.com/products";
@SuppressWarnings("unchecked")
        Map<String, Object> response =
                restTemplate.getForObject(url, Map.class);

        if (response == null || !response.containsKey("products")) {
            return Collections.emptyList();
        }

        List<?> rawProducts = (List<?>) response.get("products");

        List<Product> result = new ArrayList<>();

        for (Object obj : rawProducts) {

            Map<?, ?> p = (Map<?, ?>) obj;

            List<?> images = (List<?>) p.get("images");

            String imageUrl = (images != null && !images.isEmpty())
                    ? images.get(0).toString()
                    : "";

            Product product = Product.builder()
                    .name((String) p.get("title"))
                    .price(Double.parseDouble(p.get("price").toString()))
                    .description((String) p.get("description"))
                    .imageUrl(imageUrl)
                    .brand((String) p.get("brand"))
                    .category((String) p.get("category"))
                    .stock(Integer.parseInt(p.get("stock").toString()))
                    .active(true)
                    .build();

            result.add(product);
        }

        return result;
    }
public void syncProducts() {

    List<Product> products = fetchProducts();

    for(Product p : products){

        Optional<Product> existing =
                productRepository.findByNameAndBrand(p.getName(), p.getBrand());

        if(existing.isPresent()){

            Product db = existing.get();
            db.setPrice(p.getPrice());
            db.setStock(p.getStock());
            db.setImageUrl(p.getImageUrl());
            db.setCategory(p.getCategory());

            productRepository.save(db);

        } else {

            productRepository.save(p);

        }
    }
}
}