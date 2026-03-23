package com.example.specdriven.ticket;

import com.example.specdriven.data.Ticket;
import java.time.LocalDateTime;
import java.util.List;

public record TicketDetail(
        Long id,
        String title,
        String description,
        String category,
        String priority,
        String status,
        LocalDateTime createdDate,
        List<CommentInfo> comments
) {
    public static TicketDetail from(Ticket ticket, List<CommentInfo> comments) {
        return new TicketDetail(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getCategory().name(),
                ticket.getPriority().name(),
                ticket.getStatus().name(),
                ticket.getCreatedDate(),
                comments
        );
    }
}
