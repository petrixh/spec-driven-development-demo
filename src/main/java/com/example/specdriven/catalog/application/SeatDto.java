package com.example.specdriven.catalog.application;

public record SeatDto(
        int row,
        int number,
        boolean sold
) {
}
