package com.example.specdriven.catalog.application;

public record MovieCardDto(
        Long id,
        String title,
        Integer durationMinutes,
        String posterUrl,
        boolean hasUpcomingShows) {
}
