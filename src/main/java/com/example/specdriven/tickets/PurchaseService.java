package com.example.specdriven.tickets;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;

    public PurchaseService(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    public PurchaseOrder createOrder(Ticket ticket, int quantity, String cardLastFour) {
        PurchaseOrder order = new PurchaseOrder();
        order.setConfirmationCode(UUID.randomUUID());
        order.setTicket(ticket);
        order.setQuantity(quantity);
        order.setTotalPrice(ticket.getPrice().multiply(BigDecimal.valueOf(quantity)));
        order.setCardLastFour(cardLastFour);
        order.setPurchasedAt(LocalDateTime.now());
        return purchaseRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Optional<PurchaseOrder> findByConfirmationCode(UUID code) {
        return purchaseRepository.findByConfirmationCode(code);
    }
}
