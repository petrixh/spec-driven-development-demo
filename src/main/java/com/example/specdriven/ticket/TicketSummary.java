package com.example.specdriven.ticket;

import com.example.specdriven.domain.Category;
import com.example.specdriven.domain.Priority;
import com.example.specdriven.domain.Status;
import com.example.specdriven.domain.Ticket;

import java.time.LocalDateTime;

public record TicketSummary(
        Long id,
        String title,
        Category category,
        Priority priority,
        Status status,
        LocalDateTime createdDate
) {
    public static TicketSummary fromTicket(Ticket ticket) {
        return new TicketSummary(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getCategory(),
                ticket.getPriority(),
                ticket.getStatus(),
                ticket.getCreatedDate()
        );
    }
}
