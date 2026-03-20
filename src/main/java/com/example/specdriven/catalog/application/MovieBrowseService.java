package com.example.specdriven.catalog.application;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.specdriven.catalog.domain.Movie;
import com.example.specdriven.catalog.domain.MovieRepository;
import com.example.specdriven.catalog.domain.Show;
import com.example.specdriven.catalog.domain.ShowRepository;
import com.example.specdriven.catalog.domain.TicketRepository;

@Service
@Transactional(readOnly = true)
public class MovieBrowseService {

    private final MovieRepository movieRepository;
    private final ShowRepository showRepository;
    private final TicketRepository ticketRepository;
    private final Clock clock;

    public MovieBrowseService(MovieRepository movieRepository, ShowRepository showRepository,
            TicketRepository ticketRepository, Clock clock) {
        this.movieRepository = movieRepository;
        this.showRepository = showRepository;
        this.ticketRepository = ticketRepository;
        this.clock = clock;
    }

    public List<MovieCardDto> listBrowseableMovies() {
        return movieRepository.findBrowseableMovies(LocalDateTime.now(clock))
                .stream()
                .map(this::toMovieCard)
                .toList();
    }

    public MovieDetailsDto getMovieDetails(long movieId) {
        LocalDateTime now = LocalDateTime.now(clock);
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found: " + movieId));

        return new MovieDetailsDto(
                movie.getId(),
                movie.getTitle(),
                movie.getDescription(),
                movie.getDurationMinutes(),
                posterUrl(movie.getPosterFileName()),
                buildShowDateGroups(movieId, now));
    }

    private MovieCardDto toMovieCard(Movie movie) {
        return new MovieCardDto(
                movie.getId(),
                movie.getTitle(),
                movie.getDurationMinutes(),
                posterUrl(movie.getPosterFileName()),
                true);
    }

    private String posterUrl(String posterFileName) {
        return posterFileName == null ? "" : "/posters/" + posterFileName;
    }

    private List<ShowDateGroupDto> buildShowDateGroups(long movieId, LocalDateTime now) {
        Map<LocalDate, List<ShowTimeDto>> grouped = showRepository
                .findAllByMovieIdAndDateTimeAfterOrderByDateTimeAsc(movieId, now)
                .stream()
                .collect(Collectors.groupingBy(
                        show -> show.getDateTime().toLocalDate(),
                        java.util.LinkedHashMap::new,
                        Collectors.mapping(this::toShowTime, Collectors.toList())));

        return grouped.entrySet().stream()
                .map(entry -> new ShowDateGroupDto(
                        entry.getKey().format(DateTimeFormatter.ISO_LOCAL_DATE),
                        entry.getValue()))
                .toList();
    }

    private ShowTimeDto toShowTime(Show show) {
        int totalSeats = show.getScreeningRoom().getRows() * show.getScreeningRoom().getSeatsPerRow();
        int availableSeats = (int) Math.max(0, totalSeats - ticketRepository.countByShowId(show.getId()));

        return new ShowTimeDto(
                show.getId(),
                show.getDateTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                show.getScreeningRoom().getName(),
                availableSeats,
                availableSeats == 0);
    }
}
