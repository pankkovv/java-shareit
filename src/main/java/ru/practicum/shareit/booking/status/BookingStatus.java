package ru.practicum.shareit.booking.status;

public enum BookingStatus {
    WAITING("WAITING"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED"),
    CANCELED("CANCELED");

    public final String label;

    BookingStatus(String label) {
        this.label = label;
    }
}
