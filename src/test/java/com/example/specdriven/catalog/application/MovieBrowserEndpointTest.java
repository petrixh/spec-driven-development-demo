package com.example.specdriven.catalog.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.specdriven.catalog.domain.Movie;
import com.example.specdriven.catalog.domain.MovieRepository;
import com.example.specdriven.catalog.domain.ScreeningRoom;
import com.example.specdriven.catalog.domain.ScreeningRoomRepository;
import com.example.specdriven.catalog.domain.Show;
import com.example.specdriven.catalog.domain.ShowRepository;

@SpringBootTest
@Transactional
class MovieBrowserEndpointTest {

    @Autowired
    private MovieBrowserEndpoint endpoint;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private ScreeningRoomRepository screeningRoomRepository;

    @BeforeEach
    void setUp() {
        showRepository.deleteAll();
        movieRepository.deleteAll();
        screeningRoomRepository.deleteAll();

        ScreeningRoom room = screeningRoomRepository.save(new ScreeningRoom("Room 1", 8, 10));
        Movie zebra = movieRepository.save(new Movie("Zebra Nights", "Late-night mystery", 120, "pink-elephants.png"));
        Movie alpha = movieRepository.save(new Movie("Alpha Dawn", "Opening-night drama", 95, "ai-developer-2.png"));
        Movie expired = movieRepository.save(new Movie("Expired Reel", "Already finished its run", 88, "reindeer-hunter.png"));

        showRepository.saveAll(List.of(
                new Show(LocalDateTime.now().plusDays(2), zebra, room),
                new Show(LocalDateTime.now().plusDays(1), alpha, room),
                new Show(LocalDateTime.now().minusDays(1), expired, room)));
    }

    @Test
    void returnsOnlyMoviesWithFutureShowsSortedAlphabetically() {
        List<MovieCardDto> movies = endpoint.listBrowseableMovies();

        assertThat(movies)
                .extracting(MovieCardDto::title)
                .containsExactly("Alpha Dawn", "Zebra Nights");
        assertThat(movies)
                .allMatch(MovieCardDto::hasUpcomingShows);
    }

    @Test
    void returnsMovieDetailsForDetailPage() {
        MovieCardDto movie = endpoint.listBrowseableMovies().getFirst();

        MovieDetailsDto details = endpoint.getMovieDetails(movie.id());

        assertThat(details.title()).isEqualTo(movie.title());
        assertThat(details.posterUrl()).startsWith("/posters/");
        assertThat(details.durationMinutes()).isPositive();
    }
}
