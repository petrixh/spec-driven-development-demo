package com.example.specdriven.show;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"show_id", "seat_row", "seat_number"}))
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int seatRow;

    @Column(nullable = false)
    private int seatNumber;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String customerEmail;

    @Column(nullable = false)
    private LocalDateTime purchasedAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "show_id")
    private Show show;

    protected Ticket() {
    }

    public Ticket(int seatRow, int seatNumber, String customerName, String customerEmail, Show show) {
        this.seatRow = seatRow;
        this.seatNumber = seatNumber;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.show = show;
        this.purchasedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public int getSeatRow() { return seatRow; }
    public int getSeatNumber() { return seatNumber; }
    public String getCustomerName() { return customerName; }
    public String getCustomerEmail() { return customerEmail; }
    public LocalDateTime getPurchasedAt() { return purchasedAt; }
    public Show getShow() { return show; }
}
