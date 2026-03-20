package com.example.specdriven.movie;

import com.vaadin.hilla.BrowserCallable;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import java.util.List;

@BrowserCallable
@AnonymousAllowed
public class MovieEndpoint {

    private final MovieRepository movieRepository;

    public MovieEndpoint(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> getMovies() {
        return movieRepository.findMoviesWithFutureShows();
    }
}
