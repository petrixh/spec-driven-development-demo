package com.example.specdriven.catalog.application;

public record TicketDto(
        Long id,
        int row,
        int seatNumber,
        String movieTitle,
        String showDateTime,
        String screeningRoomName) {
}
