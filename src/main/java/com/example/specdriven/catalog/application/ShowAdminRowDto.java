package com.example.specdriven.catalog.application;

import java.time.LocalDateTime;

public record ShowAdminRowDto(
        Long id,
        Long movieId,
        String movieTitle,
        LocalDateTime dateTime,
        Long screeningRoomId,
        String screeningRoomName,
        long ticketsSold,
        int totalCapacity
) {
}
