package com.example.specdriven.catalog.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;

@BrowserCallable
@AnonymousAllowed
@Service
public class MovieBrowserEndpoint {

    private final MovieBrowseService movieBrowseService;

    public MovieBrowserEndpoint(MovieBrowseService movieBrowseService) {
        this.movieBrowseService = movieBrowseService;
    }

    public List<MovieCardDto> listBrowseableMovies() {
        return movieBrowseService.listBrowseableMovies();
    }

    public MovieDetailsDto getMovieDetails(long movieId) {
        return movieBrowseService.getMovieDetails(movieId);
    }
}
