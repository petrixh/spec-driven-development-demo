package com.example.specdriven.tickets;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
class DataSeeder implements CommandLineRunner {

    private final TicketRepository ticketRepository;

    DataSeeder(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public void run(String... args) {
        if (ticketRepository.count() > 0) {
            return;
        }
        ticketRepository.saveAll(List.of(
                new Ticket("Bus Single Ride", "One-way trip on any city bus route.",
                        TransitMode.BUS, TicketType.SINGLE_RIDE, new BigDecimal("2.50")),
                new Ticket("Bus Day Pass", "Unlimited bus rides for one day.",
                        TransitMode.BUS, TicketType.DAY_PASS, new BigDecimal("7.00")),
                new Ticket("Train Single Ride", "One-way commuter train trip.",
                        TransitMode.TRAIN, TicketType.SINGLE_RIDE, new BigDecimal("4.50")),
                new Ticket("Train Day Pass", "Unlimited train rides for one day.",
                        TransitMode.TRAIN, TicketType.DAY_PASS, new BigDecimal("12.00")),
                new Ticket("Metro Single Ride", "One-way metro trip across the city.",
                        TransitMode.METRO, TicketType.SINGLE_RIDE, new BigDecimal("3.00")),
                new Ticket("Metro Day Pass", "Unlimited metro rides for one day.",
                        TransitMode.METRO, TicketType.DAY_PASS, new BigDecimal("9.00")),
                new Ticket("Ferry Single Ride", "One-way ferry crossing.",
                        TransitMode.FERRY, TicketType.SINGLE_RIDE, new BigDecimal("5.00")),
                new Ticket("Ferry Day Pass", "Unlimited ferry rides for one day.",
                        TransitMode.FERRY, TicketType.DAY_PASS, new BigDecimal("14.00"))
        ));
    }
}
