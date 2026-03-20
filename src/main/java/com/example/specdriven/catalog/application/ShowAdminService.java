package com.example.specdriven.catalog.application;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.specdriven.catalog.domain.Movie;
import com.example.specdriven.catalog.domain.MovieRepository;
import com.example.specdriven.catalog.domain.ScreeningRoom;
import com.example.specdriven.catalog.domain.ScreeningRoomRepository;
import com.example.specdriven.catalog.domain.Show;
import com.example.specdriven.catalog.domain.ShowRepository;
import com.example.specdriven.catalog.domain.TicketRepository;

@Service
@Transactional
public class ShowAdminService {

    private static final int CLEANUP_BUFFER_MINUTES = 30;

    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;
    private final ScreeningRoomRepository screeningRoomRepository;
    private final TicketRepository ticketRepository;
    private final Clock clock;

    public ShowAdminService(
            ShowRepository showRepository,
            MovieRepository movieRepository,
            ScreeningRoomRepository screeningRoomRepository,
            TicketRepository ticketRepository,
            Clock clock
    ) {
        this.showRepository = showRepository;
        this.movieRepository = movieRepository;
        this.screeningRoomRepository = screeningRoomRepository;
        this.ticketRepository = ticketRepository;
        this.clock = clock;
    }

    @Transactional(readOnly = true)
    public List<ShowAdminRowDto> listShows() {
        return showRepository.findAllByOrderByDateTimeAsc().stream()
                .map(show -> new ShowAdminRowDto(
                        show.getId(),
                        show.getMovie().getId(),
                        show.getMovie().getTitle(),
                        show.getDateTime(),
                        show.getScreeningRoom().getId(),
                        show.getScreeningRoom().getName(),
                        ticketRepository.countByShowId(show.getId()),
                        show.getScreeningRoom().getRows() * show.getScreeningRoom().getSeatsPerRow()))
                .toList();
    }

    @Transactional(readOnly = true)
    public ShowAdminFormData getShow(Long showId) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new IllegalArgumentException("Show not found."));
        return new ShowAdminFormData(
                show.getId(),
                show.getMovie().getId(),
                show.getScreeningRoom().getId(),
                show.getDateTime());
    }

    @Transactional(readOnly = true)
    public List<MovieOptionDto> listMovieOptions() {
        return movieRepository.findAllByOrderByTitleAsc().stream()
                .map(movie -> new MovieOptionDto(movie.getId(), movie.getTitle()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ScreeningRoomOptionDto> listScreeningRooms() {
        return screeningRoomRepository.findAll().stream()
                .sorted(Comparator.comparing(ScreeningRoom::getName))
                .map(room -> new ScreeningRoomOptionDto(
                        room.getId(),
                        room.getName(),
                        room.getRows(),
                        room.getSeatsPerRow()))
                .toList();
    }

    public ShowAdminRowDto saveShow(ShowAdminFormData formData) {
        validate(formData);

        Show show;
        Movie movie;
        if (formData.id() == null) {
            movie = movieRepository.findById(formData.movieId())
                    .orElseThrow(() -> new IllegalArgumentException("Movie is required."));
            ScreeningRoom screeningRoom = loadScreeningRoom(formData.screeningRoomId());
            checkForOverlap(null, movie, screeningRoom, formData.dateTime());
            show = showRepository.save(new Show(formData.dateTime(), movie, screeningRoom));
        } else {
            show = showRepository.findById(formData.id())
                    .orElseThrow(() -> new IllegalArgumentException("Show not found."));
            movie = show.getMovie();
            if (!movie.getId().equals(formData.movieId())) {
                throw new IllegalArgumentException("The movie for an existing show cannot be changed.");
            }
            ScreeningRoom screeningRoom = loadScreeningRoom(formData.screeningRoomId());
            checkForOverlap(show.getId(), movie, screeningRoom, formData.dateTime());
            show.update(formData.dateTime(), screeningRoom);
        }

        return new ShowAdminRowDto(
                show.getId(),
                show.getMovie().getId(),
                show.getMovie().getTitle(),
                show.getDateTime(),
                show.getScreeningRoom().getId(),
                show.getScreeningRoom().getName(),
                ticketRepository.countByShowId(show.getId()),
                show.getScreeningRoom().getRows() * show.getScreeningRoom().getSeatsPerRow());
    }

    public void deleteShow(Long showId) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new IllegalArgumentException("Show not found."));
        if (ticketRepository.countByShowId(showId) > 0) {
            throw new IllegalStateException("Shows with sold tickets cannot be deleted.");
        }
        showRepository.delete(show);
    }

    private void validate(ShowAdminFormData formData) {
        if (formData.dateTime() == null) {
            throw new IllegalArgumentException("Show date and time are required.");
        }
        if (formData.dateTime().isBefore(LocalDateTime.now(clock))) {
            throw new IllegalArgumentException("Shows must be scheduled in the future.");
        }
        if (formData.screeningRoomId() == null) {
            throw new IllegalArgumentException("Screening room is required.");
        }
        if (formData.id() == null && formData.movieId() == null) {
            throw new IllegalArgumentException("Movie is required.");
        }
    }

    private ScreeningRoom loadScreeningRoom(Long screeningRoomId) {
        return screeningRoomRepository.findById(screeningRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Screening room is required."));
    }

    private void checkForOverlap(Long showId, Movie movie, ScreeningRoom screeningRoom, LocalDateTime candidateStart) {
        LocalDateTime candidateEnd = candidateStart.plusMinutes(movie.getDurationMinutes() + CLEANUP_BUFFER_MINUTES);
        boolean overlaps = showRepository.findAllByScreeningRoomIdOrderByDateTimeAsc(screeningRoom.getId()).stream()
                .filter(existing -> showId == null || !existing.getId().equals(showId))
                .anyMatch(existing -> {
                    LocalDateTime existingStart = existing.getDateTime();
                    LocalDateTime existingEnd =
                            existingStart.plusMinutes(existing.getMovie().getDurationMinutes() + CLEANUP_BUFFER_MINUTES);
                    return candidateStart.isBefore(existingEnd) && existingStart.isBefore(candidateEnd);
                });

        if (overlaps) {
            throw new IllegalStateException("This screening room already has an overlapping show.");
        }
    }
}
