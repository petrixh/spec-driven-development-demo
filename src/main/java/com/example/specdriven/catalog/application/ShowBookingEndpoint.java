package com.example.specdriven.catalog.application;

import org.springframework.stereotype.Service;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;

import jakarta.validation.Valid;

@BrowserCallable
@AnonymousAllowed
@Service
public class ShowBookingEndpoint {

    private final ShowBookingService showBookingService;

    public ShowBookingEndpoint(ShowBookingService showBookingService) {
        this.showBookingService = showBookingService;
    }

    public ShowSeatSelectionDto getShowSeatSelection(long showId) {
        return showBookingService.getShowSeatSelection(showId);
    }

    public TicketConfirmationDto purchaseTickets(@Valid PurchaseTicketsRequest request) {
        return showBookingService.purchaseTickets(request);
    }
}
