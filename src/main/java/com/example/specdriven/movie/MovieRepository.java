package com.example.specdriven.movie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("SELECT DISTINCT m FROM Movie m JOIN Show s ON s.movie = m WHERE s.dateTime > :now ORDER BY m.title")
    List<Movie> findMoviesWithFutureShows(LocalDateTime now);

    boolean existsByTitle(String title);

    boolean existsByTitleAndIdNot(String title, Long id);
}
