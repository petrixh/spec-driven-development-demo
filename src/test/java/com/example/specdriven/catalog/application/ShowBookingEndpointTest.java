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
class ShowBookingEndpointTest {

    @Autowired
    private ShowBookingEndpoint endpoint;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ScreeningRoomRepository screeningRoomRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private TicketRepository ticketRepository;

    private Show show;

    @BeforeEach
    void setUp() {
        ticketRepository.deleteAll();
        showRepository.deleteAll();
        movieRepository.deleteAll();
        screeningRoomRepository.deleteAll();

        Movie movie = movieRepository.save(new Movie("Concurrent Feature", "Thriller", 118, "ai-developer-2.png"));
        ScreeningRoom room = screeningRoomRepository.save(new ScreeningRoom("Room 9", 3, 4));
        show = showRepository.save(new Show(LocalDateTime.now().plusDays(1).withHour(19).withMinute(30), movie, room));

        ticketRepository.save(new Ticket(1, 2, "Pat", "pat@example.com", LocalDateTime.now(), show));
    }

    @Test
    void returnsSeatMapWithSoldSeats() {
        ShowSeatSelectionDto selection = endpoint.getShowSeatSelection(show.getId());

        assertThat(selection.movieTitle()).isEqualTo("Concurrent Feature");
        assertThat(selection.screeningRoomName()).isEqualTo("Room 9");
        assertThat(selection.rows()).isEqualTo(3);
        assertThat(selection.seatsPerRow()).isEqualTo(4);
        assertThat(selection.soldSeats()).containsExactly(new SeatDto(1, 2, true));
    }

    @Test
    void createsTicketsAndReturnsConfirmation() {
        TicketConfirmationDto confirmation = endpoint.purchaseTickets(new PurchaseTicketsRequest(
                show.getId(),
                List.of(new SelectedSeatDto(1, 1), new SelectedSeatDto(2, 3)),
                "Taylor",
                "taylor@example.com"));

        assertThat(confirmation.movieTitle()).isEqualTo("Concurrent Feature");
        assertThat(confirmation.seats()).containsExactly("A1", "B3");
        assertThat(ticketRepository.countByShowId(show.getId())).isEqualTo(3);
    }

    @Test
    void rejectsAlreadySoldSeatsAndLeavesExistingTicketsUntouched() {
        assertThatThrownBy(() -> endpoint.purchaseTickets(new PurchaseTicketsRequest(
                show.getId(),
                List.of(new SelectedSeatDto(1, 2)),
                "Jordan",
                "jordan@example.com")))
                .isInstanceOf(BookingConflictException.class)
                .hasMessageContaining("just sold");

        assertThat(ticketRepository.countByShowId(show.getId())).isEqualTo(1);
    }
}
