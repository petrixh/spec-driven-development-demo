package com.example.specdriven.catalog.application;

import java.util.List;

public record TicketConfirmationDto(
        long showId,
        long movieId,
        String movieTitle,
        String screeningRoomName,
        String showDate,
        String showTime,
        String customerName,
        String customerEmail,
        List<String> seats
) {
}
