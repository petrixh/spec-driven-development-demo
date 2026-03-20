package com.example.specdriven.catalog.application;

import java.util.List;

public record PurchaseResultDto(
        boolean success,
        String errorMessage,
        List<TicketDto> tickets) {
}
