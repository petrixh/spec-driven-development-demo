package com.example.specdriven.tickets;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.math.BigDecimal;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransitMode transitMode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketType ticketType;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    public enum TransitMode {
        BUS, TRAIN, METRO, FERRY
    }

    public enum TicketType {
        SINGLE_RIDE, DAY_PASS
    }

    public Ticket() {
    }

    public Ticket(Long id, String name, String description, TransitMode transitMode, TicketType ticketType, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.transitMode = transitMode;
        this.ticketType = ticketType;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TransitMode getTransitMode() {
        return transitMode;
    }

    public void setTransitMode(TransitMode transitMode) {
        this.transitMode = transitMode;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
