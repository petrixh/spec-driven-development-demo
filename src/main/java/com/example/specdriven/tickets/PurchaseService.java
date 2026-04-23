package com.example.specdriven.tickets;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class PurchaseService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final TicketRepository ticketRepository;

    public PurchaseService(PurchaseOrderRepository purchaseOrderRepository, TicketRepository ticketRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    public PurchaseOrder createPurchase(Long ticketId, Integer quantity, String cardLastFour) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found: " + ticketId));
        PurchaseOrder order = PurchaseOrder.create(ticket, quantity, cardLastFour);
        return purchaseOrderRepository.save(order);
    }

    public Optional<PurchaseOrder> findByConfirmationCode(UUID confirmationCode) {
        return purchaseOrderRepository.findByConfirmationCode(confirmationCode);
    }

    public Optional<PurchaseOrder> findById(Long id) {
        return purchaseOrderRepository.findById(id);
    }
}
