package com.example.specdriven.adjust;

import com.example.specdriven.data.Product;
import com.example.specdriven.data.ProductRepository;
import com.example.specdriven.data.StockEvent;
import com.example.specdriven.data.StockEventRepository;
import com.example.specdriven.data.StockEventType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdjustStockService {

    private final ProductRepository productRepository;
    private final StockEventRepository stockEventRepository;

    public AdjustStockService(ProductRepository productRepository, StockEventRepository stockEventRepository) {
        this.productRepository = productRepository;
        this.stockEventRepository = stockEventRepository;
    }

    @Transactional
    public Product adjustStock(Product product, int adjustment, String reason) {
        int newStock = product.getCurrentStock() + adjustment;
        if (newStock < 0) {
            throw new IllegalArgumentException(
                    "Adjustment would bring stock below zero (current: %d, adjustment: %d)"
                            .formatted(product.getCurrentStock(), adjustment));
        }

        int oldStock = product.getCurrentStock();
        product.setCurrentStock(newStock);
        productRepository.save(product);

        StockEvent event = new StockEvent(StockEventType.ADJUSTMENT, adjustment, reason, product);
        stockEventRepository.save(event);

        return product;
    }
}
