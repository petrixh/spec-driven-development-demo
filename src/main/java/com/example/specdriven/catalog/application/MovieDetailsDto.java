package com.example.specdriven.catalog.application;

public record MovieDetailsDto(
        Long id,
        String title,
        String description,
        Integer durationMinutes,
        String posterUrl
) {
}
