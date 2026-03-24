package com.example.specdriven.ticket;

import com.example.specdriven.show.Show;
import com.example.specdriven.show.ShowRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ShowRepository showRepository;

    public TicketService(TicketRepository ticketRepository, ShowRepository showRepository) {
        this.ticketRepository = ticketRepository;
        this.showRepository = showRepository;
    }

    public List<SeatStatus> getSeatsForShow(Long showId) {
        Show show = showRepository.findById(showId).orElseThrow();
        List<Ticket> soldTickets = ticketRepository.findByShowId(showId);

        List<SeatStatus> seats = new ArrayList<>();
        for (int row = 1; row <= show.getScreeningRoom().getRows(); row++) {
            for (int seat = 1; seat <= show.getScreeningRoom().getSeatsPerRow(); seat++) {
                final int r = row, s = seat;
                boolean sold = soldTickets.stream()
                        .anyMatch(t -> t.getSeatRow() == r && t.getSeatNumber() == s);
                seats.add(new SeatStatus(row, seat, sold));
            }
        }
        return seats;
    }

    @Transactional
    public PurchaseResult purchaseTickets(Long showId, List<SeatSelection> seats,
                                          String customerName, String customerEmail) {
        if (customerName == null || customerName.isBlank()) {
            return new PurchaseResult(false, "Name is required", List.of());
        }
        if (customerEmail == null || customerEmail.isBlank()) {
            return new PurchaseResult(false, "Email is required", List.of());
        }
        if (!customerEmail.matches("^[^@]+@[^@]+\\.[^@]+$")) {
            return new PurchaseResult(false, "Please enter a valid email address", List.of());
        }
        if (seats == null || seats.isEmpty()) {
            return new PurchaseResult(false, "Please select at least one seat", List.of());
        }
        if (seats.size() > 6) {
            return new PurchaseResult(false, "Maximum 6 tickets per transaction", List.of());
        }

        Show show = showRepository.findById(showId).orElse(null);
        if (show == null) {
            return new PurchaseResult(false, "Show not found", List.of());
        }

        // Check for conflicts
        for (SeatSelection seat : seats) {
            if (ticketRepository.existsByShowIdAndSeatRowAndSeatNumber(
                    showId, seat.row(), seat.seat())) {
                return new PurchaseResult(false,
                        "Seat " + rowLabel(seat.row()) + seat.seat()
                                + " has already been sold. Please refresh and try again.",
                        List.of());
            }
        }

        // Create tickets
        List<TicketInfo> ticketInfos = new ArrayList<>();
        for (SeatSelection seat : seats) {
            Ticket ticket = new Ticket();
            ticket.setShow(show);
            ticket.setSeatRow(seat.row());
            ticket.setSeatNumber(seat.seat());
            ticket.setCustomerName(customerName);
            ticket.setCustomerEmail(customerEmail);
            ticketRepository.save(ticket);
            ticketInfos.add(new TicketInfo(seat.row(), seat.seat(),
                    rowLabel(seat.row()) + seat.seat()));
        }

        return new PurchaseResult(true,
                "Successfully purchased " + ticketInfos.size() + " ticket(s)!",
                ticketInfos);
    }

    private String rowLabel(int row) {
        return String.valueOf((char) ('A' + row - 1));
    }

    public record SeatStatus(int row, int seat, boolean sold) {}
    public record SeatSelection(int row, int seat) {}
    public record TicketInfo(int row, int seat, String label) {}
    public record PurchaseResult(boolean success, String message, List<TicketInfo> tickets) {}
}
