package com.example.specdriven.catalog.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    long countByShowId(Long showId);

    List<Ticket> findAllByShowId(Long showId);

    boolean existsByShowIdAndSeatRowAndSeatNumber(Long showId, Integer seatRow, Integer seatNumber);

    @Query("select count(t) > 0 from Ticket t where t.show.movie.id = :movieId and t.show.dateTime > CURRENT_TIMESTAMP")
    boolean existsFutureSoldTicketsByMovieId(Long movieId);

    boolean existsByShowId(Long showId);
}
