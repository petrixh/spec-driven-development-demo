package com.example.specdriven.tickets;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransitMode transitMode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketType ticketType;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    public Ticket() {
    }

    public Ticket(String name, String description, TransitMode transitMode, TicketType ticketType, BigDecimal price) {
        this.name = name;
        this.description = description;
        this.transitMode = transitMode;
        this.ticketType = ticketType;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TransitMode getTransitMode() {
        return transitMode;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
