package com.example.specdriven.product;

import com.example.specdriven.stock.StockEventRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final StockEventRepository stockEventRepository;

    public ProductService(ProductRepository productRepository, StockEventRepository stockEventRepository) {
        this.productRepository = productRepository;
        this.stockEventRepository = stockEventRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Transactional
    public Product save(Product product) {
        if (product.getReorderPoint() < 0) {
            throw new IllegalArgumentException("Reorder point must be non-negative");
        }
        if (product.getUnitPrice() == null || product.getUnitPrice().signum() <= 0) {
            throw new IllegalArgumentException("Unit price must be positive");
        }
        // Check SKU uniqueness
        productRepository.findBySku(product.getSku()).ifPresent(existing -> {
            if (!existing.getId().equals(product.getId())) {
                throw new DataIntegrityViolationException("SKU already exists: " + product.getSku());
            }
        });
        return productRepository.save(product);
    }

    @Transactional
    public void delete(Product product) {
        if (stockEventRepository.existsByProductId(product.getId())) {
            throw new IllegalStateException("Cannot delete product with existing stock events");
        }
        productRepository.delete(product);
    }

    public boolean hasStockEvents(Product product) {
        return product.getId() != null && stockEventRepository.existsByProductId(product.getId());
    }
}
