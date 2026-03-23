package com.example.specdriven.data;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 4000, nullable = false)
    private String text;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @ManyToOne(optional = false)
    private User author;

    @ManyToOne(optional = false)
    private Ticket ticket;

    protected Comment() {}

    public Comment(String text, User author, Ticket ticket) {
        this.text = text;
        this.author = author;
        this.ticket = ticket;
        this.createdDate = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getText() { return text; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public User getAuthor() { return author; }
    public Ticket getTicket() { return ticket; }
}
