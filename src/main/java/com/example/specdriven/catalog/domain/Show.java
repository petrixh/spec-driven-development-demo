package com.example.specdriven.catalog.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateTime;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private ScreeningRoom screeningRoom;

    protected Show() {
    }

    public Show(LocalDateTime dateTime, Movie movie, ScreeningRoom screeningRoom) {
        this.dateTime = dateTime;
        this.movie = movie;
        this.screeningRoom = screeningRoom;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Movie getMovie() {
        return movie;
    }

    public ScreeningRoom getScreeningRoom() {
        return screeningRoom;
    }

    public void update(LocalDateTime dateTime, ScreeningRoom screeningRoom) {
        this.dateTime = dateTime;
        this.screeningRoom = screeningRoom;
    }
}
