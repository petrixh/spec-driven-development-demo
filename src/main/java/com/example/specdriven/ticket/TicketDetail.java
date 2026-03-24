package com.example.specdriven.ticket;

import com.example.specdriven.domain.Category;
import com.example.specdriven.domain.Priority;
import com.example.specdriven.domain.Status;
import com.example.specdriven.domain.Ticket;

import java.time.LocalDateTime;
import java.util.List;

public record TicketDetail(
        Long id,
        String title,
        String description,
        Category category,
        Priority priority,
        Status status,
        String createdByName,
        String assignedToName,
        LocalDateTime createdDate,
        LocalDateTime updatedDate,
        List<CommentInfo> comments
) {
    public static TicketDetail fromTicket(Ticket ticket) {
        return new TicketDetail(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getCategory(),
                ticket.getPriority(),
                ticket.getStatus(),
                ticket.getCreatedBy().getName(),
                ticket.getAssignedTo() != null ? ticket.getAssignedTo().getName() : null,
                ticket.getCreatedDate(),
                ticket.getUpdatedDate(),
                ticket.getComments().stream().map(CommentInfo::fromComment).toList()
        );
    }
}
