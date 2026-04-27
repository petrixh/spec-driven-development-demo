package com.example.specdriven.tickets;

public enum TransitMode {
    BUS, TRAIN, METRO, FERRY;

    public String displayName() {
        return switch (this) {
            case BUS -> "Bus";
            case TRAIN -> "Train";
            case METRO -> "Metro";
            case FERRY -> "Ferry";
        };
    }

    public String emoji() {
        return switch (this) {
            case BUS -> "🚌";
            case TRAIN -> "🚆";
            case METRO -> "🚇";
            case FERRY -> "⛴️";
        };
    }

    public String cssClass() {
        return name().toLowerCase();
    }
}
