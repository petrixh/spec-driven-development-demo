package com.example.specdriven.show;

import com.example.specdriven.movie.Movie;
import com.example.specdriven.movie.MovieRepository;
import com.example.specdriven.screeningroom.ScreeningRoom;
import com.example.specdriven.screeningroom.ScreeningRoomRepository;
import com.example.specdriven.ticket.Ticket;
import com.example.specdriven.ticket.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ShowServiceTest {

    @Autowired private ShowService showService;
    @Autowired private ShowRepository showRepository;
    @Autowired private MovieRepository movieRepository;
    @Autowired private ScreeningRoomRepository screeningRoomRepository;
    @Autowired private TicketRepository ticketRepository;

    private Movie movie;
    private ScreeningRoom room;

    @BeforeEach
    void setUp() {
        ticketRepository.deleteAll();
        showRepository.deleteAll();
        movieRepository.deleteAll();
        screeningRoomRepository.deleteAll();

        movie = new Movie();
        movie.setTitle("Test Movie");
        movie.setDurationMinutes(120);
        movie = movieRepository.save(movie);

        room = new ScreeningRoom();
        room.setName("Room 1");
        room.setRows(8);
        room.setSeatsPerRow(10);
        room = screeningRoomRepository.save(room);
    }

    @Test
    void canCreateFutureShow() {
        Show show = new Show();
        show.setMovie(movie);
        show.setScreeningRoom(room);
        show.setDateTime(LocalDateTime.now().plusDays(1));

        Show saved = showService.save(show);
        assertNotNull(saved.getId());
    }

    @Test
    void showMustBeInFuture() {
        Show show = new Show();
        show.setMovie(movie);
        show.setScreeningRoom(room);
        show.setDateTime(LocalDateTime.now().minusHours(1));

        assertThrows(IllegalArgumentException.class, () -> showService.save(show));
    }

    @Test
    void overlappingShowsInSameRoomRejected() {
        Show show1 = new Show();
        show1.setMovie(movie);
        show1.setScreeningRoom(room);
        show1.setDateTime(LocalDateTime.now().plusDays(1).withHour(14).withMinute(0));
        showService.save(show1);

        // 120 min movie + 30 min buffer = 150 min. Show1 ends at 16:30.
        // Show2 at 15:00 overlaps.
        Show show2 = new Show();
        show2.setMovie(movie);
        show2.setScreeningRoom(room);
        show2.setDateTime(LocalDateTime.now().plusDays(1).withHour(15).withMinute(0));

        assertThrows(IllegalArgumentException.class, () -> showService.save(show2));
    }

    @Test
    void nonOverlappingShowsInSameRoomAllowed() {
        Show show1 = new Show();
        show1.setMovie(movie);
        show1.setScreeningRoom(room);
        show1.setDateTime(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0));
        showService.save(show1);

        // 120 min + 30 min buffer = show1 ends at 12:30. Show2 at 13:00 is fine.
        Show show2 = new Show();
        show2.setMovie(movie);
        show2.setScreeningRoom(room);
        show2.setDateTime(LocalDateTime.now().plusDays(1).withHour(13).withMinute(0));

        assertDoesNotThrow(() -> showService.save(show2));
    }

    @Test
    void canDeleteShowWithNoTickets() {
        Show show = new Show();
        show.setMovie(movie);
        show.setScreeningRoom(room);
        show.setDateTime(LocalDateTime.now().plusDays(1));
        Show saved = showService.save(show);

        assertDoesNotThrow(() -> showService.delete(saved));
    }

    @Test
    void cannotDeleteShowWithSoldTickets() {
        Show show = new Show();
        show.setMovie(movie);
        show.setScreeningRoom(room);
        show.setDateTime(LocalDateTime.now().plusDays(1));
        Show saved = showService.save(show);

        Ticket ticket = new Ticket();
        ticket.setShow(saved);
        ticket.setSeatRow(1);
        ticket.setSeatNumber(1);
        ticket.setCustomerName("John");
        ticket.setCustomerEmail("john@test.com");
        ticketRepository.save(ticket);

        assertThrows(IllegalStateException.class, () -> showService.delete(saved));
    }

    @Test
    void movieIsRequired() {
        Show show = new Show();
        show.setScreeningRoom(room);
        show.setDateTime(LocalDateTime.now().plusDays(1));
        assertThrows(IllegalArgumentException.class, () -> showService.save(show));
    }
}
