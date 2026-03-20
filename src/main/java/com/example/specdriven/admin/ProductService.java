package com.example.specdriven.admin;

import com.example.specdriven.checkout.Product;
import com.example.specdriven.checkout.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Transactional
    public Product save(Product product) {
        // Check for duplicate barcode
        productRepository.findByBarcode(product.getBarcode()).ifPresent(existing -> {
            if (!existing.getId().equals(product.getId())) {
                throw new IllegalArgumentException("A product with barcode '" + product.getBarcode() + "' already exists.");
            }
        });
        return productRepository.save(product);
    }

    @Transactional
    public void delete(Product product) {
        productRepository.delete(product);
    }
}
