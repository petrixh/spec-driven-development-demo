package com.example.specdriven.catalog.application;

public record ShowTimeDto(
        Long showId,
        String time,
        String screeningRoomName,
        int availableSeats,
        boolean soldOut) {
}
