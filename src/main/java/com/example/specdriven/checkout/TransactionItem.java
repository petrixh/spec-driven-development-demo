package com.example.specdriven.checkout;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
public class TransactionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;

    private BigDecimal priceAtScan;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Transaction transaction;

    protected TransactionItem() {}

    public TransactionItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.priceAtScan = product.getPrice();
    }

    public Long getId() { return id; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public BigDecimal getPriceAtScan() { return priceAtScan; }
    public Product getProduct() { return product; }
    public Transaction getTransaction() { return transaction; }
    public void setTransaction(Transaction transaction) { this.transaction = transaction; }

    public BigDecimal getLineTotal() {
        return priceAtScan.multiply(BigDecimal.valueOf(quantity));
    }
}
