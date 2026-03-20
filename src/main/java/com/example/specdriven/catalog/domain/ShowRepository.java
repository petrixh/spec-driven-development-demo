package com.example.specdriven.catalog.domain;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShowRepository extends JpaRepository<Show, Long> {

    List<Show> findAllByMovieIdAndDateTimeAfterOrderByDateTimeAsc(Long movieId, LocalDateTime now);

    boolean existsByMovieIdAndDateTimeAfter(Long movieId, LocalDateTime now);

    @Query("""
            select count(s) > 0
            from Show s
            where s.screeningRoom.id = :roomId
              and s.id <> :excludeShowId
              and s.dateTime < :endTime
              and s.dateTime > :startTime
            """)
    boolean existsOverlappingShow(Long roomId, Long excludeShowId, LocalDateTime startTime, LocalDateTime endTime);

    @Query("select s from Show s join fetch s.movie join fetch s.screeningRoom order by s.dateTime asc")
    List<Show> findAllByOrderByDateTimeAsc();
}
