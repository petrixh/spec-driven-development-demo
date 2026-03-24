package com.example.specdriven.ticket;

import com.example.specdriven.movie.Movie;
import com.example.specdriven.movie.MovieRepository;
import com.example.specdriven.screeningroom.ScreeningRoom;
import com.example.specdriven.screeningroom.ScreeningRoomRepository;
import com.example.specdriven.show.Show;
import com.example.specdriven.show.ShowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TicketServiceTest {

    @Autowired private TicketService ticketService;
    @Autowired private TicketRepository ticketRepository;
    @Autowired private ShowRepository showRepository;
    @Autowired private MovieRepository movieRepository;
    @Autowired private ScreeningRoomRepository screeningRoomRepository;

    private Show show;

    @BeforeEach
    void setUp() {
        ticketRepository.deleteAll();
        showRepository.deleteAll();
        movieRepository.deleteAll();
        screeningRoomRepository.deleteAll();

        Movie movie = new Movie();
        movie.setTitle("Test Movie");
        movie.setDurationMinutes(120);
        movie = movieRepository.save(movie);

        ScreeningRoom room = new ScreeningRoom();
        room.setName("Room 1");
        room.setRows(5);
        room.setSeatsPerRow(6);
        room = screeningRoomRepository.save(room);

        show = new Show();
        show.setMovie(movie);
        show.setScreeningRoom(room);
        show.setDateTime(LocalDateTime.now().plusDays(1));
        show = showRepository.save(show);
    }

    @Test
    void getSeatsForShowReturnsAllSeats() {
        var seats = ticketService.getSeatsForShow(show.getId());
        assertEquals(30, seats.size()); // 5 rows x 6 seats
        assertTrue(seats.stream().noneMatch(TicketService.SeatStatus::sold));
    }

    @Test
    void purchaseCreatesTickets() {
        var result = ticketService.purchaseTickets(show.getId(),
                List.of(new TicketService.SeatSelection(1, 1), new TicketService.SeatSelection(1, 2)),
                "John Doe", "john@test.com");

        assertTrue(result.success());
        assertEquals(2, result.tickets().size());
        assertEquals(2, ticketRepository.countByShowId(show.getId()));
    }

    @Test
    void purchasedSeatsShowAsSold() {
        ticketService.purchaseTickets(show.getId(),
                List.of(new TicketService.SeatSelection(1, 1)),
                "John", "john@test.com");

        var seats = ticketService.getSeatsForShow(show.getId());
        var seat11 = seats.stream()
                .filter(s -> s.row() == 1 && s.seat() == 1).findFirst().orElseThrow();
        assertTrue(seat11.sold());
    }

    @Test
    void maximumSixTicketsPerTransaction() {
        var seats = List.of(
                new TicketService.SeatSelection(1, 1), new TicketService.SeatSelection(1, 2),
                new TicketService.SeatSelection(1, 3), new TicketService.SeatSelection(1, 4),
                new TicketService.SeatSelection(1, 5), new TicketService.SeatSelection(1, 6),
                new TicketService.SeatSelection(2, 1));
        var result = ticketService.purchaseTickets(show.getId(), seats, "John", "john@test.com");
        assertFalse(result.success());
        assertTrue(result.message().contains("Maximum 6"));
    }

    @Test
    void nameIsRequired() {
        var result = ticketService.purchaseTickets(show.getId(),
                List.of(new TicketService.SeatSelection(1, 1)),
                "", "john@test.com");
        assertFalse(result.success());
    }

    @Test
    void emailIsRequired() {
        var result = ticketService.purchaseTickets(show.getId(),
                List.of(new TicketService.SeatSelection(1, 1)),
                "John", "");
        assertFalse(result.success());
    }

    @Test
    void invalidEmailIsRejected() {
        var result = ticketService.purchaseTickets(show.getId(),
                List.of(new TicketService.SeatSelection(1, 1)),
                "John", "not-an-email");
        assertFalse(result.success());
    }

    @Test
    void doubleBookingIsRejected() {
        ticketService.purchaseTickets(show.getId(),
                List.of(new TicketService.SeatSelection(1, 1)),
                "John", "john@test.com");

        var result = ticketService.purchaseTickets(show.getId(),
                List.of(new TicketService.SeatSelection(1, 1)),
                "Jane", "jane@test.com");
        assertFalse(result.success());
        assertTrue(result.message().contains("already been sold"));
    }
}
