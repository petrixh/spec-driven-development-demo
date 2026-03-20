package com.example.specdriven.catalog.application;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PurchaseTicketsRequest(
        long showId,
        List<@Valid SelectedSeatDto> seats,
        @NotBlank String customerName,
        @NotBlank @Email String customerEmail
) {
}
