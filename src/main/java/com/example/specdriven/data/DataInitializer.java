package com.example.specdriven.data;

import com.example.specdriven.product.Product;
import com.example.specdriven.product.ProductRepository;
import com.example.specdriven.stock.StockEvent;
import com.example.specdriven.stock.StockEventRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final StockEventRepository stockEventRepository;

    public DataInitializer(ProductRepository productRepository, StockEventRepository stockEventRepository) {
        this.productRepository = productRepository;
        this.stockEventRepository = stockEventRepository;
    }

    @Override
    public void run(String... args) {
        if (productRepository.count() > 0) {
            return;
        }

        Product usbCable = createProduct("USB-C Cable", "CABLE-USB-C", "Cables", "9.99", 20, 150);
        Product mouse = createProduct("Wireless Mouse", "MOUSE-WL-01", "Peripherals", "29.99", 10, 45);
        Product keyboard = createProduct("Mechanical Keyboard", "KB-MECH-01", "Peripherals", "79.99", 5, 12);
        Product monitor = createProduct("27\" Monitor", "MON-27-4K", "Displays", "349.99", 3, 8);
        Product stand = createProduct("Laptop Stand", "STAND-LP-01", "Accessories", "24.99", 15, 60);
        Product hdmi = createProduct("HDMI Cable 2m", "CABLE-HDMI-2M", "Cables", "12.99", 25, 200);
        Product webcam = createProduct("Webcam HD", "CAM-HD-01", "Peripherals", "49.99", 8, 3);
        createProduct("USB Hub 4-port", "HUB-USB4", "Accessories", "19.99", 10, 0);
        Product lamp = createProduct("Desk Lamp LED", "LAMP-LED-01", "Accessories", "34.99", 7, 22);
        Product ethernet = createProduct("Ethernet Cable 5m", "CABLE-ETH-5M", "Cables", "7.99", 30, 85);

        LocalDateTime now = LocalDateTime.now();
        createEvent(usbCable, StockEvent.Type.RECEIVED, 50, "PO-2024-001", now.minusHours(2));
        createEvent(mouse, StockEvent.Type.RECEIVED, 20, "PO-2024-002", now.minusHours(5));
        createEvent(keyboard, StockEvent.Type.ADJUSTMENT, -3, "Damaged in transit", now.minusHours(8));
        createEvent(monitor, StockEvent.Type.RECEIVED, 5, "PO-2024-003", now.minusDays(1));
        createEvent(stand, StockEvent.Type.RECEIVED, 30, "PO-2024-004", now.minusDays(1).minusHours(3));
        createEvent(hdmi, StockEvent.Type.RECEIVED, 100, "PO-2024-005", now.minusDays(2));
        createEvent(webcam, StockEvent.Type.ADJUSTMENT, -5, "Defective batch returned", now.minusDays(2).minusHours(6));
        createEvent(lamp, StockEvent.Type.RECEIVED, 10, "PO-2024-006", now.minusDays(3));
        createEvent(ethernet, StockEvent.Type.RECEIVED, 40, "PO-2024-007", now.minusDays(3).minusHours(4));
        createEvent(usbCable, StockEvent.Type.ADJUSTMENT, -10, "Inventory count correction", now.minusDays(4));
    }

    private Product createProduct(String name, String sku, String category, String price, int reorderPoint, int stock) {
        Product product = new Product();
        product.setName(name);
        product.setSku(sku);
        product.setCategory(category);
        product.setUnitPrice(new BigDecimal(price));
        product.setReorderPoint(reorderPoint);
        product.setCurrentStock(stock);
        return productRepository.save(product);
    }

    private void createEvent(Product product, StockEvent.Type type, int quantity, String reason, LocalDateTime timestamp) {
        StockEvent event = new StockEvent();
        event.setProduct(product);
        event.setType(type);
        event.setQuantity(quantity);
        event.setReason(reason);
        event.setTimestamp(timestamp);
        stockEventRepository.save(event);
    }
}
