package com.ecommerce.ecommerce_backend.controller;

import com.ecommerce.ecommerce_backend.dto.ProductRequest;
import com.ecommerce.ecommerce_backend.model.Product;
import com.ecommerce.ecommerce_backend.repository.ProductRepository;
import com.ecommerce.ecommerce_backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;

   

   @GetMapping
public Page<Product> getAllProducts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "asc") String direction
) {
    Sort sort = direction.equalsIgnoreCase("desc")
            ? Sort.by(sortBy).descending()
            : Sort.by(sortBy).ascending();

    Pageable pageable = PageRequest.of(page, size, sort);
    return productRepository.findAll(pageable);
}


@GetMapping("/search")
public Page<Product> search(
        @RequestParam String keyword,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size
){
    Pageable pageable = PageRequest.of(page, size);
    return productService.search(keyword, pageable);
}


@GetMapping("/filter")
public Page<Product> filterPrice(
        @RequestParam double min,
        @RequestParam double max,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size
){
    Pageable pageable = PageRequest.of(page, size);
    return productService.filterByPrice(min, max, pageable);
}

  @GetMapping("/all")
    public List<Product> getProducts(){
        return productService.getAll();
    }



    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id){
        return productService.getById(id);
    }

     @PostMapping("/add")
    public ResponseEntity<Product> add(@RequestBody ProductRequest request){
        return ResponseEntity.ok(productService.addProduct(request));
    }

    @PutMapping("/stock/{productId}")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<?> updateStock(
        @PathVariable Long productId,
        @RequestParam int stock){

    Product p = productService.updateStock(productId, stock);
    return ResponseEntity.ok(p);
}

    @PutMapping("/update/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id,
                                          @RequestBody ProductRequest request){
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product Deleted");
    }
}
