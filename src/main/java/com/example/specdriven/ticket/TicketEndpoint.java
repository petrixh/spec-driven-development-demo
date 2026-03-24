package com.example.specdriven.ticket;

import com.example.specdriven.domain.Status;
import com.example.specdriven.domain.Ticket;
import com.vaadin.hilla.BrowserCallable;
import jakarta.annotation.security.PermitAll;
import org.springframework.stereotype.Service;

import java.util.List;

@BrowserCallable
@Service
public class TicketEndpoint {

    private final TicketService ticketService;

    public TicketEndpoint(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PermitAll
    public TicketSummary submitTicket(SubmitTicketRequest request) {
        Ticket ticket = ticketService.submitTicket(
                request.title(),
                request.description(),
                request.category(),
                request.priority()
        );
        return TicketSummary.fromTicket(ticket);
    }

    @PermitAll
    public List<TicketSummary> getMyTickets(Status statusFilter) {
        return ticketService.getMyTickets(statusFilter).stream()
                .map(TicketSummary::fromTicket)
                .toList();
    }

    @PermitAll
    public TicketDetail getTicketDetail(Long id) {
        Ticket ticket = ticketService.getTicketById(id);
        // Customers can only see their own tickets
        var currentUser = ticketService.getCurrentUser();
        if (!ticket.getCreatedBy().getId().equals(currentUser.getId())
                && currentUser.getRole() == com.example.specdriven.domain.Role.CUSTOMER) {
            throw new IllegalArgumentException("Access denied");
        }
        return TicketDetail.fromTicket(ticket);
    }
}
