package com.example.specdriven.tickets;

public enum TransitMode {
    BUS("Bus", "🚌", "bus"),
    TRAIN("Train", "🚆", "train"),
    METRO("Metro", "🚇", "metro"),
    FERRY("Ferry", "⛴️", "ferry");

    private final String displayName;
    private final String emoji;
    private final String cssKey;

    TransitMode(String displayName, String emoji, String cssKey) {
        this.displayName = displayName;
        this.emoji = emoji;
        this.cssKey = cssKey;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmoji() {
        return emoji;
    }

    public String getCssKey() {
        return cssKey;
    }
}
