package com.example.specdriven.data;

import com.example.specdriven.product.Product;
import com.example.specdriven.product.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;

    public DataInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        if (productRepository.count() > 0) {
            return;
        }

        createProduct("USB-C Cable", "CABLE-USB-C", "Cables", "9.99", 20, 150);
        createProduct("Wireless Mouse", "MOUSE-WL-01", "Peripherals", "29.99", 10, 45);
        createProduct("Mechanical Keyboard", "KB-MECH-01", "Peripherals", "79.99", 5, 12);
        createProduct("27\" Monitor", "MON-27-4K", "Displays", "349.99", 3, 8);
        createProduct("Laptop Stand", "STAND-LP-01", "Accessories", "24.99", 15, 60);
        createProduct("HDMI Cable 2m", "CABLE-HDMI-2M", "Cables", "12.99", 25, 200);
        createProduct("Webcam HD", "CAM-HD-01", "Peripherals", "49.99", 8, 3);
        createProduct("USB Hub 4-port", "HUB-USB4", "Accessories", "19.99", 10, 0);
        createProduct("Desk Lamp LED", "LAMP-LED-01", "Accessories", "34.99", 7, 22);
        createProduct("Ethernet Cable 5m", "CABLE-ETH-5M", "Cables", "7.99", 30, 85);
    }

    private void createProduct(String name, String sku, String category, String price, int reorderPoint, int stock) {
        Product product = new Product();
        product.setName(name);
        product.setSku(sku);
        product.setCategory(category);
        product.setUnitPrice(new BigDecimal(price));
        product.setReorderPoint(reorderPoint);
        product.setCurrentStock(stock);
        productRepository.save(product);
    }
}
