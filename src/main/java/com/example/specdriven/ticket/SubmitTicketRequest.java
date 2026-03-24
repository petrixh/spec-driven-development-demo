package com.example.specdriven.ticket;

import com.example.specdriven.domain.Category;
import com.example.specdriven.domain.Priority;

public record SubmitTicketRequest(
        String title,
        Category category,
        Priority priority,
        String description
) {
}
