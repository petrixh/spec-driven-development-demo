package com.example.specdriven.movie;

import com.example.specdriven.show.ScreeningRoom;
import com.example.specdriven.show.ScreeningRoomRepository;
import com.example.specdriven.show.Show;
import com.example.specdriven.show.ShowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for UC-001: Browse Movies.
 * Acceptance criteria tested:
 * - Only movies with future shows appear
 * - Each result contains poster, title, and duration
 * - Movies are sorted alphabetically by title
 */
@SpringBootTest
class BrowseMoviesTest {

    @Autowired
    private MovieEndpoint movieEndpoint;

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
    }

    @Test
    void onlyMoviesWithFutureShowsAppear() {
        ScreeningRoom room = screeningRoomRepository.save(new ScreeningRoom("Room 1", 5, 8));

        Movie movieWithFutureShow = movieRepository.save(
                new Movie("Future Movie", "desc", 120, "poster.jpg"));
        Movie movieWithPastShowOnly = movieRepository.save(
                new Movie("Past Movie", "desc", 90, "poster2.jpg"));
        Movie movieWithNoShows = movieRepository.save(
                new Movie("No Shows Movie", "desc", 100, null));

        showRepository.save(new Show(LocalDateTime.now().plusDays(1), movieWithFutureShow, room));
        showRepository.save(new Show(LocalDateTime.now().minusDays(1), movieWithPastShowOnly, room));

        List<MovieEndpoint.MovieSummary> results = movieEndpoint.getMoviesWithFutureShows();

        assertEquals(1, results.size());
        assertEquals("Future Movie", results.get(0).title());
    }

    @Test
    void eachResultContainsTitleDurationAndPoster() {
        ScreeningRoom room = screeningRoomRepository.save(new ScreeningRoom("Room 1", 5, 8));
        Movie movie = movieRepository.save(
                new Movie("Inception", "A mind-bending thriller", 148, "inception.jpg"));
        showRepository.save(new Show(LocalDateTime.now().plusDays(1), movie, room));

        List<MovieEndpoint.MovieSummary> results = movieEndpoint.getMoviesWithFutureShows();

        assertEquals(1, results.size());
        MovieEndpoint.MovieSummary summary = results.get(0);
        assertEquals("Inception", summary.title());
        assertEquals(148, summary.durationMinutes());
        assertEquals("inception.jpg", summary.posterFileName());
        assertNotNull(summary.id());
    }

    @Test
    void moviesSortedAlphabeticallyByTitle() {
        ScreeningRoom room = screeningRoomRepository.save(new ScreeningRoom("Room 1", 5, 8));

        Movie movieC = movieRepository.save(new Movie("Casablanca", "desc", 102, null));
        Movie movieA = movieRepository.save(new Movie("Alien", "desc", 117, null));
        Movie movieB = movieRepository.save(new Movie("Batman", "desc", 126, null));

        showRepository.save(new Show(LocalDateTime.now().plusDays(1), movieC, room));
        showRepository.save(new Show(LocalDateTime.now().plusDays(1), movieA, room));
        showRepository.save(new Show(LocalDateTime.now().plusDays(1), movieB, room));

        List<MovieEndpoint.MovieSummary> results = movieEndpoint.getMoviesWithFutureShows();

        assertEquals(3, results.size());
        assertEquals("Alien", results.get(0).title());
        assertEquals("Batman", results.get(1).title());
        assertEquals("Casablanca", results.get(2).title());
    }

    @Test
    void movieWithMultipleFutureShowsAppearsOnlyOnce() {
        ScreeningRoom room = screeningRoomRepository.save(new ScreeningRoom("Room 1", 5, 8));
        Movie movie = movieRepository.save(new Movie("Dune", "desc", 155, "dune.jpg"));

        showRepository.save(new Show(LocalDateTime.now().plusDays(1), movie, room));
        showRepository.save(new Show(LocalDateTime.now().plusDays(2), movie, room));
        showRepository.save(new Show(LocalDateTime.now().plusDays(3), movie, room));

        List<MovieEndpoint.MovieSummary> results = movieEndpoint.getMoviesWithFutureShows();

        assertEquals(1, results.size());
        assertEquals("Dune", results.get(0).title());
    }

    @Test
    void emptyResultWhenNoMoviesHaveFutureShows() {
        ScreeningRoom room = screeningRoomRepository.save(new ScreeningRoom("Room 1", 5, 8));
        Movie movie = movieRepository.save(new Movie("Old Movie", "desc", 90, null));
        showRepository.save(new Show(LocalDateTime.now().minusDays(1), movie, room));

        List<MovieEndpoint.MovieSummary> results = movieEndpoint.getMoviesWithFutureShows();

        assertTrue(results.isEmpty());
    }
}
