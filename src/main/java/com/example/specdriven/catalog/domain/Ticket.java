package com.example.specdriven.catalog.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"show_id", "seat_row", "seat_number"}))
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer seatRow;

    private Integer seatNumber;

    private String customerName;

    private String customerEmail;

    private LocalDateTime purchasedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Show show;

    protected Ticket() {
    }

    public Ticket(Integer seatRow, Integer seatNumber, String customerName, String customerEmail,
            LocalDateTime purchasedAt, Show show) {
        this.seatRow = seatRow;
        this.seatNumber = seatNumber;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.purchasedAt = purchasedAt;
        this.show = show;
    }

    public Long getId() {
        return id;
    }

    public Integer getSeatRow() {
        return seatRow;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public LocalDateTime getPurchasedAt() {
        return purchasedAt;
    }

    public Show getShow() {
        return show;
    }
}
