package com.example.specdriven.ticket;

import com.example.specdriven.data.*;
import com.vaadin.hilla.BrowserCallable;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@BrowserCallable
@PermitAll
public class TicketEndpoint {

    private final TicketService ticketService;
    private final UserRepository userRepository;

    public TicketEndpoint(TicketService ticketService, UserRepository userRepository) {
        this.ticketService = ticketService;
        this.userRepository = userRepository;
    }

    public TicketSummary submitTicket(SubmitTicketRequest request) {
        User user = getCurrentUser();
        Ticket ticket = ticketService.createTicket(
                request.title(),
                request.description(),
                Category.valueOf(request.category()),
                Priority.valueOf(request.priority()),
                user
        );
        return TicketSummary.from(ticket);
    }

    public List<TicketSummary> getMyTickets() {
        User user = getCurrentUser();
        return ticketService.getTicketsByUser(user).stream()
                .map(TicketSummary::from)
                .toList();
    }

    public TicketDetail getTicketDetail(Long id) {
        User user = getCurrentUser();
        Ticket ticket = ticketService.getTicketById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        if (!ticket.getCreatedBy().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }
        List<CommentInfo> comments = ticketService.getComments(id).stream()
                .map(CommentInfo::from)
                .toList();
        return TicketDetail.from(ticket, comments);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow();
    }
}
