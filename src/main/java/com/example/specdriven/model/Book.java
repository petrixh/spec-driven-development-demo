package com.example.specdriven.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private String isbn;
    private String callNumber;
    private BigDecimal lateFeePerDay;

    protected Book() {
    }

    public Book(String title, String author, String isbn, String callNumber, BigDecimal lateFeePerDay) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.callNumber = callNumber;
        this.lateFeePerDay = lateFeePerDay;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public BigDecimal getLateFeePerDay() {
        return lateFeePerDay;
    }

    public void setLateFeePerDay(BigDecimal lateFeePerDay) {
        this.lateFeePerDay = lateFeePerDay;
    }
}
