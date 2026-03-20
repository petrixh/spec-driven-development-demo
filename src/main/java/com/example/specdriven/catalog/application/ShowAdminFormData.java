package com.example.specdriven.catalog.application;

import java.time.LocalDateTime;

public record ShowAdminFormData(Long id, Long movieId, Long screeningRoomId, LocalDateTime dateTime) {
}
