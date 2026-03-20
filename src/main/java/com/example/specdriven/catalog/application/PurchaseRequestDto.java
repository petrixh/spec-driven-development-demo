package com.example.specdriven.catalog.application;

import java.util.List;

public record PurchaseRequestDto(
        List<SeatDto> seats,
        String customerName,
        String customerEmail) {
}
