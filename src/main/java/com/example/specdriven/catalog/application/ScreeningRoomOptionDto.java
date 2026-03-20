package com.example.specdriven.catalog.application;

public record ScreeningRoomOptionDto(Long id, String name, Integer rows, Integer seatsPerRow) {

    public int capacity() {
        return rows * seatsPerRow;
    }
}
