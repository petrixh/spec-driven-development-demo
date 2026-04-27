package com.example.specdriven.tickets;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface PurchaseRepository extends JpaRepository<PurchaseOrder, Long> {
    Optional<PurchaseOrder> findByConfirmationCode(UUID confirmationCode);
}
