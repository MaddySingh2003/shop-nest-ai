package com.ecommerce.ecommerce_backend.repository;
import com.ecommerce.ecommerce_backend.model.Product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ProductRepository extends JpaRepository<Product, Long> {


    Page<Product> findByNameContainingIgnoreCase(String name ,Pageable pageable);
 Page<Product> findByPriceBetween(double min, double max, Pageable pageable);
 List<Product> findByCategoryAndBrand(String category, String brand);
 Optional<Product> findByNameAndBrand(String name, String brand);
 Page<Product>findByActiveTrue(Pageable pageable);

}
