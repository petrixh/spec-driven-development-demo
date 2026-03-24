package com.example.specdriven.movie;

import com.example.specdriven.show.ShowRepository;
import com.example.specdriven.ticket.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MovieServiceTest {

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @BeforeEach
    void setUp() {
        ticketRepository.deleteAll();
        showRepository.deleteAll();
        movieRepository.deleteAll();
    }

    @Test
    void titleIsRequired() {
        Movie movie = new Movie();
        movie.setDurationMinutes(120);
        assertThrows(IllegalArgumentException.class, () -> movieService.save(movie));
    }

    @Test
    void blankTitleIsRejected() {
        Movie movie = new Movie();
        movie.setTitle("   ");
        movie.setDurationMinutes(120);
        assertThrows(IllegalArgumentException.class, () -> movieService.save(movie));
    }

    @Test
    void durationMustBePositive() {
        Movie movie = new Movie();
        movie.setTitle("Test Movie");
        movie.setDurationMinutes(0);
        assertThrows(IllegalArgumentException.class, () -> movieService.save(movie));
    }

    @Test
    void negativeDurationIsRejected() {
        Movie movie = new Movie();
        movie.setTitle("Test Movie");
        movie.setDurationMinutes(-5);
        assertThrows(IllegalArgumentException.class, () -> movieService.save(movie));
    }

    @Test
    void titleMustBeUnique() {
        Movie movie1 = new Movie();
        movie1.setTitle("Unique Title");
        movie1.setDurationMinutes(120);
        movieService.save(movie1);

        Movie movie2 = new Movie();
        movie2.setTitle("Unique Title");
        movie2.setDurationMinutes(90);
        assertThrows(IllegalArgumentException.class, () -> movieService.save(movie2));
    }

    @Test
    void canUpdateMovieWithSameTitle() {
        Movie movie = new Movie();
        movie.setTitle("My Movie");
        movie.setDurationMinutes(120);
        Movie saved = movieService.save(movie);

        saved.setDurationMinutes(130);
        assertDoesNotThrow(() -> movieService.save(saved));
    }

    @Test
    void canSaveAndRetrieveMovie() {
        Movie movie = new Movie();
        movie.setTitle("Save Test");
        movie.setDurationMinutes(120);
        movie.setDescription("A test movie");

        Movie saved = movieService.save(movie);
        assertNotNull(saved.getId());

        Movie found = movieService.findById(saved.getId());
        assertNotNull(found);
        assertEquals("Save Test", found.getTitle());
        assertEquals(120, found.getDurationMinutes());
    }

    @Test
    void canDeleteMovieWithoutFutureShows() {
        Movie movie = new Movie();
        movie.setTitle("Delete Test");
        movie.setDurationMinutes(90);
        Movie saved = movieService.save(movie);

        assertDoesNotThrow(() -> movieService.delete(saved));
        assertNull(movieService.findById(saved.getId()));
    }

    @Test
    void findAllReturnsAllMovies() {
        Movie m1 = new Movie();
        m1.setTitle("Movie A");
        m1.setDurationMinutes(100);
        movieService.save(m1);

        Movie m2 = new Movie();
        m2.setTitle("Movie B");
        m2.setDurationMinutes(110);
        movieService.save(m2);

        assertEquals(2, movieService.findAll().size());
    }
}
