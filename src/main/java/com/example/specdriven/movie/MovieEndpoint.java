package com.example.specdriven.movie;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import org.springframework.stereotype.Service;

import java.util.List;

@BrowserCallable
@Service
@AnonymousAllowed
public class MovieEndpoint {

    private final MovieService movieService;

    public MovieEndpoint(MovieService movieService) {
        this.movieService = movieService;
    }

    public List<MovieSummary> getMoviesWithFutureShows() {
        return movieService.findMoviesWithFutureShows().stream()
                .map(m -> new MovieSummary(m.getId(), m.getTitle(),
                        m.getDurationMinutes(), m.getPosterFileName()))
                .toList();
    }

    public MovieDetail getMovieById(Long id) {
        Movie movie = movieService.findById(id);
        if (movie == null) return null;
        return new MovieDetail(movie.getId(), movie.getTitle(),
                movie.getDescription(), movie.getDurationMinutes(),
                movie.getPosterFileName());
    }

    public record MovieSummary(Long id, String title, int durationMinutes, String posterFileName) {}

    public record MovieDetail(Long id, String title, String description, int durationMinutes, String posterFileName) {}
}
