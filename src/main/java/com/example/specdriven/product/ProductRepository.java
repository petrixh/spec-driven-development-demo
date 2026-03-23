package com.example.specdriven.product;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySku(String sku);

    List<Product> findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(String name, String sku);

    List<Product> findByCategory(String category);

    List<Product> findByCurrentStockLessThanEqualAndReorderPointGreaterThan(int stock, int reorderPoint);
}
