package com.example.specdriven.tickets;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByTransitMode(TransitMode transitMode);
}
