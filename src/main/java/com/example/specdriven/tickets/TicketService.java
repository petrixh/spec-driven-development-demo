package com.example.specdriven.tickets;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    public List<Ticket> findByMode(TransitMode mode) {
        return ticketRepository.findByTransitMode(mode);
    }

    public Optional<Ticket> findById(Long id) {
        return ticketRepository.findById(id);
    }
}
