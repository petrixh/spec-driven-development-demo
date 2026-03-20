package com.example.specdriven.catalog.application;

public record MovieAdminRowDto(
        long id,
        String title,
        int durationMinutes,
        long scheduledShows,
        String posterFileName
) {
}
