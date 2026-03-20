package com.example.specdriven.catalog.application;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.specdriven.catalog.domain.Show;
import com.example.specdriven.catalog.domain.ShowRepository;
import com.example.specdriven.catalog.domain.Ticket;
import com.example.specdriven.catalog.domain.TicketRepository;

@Service
public class ShowBookingService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    private final ShowRepository showRepository;
    private final TicketRepository ticketRepository;
    private final Clock clock;

    public ShowBookingService(ShowRepository showRepository, TicketRepository ticketRepository, Clock clock) {
        this.showRepository = showRepository;
        this.ticketRepository = ticketRepository;
        this.clock = clock;
    }

    @Transactional(readOnly = true)
    public ShowSeatSelectionDto getShowSeatSelection(long showId) {
        Show show = loadShow(showId);

        return new ShowSeatSelectionDto(
                show.getId(),
                show.getMovie().getId(),
                show.getMovie().getTitle(),
                show.getScreeningRoom().getName(),
                show.getDateTime().toLocalDate().format(DATE_FORMAT),
                show.getDateTime().toLocalTime().format(TIME_FORMAT),
                show.getScreeningRoom().getRows(),
                show.getScreeningRoom().getSeatsPerRow(),
                ticketRepository.findAllByShowIdOrderBySeatRowAscSeatNumberAsc(showId).stream()
                        .map(ticket -> new SeatDto(ticket.getSeatRow(), ticket.getSeatNumber(), true))
                        .toList());
    }

    @Transactional
    public TicketConfirmationDto purchaseTickets(PurchaseTicketsRequest request) {
        Show show = loadShow(request.showId());

        if (show.getDateTime().isBefore(LocalDateTime.now(clock))) {
            throw new IllegalArgumentException("This show has already started.");
        }

        List<SelectedSeatDto> seats = uniqueSeats(request.seats());
        validateSeatCount(seats);
        validateSeatBounds(show, seats);

        for (SelectedSeatDto seat : seats) {
            if (ticketRepository.existsByShowIdAndSeatRowAndSeatNumber(show.getId(), seat.row(), seat.number())) {
                throw new BookingConflictException("One or more selected seats were just sold. Please try again.");
            }
        }

        try {
            ticketRepository.saveAll(seats.stream()
                    .map(seat -> new Ticket(
                            seat.row(),
                            seat.number(),
                            request.customerName().trim(),
                            request.customerEmail().trim(),
                            LocalDateTime.now(clock),
                            show))
                    .toList());
        } catch (DataIntegrityViolationException exception) {
            throw new BookingConflictException("One or more selected seats were just sold. Please try again.");
        }

        return new TicketConfirmationDto(
                show.getId(),
                show.getMovie().getId(),
                show.getMovie().getTitle(),
                show.getScreeningRoom().getName(),
                show.getDateTime().toLocalDate().format(DATE_FORMAT),
                show.getDateTime().toLocalTime().format(TIME_FORMAT),
                request.customerName().trim(),
                request.customerEmail().trim(),
                seats.stream().map(this::formatSeatLabel).toList());
    }

    private Show loadShow(long showId) {
        return showRepository.findById(showId)
                .orElseThrow(() -> new IllegalArgumentException("Show not found: " + showId));
    }

    private void validateSeatBounds(Show show, List<SelectedSeatDto> seats) {
        int maxRows = show.getScreeningRoom().getRows();
        int maxSeatsPerRow = show.getScreeningRoom().getSeatsPerRow();
        boolean invalidSeat = seats.stream()
                .anyMatch(seat -> seat.row() > maxRows || seat.number() > maxSeatsPerRow);

        if (invalidSeat) {
            throw new IllegalArgumentException("One or more selected seats are invalid for this room.");
        }
    }

    private void validateSeatCount(List<SelectedSeatDto> seats) {
        if (seats.isEmpty() || seats.size() > 6) {
            throw new IllegalArgumentException("You can purchase between 1 and 6 seats at a time.");
        }
    }

    private List<SelectedSeatDto> uniqueSeats(List<SelectedSeatDto> seats) {
        Set<String> seen = new LinkedHashSet<>();
        List<SelectedSeatDto> uniqueSeats = seats.stream()
                .filter(seat -> seen.add(seat.row() + ":" + seat.number()))
                .toList();

        if (uniqueSeats.size() != seats.size()) {
            throw new IllegalArgumentException("Duplicate seats cannot be purchased.");
        }

        return uniqueSeats;
    }

    private String formatSeatLabel(SelectedSeatDto seat) {
        return rowLabel(seat.row()) + seat.number();
    }

    private String rowLabel(int row) {
        return String.valueOf((char) ('A' + row - 1));
    }
}
