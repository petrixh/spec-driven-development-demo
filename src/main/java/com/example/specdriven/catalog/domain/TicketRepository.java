package com.example.specdriven.catalog.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    long countByShowId(Long showId);

    boolean existsByShowIdAndSeatRowAndSeatNumber(Long showId, Integer seatRow, Integer seatNumber);

    List<Ticket> findAllByShowIdOrderBySeatRowAscSeatNumberAsc(Long showId);
}
