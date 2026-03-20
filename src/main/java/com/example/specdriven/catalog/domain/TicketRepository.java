package com.example.specdriven.catalog.domain;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    long countByShowId(Long showId);

    boolean existsByShowIdAndSeatRowAndSeatNumber(Long showId, Integer seatRow, Integer seatNumber);

    List<Ticket> findAllByShowIdOrderBySeatRowAscSeatNumberAsc(Long showId);

    @Query("""
            select count(ticket) > 0
            from Ticket ticket
            where ticket.show.movie.id = :movieId
              and ticket.show.dateTime > :now
            """)
    boolean existsFutureTicketsForMovie(Long movieId, LocalDateTime now);
}
