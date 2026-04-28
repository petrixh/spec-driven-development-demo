package com.example.specdriven.tickets;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final TicketRepository ticketRepository;

    public PurchaseService(PurchaseRepository purchaseRepository, TicketRepository ticketRepository) {
        this.purchaseRepository = purchaseRepository;
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    public PurchaseOrder purchase(Long ticketId, int quantity, String cardNumber) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown ticket: " + ticketId));
        BigDecimal total = ticket.getPrice().multiply(BigDecimal.valueOf(quantity));
        String digitsOnly = cardNumber.replaceAll("\\D", "");
        String lastFour = digitsOnly.length() >= 4
                ? digitsOnly.substring(digitsOnly.length() - 4)
                : digitsOnly;
        PurchaseOrder order = new PurchaseOrder(ticket, quantity, total, lastFour);
        return purchaseRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Optional<PurchaseOrder> findByConfirmationCode(UUID code) {
        return purchaseRepository.findByConfirmationCode(code);
    }
}
