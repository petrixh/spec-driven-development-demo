package com.example.specdriven.ticket;

import jakarta.validation.constraints.NotBlank;

public record SubmitTicketRequest(
        @NotBlank String title,
        @NotBlank String category,
        @NotBlank String priority,
        @NotBlank String description
) {}
