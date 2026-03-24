package com.example.specdriven.ticket;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByShowId(Long showId);

    long countByShowId(Long showId);

    boolean existsByShowIdAndSeatRowAndSeatNumber(Long showId, Integer seatRow, Integer seatNumber);
}
