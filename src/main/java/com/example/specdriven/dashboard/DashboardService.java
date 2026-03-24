package com.example.specdriven.dashboard;

import com.example.specdriven.data.Product;
import com.example.specdriven.data.ProductRepository;
import com.example.specdriven.data.StockEvent;
import com.example.specdriven.data.StockEventRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DashboardService {

    private final ProductRepository productRepository;
    private final StockEventRepository stockEventRepository;

    public DashboardService(ProductRepository productRepository, StockEventRepository stockEventRepository) {
        this.productRepository = productRepository;
        this.stockEventRepository = stockEventRepository;
    }

    public long getTotalProducts() {
        return productRepository.count();
    }

    public double getTotalStockValue() {
        return productRepository.findAll().stream()
                .mapToDouble(p -> p.getCurrentStock() * p.getUnitPrice())
                .sum();
    }

    public long getLowStockCount() {
        return productRepository.findAll().stream()
                .filter(p -> p.getCurrentStock() > 0 && p.getCurrentStock() <= p.getReorderPoint())
                .count();
    }

    public long getOutOfStockCount() {
        return productRepository.countByCurrentStock(0);
    }

    public List<Product> getLowStockAlerts() {
        return productRepository.findLowStockProducts().stream()
                .limit(10)
                .toList();
    }

    public List<StockEvent> getRecentActivity() {
        return stockEventRepository.findTop10ByOrderByTimestampDesc();
    }
}
