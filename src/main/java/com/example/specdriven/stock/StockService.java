package com.example.specdriven.stock;

import com.example.specdriven.product.Product;
import com.example.specdriven.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class StockService {

    private final ProductRepository productRepository;
    private final StockEventRepository stockEventRepository;

    public StockService(ProductRepository productRepository, StockEventRepository stockEventRepository) {
        this.productRepository = productRepository;
        this.stockEventRepository = stockEventRepository;
    }

    @Transactional
    public Product receiveStock(Product product, int quantity, String reference) {
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }

        product.setCurrentStock(product.getCurrentStock() + quantity);
        productRepository.save(product);

        StockEvent event = new StockEvent();
        event.setProduct(product);
        event.setType(StockEvent.Type.RECEIVED);
        event.setQuantity(quantity);
        event.setReason(reference);
        event.setTimestamp(LocalDateTime.now());
        stockEventRepository.save(event);

        return product;
    }

    @Transactional
    public Product adjustStock(Product product, int quantity, String reason) {
        if (quantity == 0) {
            throw new IllegalArgumentException("Adjustment quantity must be non-zero");
        }
        if (reason == null || reason.trim().length() < 3) {
            throw new IllegalArgumentException("Reason must be at least 3 characters");
        }
        int newStock = product.getCurrentStock() + quantity;
        if (newStock < 0) {
            throw new IllegalArgumentException(
                    "Adjustment would bring stock below zero (current: "
                            + product.getCurrentStock() + ", adjustment: " + quantity + ")");
        }

        int oldStock = product.getCurrentStock();
        product.setCurrentStock(newStock);
        productRepository.save(product);

        StockEvent event = new StockEvent();
        event.setProduct(product);
        event.setType(StockEvent.Type.ADJUSTMENT);
        event.setQuantity(quantity);
        event.setReason(reason);
        event.setTimestamp(LocalDateTime.now());
        stockEventRepository.save(event);

        return product;
    }
}
