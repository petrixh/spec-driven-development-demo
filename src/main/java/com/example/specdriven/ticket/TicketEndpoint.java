package com.example.specdriven.ticket;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import org.springframework.stereotype.Service;

import java.util.List;

@BrowserCallable
@Service
@AnonymousAllowed
public class TicketEndpoint {

    private final TicketService ticketService;

    public TicketEndpoint(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    public List<TicketService.SeatStatus> getSeatsForShow(Long showId) {
        return ticketService.getSeatsForShow(showId);
    }

    public TicketService.PurchaseResult purchaseTickets(Long showId,
                                                         List<TicketService.SeatSelection> seats,
                                                         String customerName,
                                                         String customerEmail) {
        return ticketService.purchaseTickets(showId, seats, customerName, customerEmail);
    }
}
