package com.example.specdriven.catalog.application;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.specdriven.catalog.domain.Show;
import com.example.specdriven.catalog.domain.ShowRepository;
import com.example.specdriven.catalog.domain.Ticket;
import com.example.specdriven.catalog.domain.TicketRepository;

@Service
public class TicketPurchaseService {

    private static final int MAX_SEATS_PER_TRANSACTION = 6;
    private static final DateTimeFormatter DISPLAY_FORMATTER =
            DateTimeFormatter.ofPattern("EEEE, MMM d 'at' HH:mm");

    private final ShowRepository showRepository;
    private final TicketRepository ticketRepository;
    private final Clock clock;

    public TicketPurchaseService(ShowRepository showRepository, TicketRepository ticketRepository, Clock clock) {
        this.showRepository = showRepository;
        this.ticketRepository = ticketRepository;
        this.clock = clock;
    }

    @Transactional(readOnly = true)
    public ShowDetailsDto getShowDetails(long showId) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new IllegalArgumentException("Show not found: " + showId));

        List<SoldSeatDto> soldSeats = ticketRepository.findAllByShowId(showId).stream()
                .map(t -> new SoldSeatDto(t.getSeatRow(), t.getSeatNumber()))
                .toList();

        return new ShowDetailsDto(
                show.getId(),
                show.getMovie().getTitle(),
                show.getMovie().getId(),
                show.getDateTime().format(DISPLAY_FORMATTER),
                show.getScreeningRoom().getName(),
                show.getScreeningRoom().getRows(),
                show.getScreeningRoom().getSeatsPerRow(),
                soldSeats);
    }

    @Transactional
    public PurchaseResultDto purchaseTickets(long showId, PurchaseRequestDto request) {
        if (request.seats() == null || request.seats().isEmpty()) {
            return new PurchaseResultDto(false, "Please select at least one seat.", List.of());
        }
        if (request.seats().size() > MAX_SEATS_PER_TRANSACTION) {
            return new PurchaseResultDto(false,
                    "Maximum " + MAX_SEATS_PER_TRANSACTION + " tickets per transaction.", List.of());
        }
        if (request.customerName() == null || request.customerName().isBlank()) {
            return new PurchaseResultDto(false, "Name is required.", List.of());
        }
        if (request.customerEmail() == null || !request.customerEmail().matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            return new PurchaseResultDto(false, "A valid email address is required.", List.of());
        }

        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new IllegalArgumentException("Show not found: " + showId));

        for (SeatDto seat : request.seats()) {
            if (ticketRepository.existsByShowIdAndSeatRowAndSeatNumber(showId, seat.row(), seat.seatNumber())) {
                return new PurchaseResultDto(false,
                        "One or more seats were just taken by another purchase. Please refresh and try again.",
                        List.of());
            }
        }

        LocalDateTime now = LocalDateTime.now(clock);
        List<TicketDto> created = new ArrayList<>();
        for (SeatDto seat : request.seats()) {
            Ticket ticket = ticketRepository.save(new Ticket(
                    seat.row(),
                    seat.seatNumber(),
                    request.customerName(),
                    request.customerEmail(),
                    now,
                    show));
            created.add(new TicketDto(
                    ticket.getId(),
                    ticket.getSeatRow(),
                    ticket.getSeatNumber(),
                    show.getMovie().getTitle(),
                    show.getDateTime().format(DISPLAY_FORMATTER),
                    show.getScreeningRoom().getName()));
        }

        return new PurchaseResultDto(true, null, created);
    }
}
