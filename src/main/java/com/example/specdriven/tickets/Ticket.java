package com.example.specdriven.tickets;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    private TransitMode transitMode;
    @Enumerated(EnumType.STRING)
    private TicketType ticketType;
    private BigDecimal price;

    public Ticket() {}

    public Ticket(String name, String description, TransitMode transitMode, TicketType ticketType, BigDecimal price) {
        this.name = name;
        this.description = description;
        this.transitMode = transitMode;
        this.ticketType = ticketType;
        this.price = price;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public TransitMode getTransitMode() { return transitMode; }
    public TicketType getTicketType() { return ticketType; }
    public BigDecimal getPrice() { return price; }
}
