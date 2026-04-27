package com.example.specdriven.tickets;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "purchase_order")
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID confirmationCode;
    private Integer quantity;
    private BigDecimal totalPrice;
    private String cardLastFour;
    private LocalDateTime purchasedAt;
    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    public PurchaseOrder() {}

    public Long getId() { return id; }
    public UUID getConfirmationCode() { return confirmationCode; }
    public void setConfirmationCode(UUID confirmationCode) { this.confirmationCode = confirmationCode; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    public String getCardLastFour() { return cardLastFour; }
    public void setCardLastFour(String cardLastFour) { this.cardLastFour = cardLastFour; }
    public LocalDateTime getPurchasedAt() { return purchasedAt; }
    public void setPurchasedAt(LocalDateTime purchasedAt) { this.purchasedAt = purchasedAt; }
    public Ticket getTicket() { return ticket; }
    public void setTicket(Ticket ticket) { this.ticket = ticket; }
}
