package com.example.specdriven.data;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

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

        var products = List.of(
            new Product("Widget A", "WDG-001", "Widgets", 9.99, 10, 50),
            new Product("Widget B", "WDG-002", "Widgets", 14.99, 5, 3),
            new Product("Gadget X", "GDG-001", "Gadgets", 24.99, 8, 0),
            new Product("Gadget Y", "GDG-002", "Gadgets", 19.99, 15, 20),
            new Product("Tool Alpha", "TL-001", "Tools", 34.99, 3, 12),
            new Product("Tool Beta", "TL-002", "Tools", 29.99, 5, 5),
            new Product("Part 101", "PT-001", "Parts", 4.99, 20, 100),
            new Product("Part 202", "PT-002", "Parts", 7.99, 10, 0)
        );

        productRepository.saveAll(products);
    }
}
