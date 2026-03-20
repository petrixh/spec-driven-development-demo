package com.example.specdriven.admin;

import com.example.specdriven.checkout.Product;
import com.example.specdriven.checkout.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void canSaveNewProduct() {
        Product product = new Product("SVC_TEST_001", "Service Test Product", new BigDecimal("5.99"));
        Product saved = productService.save(product);
        assertNotNull(saved.getId());
        assertEquals("Service Test Product", saved.getName());
    }

    @Test
    void duplicateBarcodeThrowsException() {
        // Coca-Cola barcode already exists from sample data
        Product duplicate = new Product("5000159484695", "Fake Product", new BigDecimal("1.00"));
        assertThrows(IllegalArgumentException.class, () -> productService.save(duplicate));
    }

    @Test
    void canDeleteProduct() {
        Product product = new Product("SVC_DEL_001", "Delete Me", new BigDecimal("1.00"));
        Product saved = productService.save(product);
        assertNotNull(saved.getId());

        productService.delete(saved);
        assertTrue(productRepository.findByBarcode("SVC_DEL_001").isEmpty());
    }

    @Test
    void findAllReturnsProducts() {
        assertFalse(productService.findAll().isEmpty());
    }
}
