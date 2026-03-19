package com.example.specdriven.show;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@BrowserCallable
@Service
@AnonymousAllowed
public class ShowEndpoint {

    private final ShowRepository showRepository;
    private final TicketRepository ticketRepository;

    public ShowEndpoint(ShowRepository showRepository, TicketRepository ticketRepository) {
        this.showRepository = showRepository;
        this.ticketRepository = ticketRepository;
    }

    public record ShowDetail(Long id, String movieTitle, String dateTime, String roomName,
                             int rows, int seatsPerRow, List<SoldSeat> soldSeats) {
    }

    public record SoldSeat(int row, int seat) {
    }

    public record SeatRequest(int row, int seat) {
    }

    public record PurchaseRequest(Long showId, List<SeatRequest> seats,
                                  String customerName, String customerEmail) {
    }

    public record PurchaseResult(boolean success, String message, List<TicketInfo> tickets) {
    }

    public record TicketInfo(int row, int seat) {
    }

    public ShowDetail getShowDetail(Long showId) {
        Show show = showRepository.findById(showId).orElse(null);
        if (show == null) {
            return null;
        }

        List<Ticket> tickets = ticketRepository.findByShowId(showId);
        List<SoldSeat> soldSeats = tickets.stream()
                .map(t -> new SoldSeat(t.getSeatRow(), t.getSeatNumber()))
                .toList();

        return new ShowDetail(
                show.getId(),
                show.getMovie().getTitle(),
                show.getDateTime().toString(),
                show.getScreeningRoom().getName(),
                show.getScreeningRoom().getRows(),
                show.getScreeningRoom().getSeatsPerRow(),
                soldSeats
        );
    }

    @Transactional
    public PurchaseResult purchaseTickets(PurchaseRequest request) {
        if (request.seats() == null || request.seats().isEmpty()) {
            return new PurchaseResult(false, "No seats selected", List.of());
        }
        if (request.seats().size() > 6) {
            return new PurchaseResult(false, "Maximum 6 tickets per transaction", List.of());
        }
        if (request.customerName() == null || request.customerName().isBlank()) {
            return new PurchaseResult(false, "Name is required", List.of());
        }
        if (request.customerEmail() == null || !request.customerEmail().matches("^[^@]+@[^@]+\\.[^@]+$")) {
            return new PurchaseResult(false, "Valid email is required", List.of());
        }

        Show show = showRepository.findById(request.showId()).orElse(null);
        if (show == null) {
            return new PurchaseResult(false, "Show not found", List.of());
        }

        try {
            List<TicketInfo> ticketInfos = new ArrayList<>();
            for (SeatRequest seat : request.seats()) {
                Ticket ticket = new Ticket(seat.row(), seat.seat(),
                        request.customerName(), request.customerEmail(), show);
                ticketRepository.save(ticket);
                ticketInfos.add(new TicketInfo(seat.row(), seat.seat()));
            }
            return new PurchaseResult(true,
                    "Successfully purchased " + ticketInfos.size() + " ticket(s)!", ticketInfos);
        } catch (DataIntegrityViolationException e) {
            return new PurchaseResult(false,
                    "Some seats were already taken. Please refresh and try again.", List.of());
        }
    }
}
