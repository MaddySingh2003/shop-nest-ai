package com.ecommerce.ecommerce_backend.controller;

import com.ecommerce.ecommerce_backend.dto.ProductRequest;
import com.ecommerce.ecommerce_backend.model.Product;
import com.ecommerce.ecommerce_backend.service.ProductService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public Page<Product> getProducts(
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="12") int size){

        Pageable pageable = PageRequest.of(page,size);

        return productService.getActiveProducts(pageable);
    }

    @GetMapping("/id/{id}")
    public Product getById(@PathVariable Long id){
        return productService.getById(id);
    }

    @GetMapping("/search")
    public Page<Product> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="5") int size){

        Pageable pageable = PageRequest.of(page,size);

        return productService.search(keyword,pageable);
    }

    @GetMapping("/filter")
    public Page<Product> filterPrice(
            @RequestParam double min,
            @RequestParam double max,
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="5") int size){

        Pageable pageable = PageRequest.of(page,size);

        return productService.filterByPrice(min,max,pageable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<Product> add(@RequestBody ProductRequest request){

        return ResponseEntity.ok(productService.addProduct(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/stock/{id}")
    public ResponseEntity<Product> updateStock(
            @PathVariable Long id,
            @RequestParam int stock){

        return ResponseEntity.ok(productService.updateStock(id,stock));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){

        productService.deleteProduct(id);

        return ResponseEntity.ok("Product deleted");
    }

    @GetMapping("/{id}/recommendations")
    public List<Product> recommend(@PathVariable Long id){

        return productService.getRecommendedProducts(id);
    }
}