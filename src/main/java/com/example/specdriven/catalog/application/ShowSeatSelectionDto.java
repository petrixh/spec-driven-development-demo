package com.example.specdriven.catalog.application;

import java.util.List;

public record ShowSeatSelectionDto(
        long showId,
        long movieId,
        String movieTitle,
        String screeningRoomName,
        String showDate,
        String showTime,
        int rows,
        int seatsPerRow,
        List<SeatDto> soldSeats
) {
}
