package com.example.specdriven.tickets;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class DataSeeder implements CommandLineRunner {
    private final TicketRepository ticketRepository;

    public DataSeeder(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public void run(String... args) {
        if (ticketRepository.count() > 0) return;

        ticketRepository.save(new Ticket("Bus Single Ride", "One-way trip on any city bus route", TransitMode.BUS, TicketType.SINGLE_RIDE, new BigDecimal("2.50")));
        ticketRepository.save(new Ticket("Bus Day Pass", "Unlimited bus rides for one full day", TransitMode.BUS, TicketType.DAY_PASS, new BigDecimal("7.00")));
        ticketRepository.save(new Ticket("Train Single Ride", "One-way journey on the city rail network", TransitMode.TRAIN, TicketType.SINGLE_RIDE, new BigDecimal("4.50")));
        ticketRepository.save(new Ticket("Train Day Pass", "Unlimited train travel for one full day", TransitMode.TRAIN, TicketType.DAY_PASS, new BigDecimal("12.00")));
        ticketRepository.save(new Ticket("Metro Single Ride", "Single trip on the metro underground", TransitMode.METRO, TicketType.SINGLE_RIDE, new BigDecimal("3.00")));
        ticketRepository.save(new Ticket("Metro Day Pass", "Unlimited metro rides for one full day", TransitMode.METRO, TicketType.DAY_PASS, new BigDecimal("9.00")));
        ticketRepository.save(new Ticket("Ferry Single Ride", "One-way crossing on the city ferry", TransitMode.FERRY, TicketType.SINGLE_RIDE, new BigDecimal("5.00")));
        ticketRepository.save(new Ticket("Ferry Day Pass", "Unlimited ferry crossings for one full day", TransitMode.FERRY, TicketType.DAY_PASS, new BigDecimal("14.00")));
    }
}
