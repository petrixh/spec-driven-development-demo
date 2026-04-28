package com.example.specdriven.tickets;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "purchase_orders")
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID confirmationCode;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(nullable = false, length = 4)
    private String cardLastFour;

    @Column(nullable = false)
    private LocalDateTime purchasedAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    public PurchaseOrder() {
    }

    public PurchaseOrder(Ticket ticket, int quantity, BigDecimal totalPrice, String cardLastFour) {
        this.ticket = ticket;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.cardLastFour = cardLastFour;
        this.confirmationCode = UUID.randomUUID();
        this.purchasedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public UUID getConfirmationCode() {
        return confirmationCode;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public String getCardLastFour() {
        return cardLastFour;
    }

    public LocalDateTime getPurchasedAt() {
        return purchasedAt;
    }

    public Ticket getTicket() {
        return ticket;
    }
}
