package com.example.specdriven.movie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    
    @Query("SELECT DISTINCT m FROM Movie m JOIN Show s ON s.movie = m WHERE s.dateTime > CURRENT_TIMESTAMP ORDER BY m.title ASC")
    List<Movie> findMoviesWithFutureShows();
}
