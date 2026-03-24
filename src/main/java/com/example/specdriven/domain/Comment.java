package com.example.specdriven.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;

import java.time.LocalDateTime;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    private LocalDateTime createdDate;

    @ManyToOne
    private Ticket ticket;

    @ManyToOne
    private User author;

    protected Comment() {
    }

    public Comment(String text, Ticket ticket, User author) {
        this.text = text;
        this.ticket = ticket;
        this.author = author;
    }

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public User getAuthor() {
        return author;
    }
}
