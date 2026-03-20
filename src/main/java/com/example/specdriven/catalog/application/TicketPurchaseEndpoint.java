package com.example.specdriven.catalog.application;

import org.springframework.stereotype.Service;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;

@BrowserCallable
@AnonymousAllowed
@Service
public class TicketPurchaseEndpoint {

    private final TicketPurchaseService ticketPurchaseService;

    public TicketPurchaseEndpoint(TicketPurchaseService ticketPurchaseService) {
        this.ticketPurchaseService = ticketPurchaseService;
    }

    public ShowDetailsDto getShowDetails(long showId) {
        return ticketPurchaseService.getShowDetails(showId);
    }

    public PurchaseResultDto purchaseTickets(long showId, PurchaseRequestDto request) {
        return ticketPurchaseService.purchaseTickets(showId, request);
    }
}
