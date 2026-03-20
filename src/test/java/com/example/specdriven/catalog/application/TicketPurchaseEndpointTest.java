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
import com.example.specdriven.catalog.domain.Ticket;
import com.example.specdriven.catalog.domain.TicketRepository;

@SpringBootTest
@Transactional
class TicketPurchaseEndpointTest {

    @Autowired
    private TicketPurchaseEndpoint endpoint;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private ScreeningRoomRepository screeningRoomRepository;

    @Autowired
    private TicketRepository ticketRepository;

    private Show show;
    private ScreeningRoom room;

    @BeforeEach
    void setUp() {
        ticketRepository.deleteAll();
        showRepository.deleteAll();
        movieRepository.deleteAll();
        screeningRoomRepository.deleteAll();

        room = screeningRoomRepository.save(new ScreeningRoom("Room 1", 3, 4));
        Movie movie = movieRepository.save(new Movie("Test Movie", "Desc", 90, "ai-developer-2.png"));
        show = showRepository.save(new Show(LocalDateTime.now().plusDays(1), movie, room));
    }

    @Test
    void returnsShowDetailsWithSeatMapAndSoldSeats() {
        ticketRepository.save(new Ticket(1, 2, "Bob", "bob@example.com", LocalDateTime.now(), show));

        ShowDetailsDto details = endpoint.getShowDetails(show.getId());

        assertThat(details.movieTitle()).isEqualTo("Test Movie");
        assertThat(details.rows()).isEqualTo(3);
        assertThat(details.seatsPerRow()).isEqualTo(4);
        assertThat(details.soldSeats()).hasSize(1);
        assertThat(details.soldSeats().getFirst().row()).isEqualTo(1);
        assertThat(details.soldSeats().getFirst().seatNumber()).isEqualTo(2);
    }

    @Test
    void purchasesTicketsSuccessfully() {
        PurchaseRequestDto request = new PurchaseRequestDto(
                List.of(new SeatDto(1, 1), new SeatDto(1, 3)),
                "Alice",
                "alice@example.com");

        PurchaseResultDto result = endpoint.purchaseTickets(show.getId(), request);

        assertThat(result.success()).isTrue();
        assertThat(result.tickets()).hasSize(2);
        assertThat(ticketRepository.countByShowId(show.getId())).isEqualTo(2);
    }

    @Test
    void rejectsMoreThanSixSeats() {
        PurchaseRequestDto request = new PurchaseRequestDto(
                List.of(new SeatDto(1,1), new SeatDto(1,2), new SeatDto(1,3),
                        new SeatDto(2,1), new SeatDto(2,2), new SeatDto(2,3), new SeatDto(3,1)),
                "Big Group", "group@example.com");

        PurchaseResultDto result = endpoint.purchaseTickets(show.getId(), request);

        assertThat(result.success()).isFalse();
        assertThat(result.errorMessage()).contains("Maximum");
    }

    @Test
    void rejectsDuplicateSeatOnConcurrentPurchase() {
        ticketRepository.save(new Ticket(2, 2, "First", "first@example.com", LocalDateTime.now(), show));

        PurchaseRequestDto request = new PurchaseRequestDto(
                List.of(new SeatDto(2, 2)), "Second", "second@example.com");

        PurchaseResultDto result = endpoint.purchaseTickets(show.getId(), request);

        assertThat(result.success()).isFalse();
        assertThat(result.errorMessage()).contains("just taken");
    }

    @Test
    void rejectsInvalidEmail() {
        PurchaseRequestDto request = new PurchaseRequestDto(
                List.of(new SeatDto(1, 1)), "Alice", "not-an-email");

        PurchaseResultDto result = endpoint.purchaseTickets(show.getId(), request);

        assertThat(result.success()).isFalse();
        assertThat(result.errorMessage()).containsIgnoringCase("email");
    }

    @Test
    void rejectsMissingName() {
        PurchaseRequestDto request = new PurchaseRequestDto(
                List.of(new SeatDto(1, 1)), "  ", "alice@example.com");

        PurchaseResultDto result = endpoint.purchaseTickets(show.getId(), request);

        assertThat(result.success()).isFalse();
    }
}
