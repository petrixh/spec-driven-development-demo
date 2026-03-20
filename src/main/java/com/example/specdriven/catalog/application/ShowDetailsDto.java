package com.example.specdriven.catalog.application;

import java.util.List;

public record ShowDetailsDto(
        Long showId,
        String movieTitle,
        Long movieId,
        String dateTime,
        String screeningRoomName,
        int rows,
        int seatsPerRow,
        List<SoldSeatDto> soldSeats) {
}
