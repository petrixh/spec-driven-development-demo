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
class MovieBrowserEndpointTest {

    @Autowired
    private MovieBrowserEndpoint endpoint;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private ScreeningRoomRepository screeningRoomRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @BeforeEach
    void setUp() {
        ticketRepository.deleteAll();
        showRepository.deleteAll();
        movieRepository.deleteAll();
        screeningRoomRepository.deleteAll();

        ScreeningRoom room = screeningRoomRepository.save(new ScreeningRoom("Room 1", 8, 10));
        Movie zebra = movieRepository.save(new Movie("Zebra Nights", "Late-night mystery", 120, "pink-elephants.png"));
        Movie alpha = movieRepository.save(new Movie("Alpha Dawn", "Opening-night drama", 95, "ai-developer-2.png"));
        Movie expired = movieRepository.save(
                new Movie("Expired Reel", "Already finished its run", 88, "reindeer-hunter.png"));

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
        assertThat(details.showDates()).isNotEmpty();
    }

    @Test
    void returnsGroupedChronologicalShowtimesWithAvailabilityAndSoldOutState() {
        ticketRepository.deleteAll();
        showRepository.deleteAll();
        movieRepository.deleteAll();
        screeningRoomRepository.deleteAll();

        ScreeningRoom roomOne = screeningRoomRepository.save(new ScreeningRoom("Room 1", 2, 2));
        ScreeningRoom roomTwo = screeningRoomRepository.save(new ScreeningRoom("Room 2", 1, 3));
        Movie movie = movieRepository.save(
                new Movie("Grouped Movie", "For showtime grouping", 110, "ai-developer-2.png"));

        Show firstShow = showRepository
                .save(new Show(LocalDateTime.now().plusDays(1).withHour(12).withMinute(0), movie, roomOne));
        Show soldOutShow = showRepository
                .save(new Show(LocalDateTime.now().plusDays(1).withHour(18).withMinute(30), movie, roomOne));
        Show nextDayShow = showRepository
                .save(new Show(LocalDateTime.now().plusDays(2).withHour(10).withMinute(15), movie, roomTwo));
        showRepository.save(new Show(LocalDateTime.now().minusHours(2), movie, roomTwo));

        ticketRepository.saveAll(List.of(
                new Ticket(1, 1, "A", "a@example.com", LocalDateTime.now(), firstShow),
                new Ticket(1, 1, "B", "b@example.com", LocalDateTime.now(), soldOutShow),
                new Ticket(1, 2, "C", "c@example.com", LocalDateTime.now(), soldOutShow),
                new Ticket(2, 1, "D", "d@example.com", LocalDateTime.now(), soldOutShow),
                new Ticket(2, 2, "E", "e@example.com", LocalDateTime.now(), soldOutShow)));

        MovieDetailsDto details = endpoint.getMovieDetails(movie.getId());

        assertThat(details.showDates()).hasSize(2);
        assertThat(details.showDates().getFirst().date()).isLessThan(details.showDates().get(1).date());
        assertThat(details.showDates().getFirst().showtimes())
                .extracting(ShowTimeDto::time)
                .containsExactly("12:00", "18:30");
        assertThat(details.showDates().getFirst().showtimes().getFirst().availableSeats()).isEqualTo(3);
        assertThat(details.showDates().getFirst().showtimes().get(1).soldOut()).isTrue();
        assertThat(details.showDates().get(1).showtimes().getFirst().screeningRoomName()).isEqualTo("Room 2");

        assertThat(nextDayShow).isNotNull();
    }
}
