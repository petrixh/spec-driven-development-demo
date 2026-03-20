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
class ShowAdminServiceTest {

    @Autowired
    private ShowAdminService showAdminService;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ScreeningRoomRepository screeningRoomRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private TicketRepository ticketRepository;

    private Movie movie;
    private Movie otherMovie;
    private ScreeningRoom room1;
    private ScreeningRoom room2;

    @BeforeEach
    void setUp() {
        ticketRepository.deleteAll();
        showRepository.deleteAll();
        movieRepository.deleteAll();
        screeningRoomRepository.deleteAll();

        movie = movieRepository.save(new Movie("Future Film", "Desc", 120, null));
        otherMovie = movieRepository.save(new Movie("Second Film", "Desc", 95, null));
        room1 = screeningRoomRepository.save(new ScreeningRoom("Room 1", 5, 5));
        room2 = screeningRoomRepository.save(new ScreeningRoom("Room 2", 4, 4));
    }

    @Test
    void createsShowAndListsTicketCounts() {
        ShowAdminRowDto saved = showAdminService.saveShow(new ShowAdminFormData(
                null,
                movie.getId(),
                room1.getId(),
                LocalDateTime.now().plusDays(1)));

        List<ShowAdminRowDto> shows = showAdminService.listShows();

        assertThat(saved.movieTitle()).isEqualTo("Future Film");
        assertThat(shows).hasSize(1);
        assertThat(shows.getFirst().ticketsSold()).isZero();
        assertThat(shows.getFirst().totalCapacity()).isEqualTo(25);
    }

    @Test
    void rejectsOverlappingShowInSameRoom() {
        showRepository.save(new Show(LocalDateTime.now().plusDays(1).withHour(18).withMinute(0), movie, room1));

        assertThatThrownBy(() -> showAdminService.saveShow(new ShowAdminFormData(
                null,
                otherMovie.getId(),
                room1.getId(),
                LocalDateTime.now().plusDays(1).withHour(19).withMinute(0))))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("overlapping");
    }

    @Test
    void blocksDeletingShowWithSoldTickets() {
        Show show = showRepository.save(new Show(LocalDateTime.now().plusDays(2), movie, room1));
        ticketRepository.save(new Ticket(1, 1, "Alex", "alex@example.com", LocalDateTime.now(), show));

        assertThatThrownBy(() -> showAdminService.deleteShow(show.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("sold tickets");
    }

    @Test
    void blocksChangingMovieForExistingShow() {
        Show show = showRepository.save(new Show(LocalDateTime.now().plusDays(2), movie, room1));

        assertThatThrownBy(() -> showAdminService.saveShow(new ShowAdminFormData(
                show.getId(),
                otherMovie.getId(),
                room2.getId(),
                show.getDateTime().plusHours(2))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("cannot be changed");
    }
}
