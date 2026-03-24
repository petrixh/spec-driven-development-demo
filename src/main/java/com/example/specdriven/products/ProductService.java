package com.example.specdriven.products;

import com.example.specdriven.data.Product;
import com.example.specdriven.data.ProductRepository;
import com.example.specdriven.data.StockEventRepository;
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

    public boolean isSkuDuplicate(String sku, Long excludeId) {
        if (excludeId == null) {
            return productRepository.existsBySku(sku);
        }
        return productRepository.existsBySkuAndIdNot(sku, excludeId);
    }

    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }

    public boolean hasStockEvents(Long productId) {
        return stockEventRepository.existsByProductId(productId);
    }

    @Transactional
    public void delete(Product product) {
        productRepository.delete(product);
    }
}
