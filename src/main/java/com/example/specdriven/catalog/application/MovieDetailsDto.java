package com.example.specdriven.catalog.application;

import java.util.List;

public record MovieDetailsDto(
        Long id,
        String title,
        String description,
        Integer durationMinutes,
        String posterUrl,
        List<ShowDateGroupDto> showDates) {
}
