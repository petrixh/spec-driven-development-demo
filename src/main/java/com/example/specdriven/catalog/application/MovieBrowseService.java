package com.example.specdriven.catalog.application;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.specdriven.catalog.domain.Movie;
import com.example.specdriven.catalog.domain.MovieRepository;

@Service
@Transactional(readOnly = true)
public class MovieBrowseService {

    private final MovieRepository movieRepository;
    private final Clock clock;

    public MovieBrowseService(MovieRepository movieRepository, Clock clock) {
        this.movieRepository = movieRepository;
        this.clock = clock;
    }

    public List<MovieCardDto> listBrowseableMovies() {
        return movieRepository.findBrowseableMovies(LocalDateTime.now(clock))
                .stream()
                .map(this::toMovieCard)
                .toList();
    }

    public MovieDetailsDto getMovieDetails(long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found: " + movieId));

        return new MovieDetailsDto(
                movie.getId(),
                movie.getTitle(),
                movie.getDescription(),
                movie.getDurationMinutes(),
                posterUrl(movie.getPosterFileName()));
    }

    private MovieCardDto toMovieCard(Movie movie) {
        return new MovieCardDto(
                movie.getId(),
                movie.getTitle(),
                movie.getDurationMinutes(),
                posterUrl(movie.getPosterFileName()),
                true);
    }

    private String posterUrl(String posterFileName) {
        return posterFileName == null ? "" : "/posters/" + posterFileName;
    }
}
