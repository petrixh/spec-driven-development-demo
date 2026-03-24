package com.example.specdriven.receive;

import com.example.specdriven.data.Product;
import com.example.specdriven.data.ProductRepository;
import com.example.specdriven.data.StockEvent;
import com.example.specdriven.data.StockEventRepository;
import com.example.specdriven.data.StockEventType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReceiveStockService {

    private final ProductRepository productRepository;
    private final StockEventRepository stockEventRepository;

    public ReceiveStockService(ProductRepository productRepository, StockEventRepository stockEventRepository) {
        this.productRepository = productRepository;
        this.stockEventRepository = stockEventRepository;
    }

    @Transactional
    public Product receiveStock(Product product, int quantity, String reference) {
        product.setCurrentStock(product.getCurrentStock() + quantity);
        productRepository.save(product);

        StockEvent event = new StockEvent(StockEventType.RECEIVED, quantity, reference, product);
        stockEventRepository.save(event);

        return product;
    }
}
