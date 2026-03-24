package com.example.specdriven.movie;

import jakarta.persistence.*;

@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200, unique = true)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private Integer durationMinutes;

    private String posterFileName;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
    public String getPosterFileName() { return posterFileName; }
    public void setPosterFileName(String posterFileName) { this.posterFileName = posterFileName; }
}
