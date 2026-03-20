package com.example.specdriven.catalog.domain;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowRepository extends JpaRepository<Show, Long> {

    long countByMovieId(Long movieId);

    List<Show> findAllByOrderByDateTimeAsc();

    List<Show> findAllByMovieIdAndDateTimeAfterOrderByDateTimeAsc(Long movieId, LocalDateTime now);

    List<Show> findAllByScreeningRoomIdOrderByDateTimeAsc(Long screeningRoomId);
}
