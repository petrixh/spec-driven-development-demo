package com.example.specdriven.tickets;

public enum TicketType {
    SINGLE_RIDE, DAY_PASS;

    public String displayName() {
        return switch (this) {
            case SINGLE_RIDE -> "Single Ride";
            case DAY_PASS -> "Day Pass";
        };
    }
}
