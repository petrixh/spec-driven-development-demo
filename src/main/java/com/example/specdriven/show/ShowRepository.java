package com.example.specdriven.show;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowRepository extends JpaRepository<Show, Long> {

    List<Show> findByMovieIdAndDateTimeAfterOrderByDateTimeAsc(Long movieId, LocalDateTime now);
}
