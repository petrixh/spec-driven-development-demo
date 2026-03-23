package com.example.specdriven.ticket;

import com.example.specdriven.data.Ticket;
import java.time.LocalDateTime;

public record TicketSummary(
        Long id,
        String title,
        String category,
        String priority,
        String status,
        LocalDateTime createdDate
) {
    public static TicketSummary from(Ticket ticket) {
        return new TicketSummary(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getCategory().name(),
                ticket.getPriority().name(),
                ticket.getStatus().name(),
                ticket.getCreatedDate()
        );
    }
}
