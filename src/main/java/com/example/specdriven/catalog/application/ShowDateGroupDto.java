package com.example.specdriven.catalog.application;

import java.util.List;

public record ShowDateGroupDto(
        String date,
        List<ShowTimeDto> showtimes) {
}
