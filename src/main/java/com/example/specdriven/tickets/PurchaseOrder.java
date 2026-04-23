package com.example.specdriven.tickets;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "purchase_order")
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID confirmationCode;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(nullable = false, length = 4)
    private String cardLastFour;

    @Column(nullable = false)
    private LocalDateTime purchasedAt;

    public PurchaseOrder() {
    }

    public PurchaseOrder(Long id, UUID confirmationCode, Ticket ticket, Integer quantity, BigDecimal totalPrice, String cardLastFour, LocalDateTime purchasedAt) {
        this.id = id;
        this.confirmationCode = confirmationCode;
        this.ticket = ticket;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.cardLastFour = cardLastFour;
        this.purchasedAt = purchasedAt;
    }

    public static PurchaseOrder create(Ticket ticket, Integer quantity, String cardLastFour) {
        PurchaseOrder order = new PurchaseOrder();
        order.setConfirmationCode(UUID.randomUUID());
        order.setTicket(ticket);
        order.setQuantity(quantity);
        order.setTotalPrice(ticket.getPrice().multiply(BigDecimal.valueOf(quantity)));
        order.setCardLastFour(cardLastFour);
        order.setPurchasedAt(LocalDateTime.now());
        return order;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(UUID confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCardLastFour() {
        return cardLastFour;
    }

    public void setCardLastFour(String cardLastFour) {
        this.cardLastFour = cardLastFour;
    }

    public LocalDateTime getPurchasedAt() {
        return purchasedAt;
    }

    public void setPurchasedAt(LocalDateTime purchasedAt) {
        this.purchasedAt = purchasedAt;
    }
}
