package com.example.specdriven.catalog.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
import com.example.specdriven.catalog.domain.Ticket;
import com.example.specdriven.catalog.domain.TicketRepository;

@SpringBootTest
@Transactional
class MovieAdminServiceTest {

    @Autowired
    private MovieAdminService movieAdminService;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private ScreeningRoomRepository screeningRoomRepository;

    @Autowired
    private TicketRepository ticketRepository;

    private ScreeningRoom room;

    @BeforeEach
    void setUp() {
        ticketRepository.deleteAll();
        showRepository.deleteAll();
        movieRepository.deleteAll();
        screeningRoomRepository.deleteAll();
        room = screeningRoomRepository.save(new ScreeningRoom("Room 1", 4, 4));
    }

    @Test
    void createsMovieAndListsItWithShowCount() {
        MovieAdminRowDto saved = movieAdminService.saveMovie(
                new MovieAdminFormData(null, "New Feature Film", "Description", 121, null),
                null);

        List<MovieAdminRowDto> movies = movieAdminService.listMovies();

        assertThat(saved.title()).isEqualTo("New Feature Film");
        assertThat(movies).extracting(MovieAdminRowDto::title).containsExactly("New Feature Film");
        assertThat(movies.getFirst().scheduledShows()).isZero();
    }

    @Test
    void rejectsDuplicateTitles() {
        movieRepository.save(new Movie("Duplicate", "Original", 90, null));

        assertThatThrownBy(() -> movieAdminService.saveMovie(
                new MovieAdminFormData(null, "duplicate", "Another", 95, null),
                null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void blocksDeletingMovieWithFutureSoldTickets() {
        Movie movie = movieRepository.save(new Movie("Protected", "Has tickets", 110, null));
        Show show = showRepository.save(new Show(LocalDateTime.now().plusDays(2), movie, room));
        ticketRepository.save(new Ticket(1, 1, "Alex", "alex@example.com", LocalDateTime.now(), show));

        assertThatThrownBy(() -> movieAdminService.deleteMovie(movie.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("future sold tickets");
    }
}
