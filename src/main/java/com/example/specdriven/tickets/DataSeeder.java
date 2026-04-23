package com.example.specdriven.tickets;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class DataSeeder {

    private final TicketRepository ticketRepository;

    public DataSeeder(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @PostConstruct
    public void seed() {
        if (ticketRepository.count() > 0) {
            return;
        }

        Ticket[] tickets = {
            new Ticket(null, "Bus Single Ride", "One-way trip on any city bus route", Ticket.TransitMode.BUS, Ticket.TicketType.SINGLE_RIDE, new BigDecimal("2.50")),
            new Ticket(null, "Bus Day Pass", "Unlimited bus rides for one day", Ticket.TransitMode.BUS, Ticket.TicketType.DAY_PASS, new BigDecimal("7.00")),
            new Ticket(null, "Train Single Ride", "One-way trip on commuter rail", Ticket.TransitMode.TRAIN, Ticket.TicketType.SINGLE_RIDE, new BigDecimal("4.50")),
            new Ticket(null, "Train Day Pass", "Unlimited train rides for one day", Ticket.TransitMode.TRAIN, Ticket.TicketType.DAY_PASS, new BigDecimal("12.00")),
            new Ticket(null, "Metro Single Ride", "One-way trip on metro/subway", Ticket.TransitMode.METRO, Ticket.TicketType.SINGLE_RIDE, new BigDecimal("3.00")),
            new Ticket(null, "Metro Day Pass", "Unlimited metro rides for one day", Ticket.TransitMode.METRO, Ticket.TicketType.DAY_PASS, new BigDecimal("9.00")),
            new Ticket(null, "Ferry Single Ride", "One-way trip on ferry route", Ticket.TransitMode.FERRY, Ticket.TicketType.SINGLE_RIDE, new BigDecimal("5.00")),
            new Ticket(null, "Ferry Day Pass", "Unlimited ferry rides for one day", Ticket.TransitMode.FERRY, Ticket.TicketType.DAY_PASS, new BigDecimal("14.00"))
        };

        ticketRepository.saveAll(java.util.List.of(tickets));
    }
}
