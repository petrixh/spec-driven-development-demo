package com.example.specdriven.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class LendingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Book book;

    @ManyToOne(optional = false)
    private Patron patron;

    private LocalDate checkoutDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    private LendingStatus status;

    private BigDecimal lateFee;
    private String damageAssessment;
    private String notes;

    protected LendingRecord() {
    }

    public LendingRecord(Book book, Patron patron, LocalDate checkoutDate, LocalDate dueDate) {
        this.book = book;
        this.patron = patron;
        this.checkoutDate = checkoutDate;
        this.dueDate = dueDate;
        this.status = LendingStatus.CHECKED_OUT;
    }

    public Long getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public Patron getPatron() {
        return patron;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public LendingStatus getStatus() {
        return status;
    }

    public void setStatus(LendingStatus status) {
        this.status = status;
    }

    public BigDecimal getLateFee() {
        return lateFee;
    }

    public void setLateFee(BigDecimal lateFee) {
        this.lateFee = lateFee;
    }

    public String getDamageAssessment() {
        return damageAssessment;
    }

    public void setDamageAssessment(String damageAssessment) {
        this.damageAssessment = damageAssessment;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isOverdue() {
        return status == LendingStatus.CHECKED_OUT && LocalDate.now().isAfter(dueDate);
    }

    public long daysOverdue() {
        if (!isOverdue()) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }
}
