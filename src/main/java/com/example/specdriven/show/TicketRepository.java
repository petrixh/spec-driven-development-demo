package com.example.specdriven.show;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    long countByShowId(Long showId);

    List<Ticket> findByShowId(Long showId);
}
