package ru.practicum.server.booking.status;

public enum StateStatus {
    ALL("ALL"),
    CURRENT("CURRENT"),
    PAST("PAST"),
    FUTURE("FUTURE"),
    WAITING("WAITING"),
    REJECTED("REJECTED");

    public final String label;

    StateStatus(String label) {
        this.label = label;
    }
}
