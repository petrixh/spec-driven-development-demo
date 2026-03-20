package com.example.specdriven.catalog.domain;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    boolean existsByTitleIgnoreCase(String title);

    boolean existsByTitleIgnoreCaseAndIdNot(String title, Long id);

    List<Movie> findAllByOrderByTitleAsc();

    @Query("""
            select distinct movie
            from Movie movie
            join movie.shows screening
            where screening.dateTime > :now
            order by movie.title asc
            """)
    List<Movie> findBrowseableMovies(LocalDateTime now);
}
