package com.example.specdriven.catalog.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    long countByShowId(Long showId);
}
