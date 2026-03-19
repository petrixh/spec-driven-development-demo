package com.example.specdriven.show;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    long countByShowId(Long showId);
}
