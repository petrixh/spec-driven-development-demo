package com.example.specdriven.tickets;

public enum TicketType {
    SINGLE_RIDE("Single Ride"),
    DAY_PASS("Day Pass");

    private final String displayName;

    TicketType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
