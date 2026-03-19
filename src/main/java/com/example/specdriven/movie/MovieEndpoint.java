package com.example.specdriven.movie;

import com.example.specdriven.show.Show;
import com.example.specdriven.show.ShowRepository;
import com.example.specdriven.show.TicketRepository;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@BrowserCallable
@Service
@AnonymousAllowed
public class MovieEndpoint {

    private final MovieRepository movieRepository;
    private final ShowRepository showRepository;
    private final TicketRepository ticketRepository;

    public MovieEndpoint(MovieRepository movieRepository,
                         ShowRepository showRepository,
                         TicketRepository ticketRepository) {
        this.movieRepository = movieRepository;
        this.showRepository = showRepository;
        this.ticketRepository = ticketRepository;
    }

    public record MovieSummary(Long id, String title, int durationMinutes, String posterFileName) {
    }

    public record MovieDetail(Long id, String title, String description,
                              int durationMinutes, String posterFileName,
                              List<ShowInfo> shows) {
    }

    public record ShowInfo(Long id, String dateTime, String roomName,
                           int totalSeats, int availableSeats, boolean soldOut) {
    }

    public List<MovieSummary> getMoviesWithFutureShows() {
        return movieRepository.findMoviesWithFutureShows(LocalDateTime.now())
                .stream()
                .map(m -> new MovieSummary(m.getId(), m.getTitle(), m.getDurationMinutes(), m.getPosterFileName()))
                .toList();
    }

    public MovieDetail getMovieDetail(Long movieId) {
        Movie movie = movieRepository.findById(movieId).orElse(null);
        if (movie == null) {
            return null;
        }

        List<Show> futureShows = showRepository
                .findByMovieIdAndDateTimeAfterOrderByDateTimeAsc(movieId, LocalDateTime.now());

        List<ShowInfo> shows = futureShows.stream().map(show -> {
            int totalSeats = show.getScreeningRoom().getRows() * show.getScreeningRoom().getSeatsPerRow();
            long soldTickets = ticketRepository.countByShowId(show.getId());
            int available = totalSeats - (int) soldTickets;
            return new ShowInfo(
                    show.getId(),
                    show.getDateTime().toString(),
                    show.getScreeningRoom().getName(),
                    totalSeats,
                    available,
                    available <= 0
            );
        }).toList();

        return new MovieDetail(
                movie.getId(),
                movie.getTitle(),
                movie.getDescription(),
                movie.getDurationMinutes(),
                movie.getPosterFileName(),
                shows
        );
    }
}
