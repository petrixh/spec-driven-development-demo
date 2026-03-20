package com.example.specdriven.catalog.application;

public record MovieAdminFormData(
        Long id,
        String title,
        String description,
        Integer durationMinutes,
        String posterFileName
) {
}
