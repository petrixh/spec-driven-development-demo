package com.example.specdriven.catalog.application;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record SelectedSeatDto(
        @Min(1) int row,
        @Min(1) @Max(99) int number
) {
}
