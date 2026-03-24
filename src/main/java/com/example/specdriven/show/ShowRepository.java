package com.example.specdriven.show;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface ShowRepository extends JpaRepository<Show, Long> {

    List<Show> findByMovieIdAndDateTimeAfterOrderByDateTime(Long movieId, LocalDateTime now);

    @Query("SELECT s FROM Show s WHERE s.screeningRoom.id = :roomId AND s.dateTime > :dayStart AND s.dateTime < :dayEnd")
    List<Show> findShowsInRoomBetween(Long roomId, LocalDateTime dayStart, LocalDateTime dayEnd);

    long countByMovieId(Long movieId);

    @Query("SELECT s FROM Show s WHERE s.movie.id = :movieId AND s.dateTime > :now")
    List<Show> findFutureShowsByMovieId(Long movieId, LocalDateTime now);
}
