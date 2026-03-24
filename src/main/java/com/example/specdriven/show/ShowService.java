package com.example.specdriven.show;

import com.example.specdriven.ticket.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShowService {

    private final ShowRepository showRepository;
    private final TicketRepository ticketRepository;

    public ShowService(ShowRepository showRepository, TicketRepository ticketRepository) {
        this.showRepository = showRepository;
        this.ticketRepository = ticketRepository;
    }

    public List<Show> findAll() {
        return showRepository.findAll();
    }

    public Show findById(Long id) {
        return showRepository.findById(id).orElse(null);
    }

    @Transactional
    public Show save(Show show) {
        if (show.getMovie() == null) {
            throw new IllegalArgumentException("Movie is required");
        }
        if (show.getScreeningRoom() == null) {
            throw new IllegalArgumentException("Screening room is required");
        }
        if (show.getDateTime() == null) {
            throw new IllegalArgumentException("Date and time is required");
        }
        if (show.getDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Show must be scheduled in the future");
        }

        // Check for overlapping shows in the same room
        int durationWithBuffer = show.getMovie().getDurationMinutes() + 30;
        LocalDateTime showEnd = show.getDateTime().plusMinutes(durationWithBuffer);

        // Find shows in the same room on the same day (wide window) and check overlap in Java
        LocalDateTime dayStart = show.getDateTime().minusHours(12);
        LocalDateTime dayEnd = show.getDateTime().plusHours(12);
        List<Show> nearbyShows = showRepository.findShowsInRoomBetween(
                show.getScreeningRoom().getId(), dayStart, dayEnd);

        for (Show existing : nearbyShows) {
            if (show.getId() != null && show.getId().equals(existing.getId())) {
                continue; // Skip self when editing
            }
            int existingDuration = existing.getMovie().getDurationMinutes() + 30;
            LocalDateTime existingEnd = existing.getDateTime().plusMinutes(existingDuration);

            // Check overlap: two intervals overlap if start1 < end2 AND start2 < end1
            if (show.getDateTime().isBefore(existingEnd) && existing.getDateTime().isBefore(showEnd)) {
                throw new IllegalArgumentException(
                        "This show overlaps with '" + existing.getMovie().getTitle()
                                + "' at " + existing.getDateTime().toLocalTime()
                                + " in " + existing.getScreeningRoom().getName());
            }
        }

        return showRepository.save(show);
    }

    @Transactional
    public void delete(Show show) {
        if (ticketRepository.countByShowId(show.getId()) > 0) {
            throw new IllegalStateException("Cannot delete show with sold tickets");
        }
        showRepository.delete(show);
    }

    public long getTicketCount(Show show) {
        return ticketRepository.countByShowId(show.getId());
    }

    public long getTotalCapacity(Show show) {
        return (long) show.getScreeningRoom().getRows() * show.getScreeningRoom().getSeatsPerRow();
    }
}
