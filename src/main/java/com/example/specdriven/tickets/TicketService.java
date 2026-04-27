package com.example.specdriven.tickets;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll().stream()
                .sorted(java.util.Comparator.comparing(Ticket::getTransitMode)
                        .thenComparing(Ticket::getName, java.util.Comparator.reverseOrder()))
                .toList();
    }

    public List<Ticket> getTicketsByMode(Ticket.TransitMode mode) {
        return ticketRepository.findByTransitMode(mode);
    }

    public List<Ticket.TransitMode> getAllModes() {
        return List.of(Ticket.TransitMode.BUS, Ticket.TransitMode.TRAIN, Ticket.TransitMode.METRO, Ticket.TransitMode.FERRY);
    }

    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id).orElse(null);
    }
}
