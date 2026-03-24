package com.example.specdriven.show;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@BrowserCallable
@Service
@AnonymousAllowed
public class ShowEndpoint {

    private final ShowService showService;
    private final ShowRepository showRepository;

    public ShowEndpoint(ShowService showService, ShowRepository showRepository) {
        this.showService = showService;
        this.showRepository = showRepository;
    }

    public List<ShowtimeGroup> getShowtimesForMovie(Long movieId) {
        var shows = showRepository.findByMovieIdAndDateTimeAfterOrderByDateTime(
                movieId, LocalDateTime.now());

        Map<String, List<Show>> grouped = shows.stream()
                .collect(Collectors.groupingBy(
                        s -> s.getDateTime().toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                        Collectors.toList()));

        return grouped.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> new ShowtimeGroup(
                        entry.getKey(),
                        entry.getValue().stream()
                                .map(s -> {
                                    long totalSeats = (long) s.getScreeningRoom().getRows()
                                            * s.getScreeningRoom().getSeatsPerRow();
                                    long sold = showService.getTicketCount(s);
                                    long available = totalSeats - sold;
                                    return new ShowtimeInfo(
                                            s.getId(),
                                            s.getDateTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                                            s.getScreeningRoom().getName(),
                                            (int) available,
                                            available == 0);
                                })
                                .toList()))
                .toList();
    }

    public ShowDetail getShowDetail(Long showId) {
        Show show = showService.findById(showId);
        if (show == null) return null;
        return new ShowDetail(
                show.getId(),
                show.getMovie().getId(),
                show.getMovie().getTitle(),
                show.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                show.getScreeningRoom().getName(),
                show.getScreeningRoom().getRows(),
                show.getScreeningRoom().getSeatsPerRow());
    }

    public record ShowtimeGroup(String date, List<ShowtimeInfo> showtimes) {}
    public record ShowtimeInfo(Long id, String time, String roomName, int availableSeats, boolean soldOut) {}
    public record ShowDetail(Long id, Long movieId, String movieTitle, String dateTime,
                             String roomName, int rows, int seatsPerRow) {}
}
