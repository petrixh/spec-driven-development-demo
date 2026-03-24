package com.example.specdriven.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsBySku(String sku);

    boolean existsBySkuAndIdNot(String sku, Long id);

    @Query("SELECT DISTINCT p.category FROM Product p WHERE p.category IS NOT NULL ORDER BY p.category")
    List<String> findDistinctCategories();

    @Query("SELECT p FROM Product p WHERE p.currentStock > 0 AND p.currentStock <= p.reorderPoint " +
           "ORDER BY (p.currentStock * 1.0 / CASE WHEN p.reorderPoint = 0 THEN 1 ELSE p.reorderPoint END) ASC")
    List<Product> findLowStockProducts();

    long countByCurrentStock(int currentStock);

    @Query("SELECT p FROM Product p WHERE " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(p.sku) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Product> searchByNameOrSku(String search);
}
