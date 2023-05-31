package ru.practicum.shareit.booking.dto;

import java.util.Optional;

public enum BookingStateGateway {
    ALL("ALL"),
    CURRENT("CURRENT"),
    PAST("PAST"),
    FUTURE("FUTURE"),
    WAITING("WAITING"),
    REJECTED("REJECTED");

    public final String label;

    BookingStateGateway(String label) {
        this.label = label;
    }

    public static Optional<BookingStateGateway> from(String stringState) {
        for (BookingStateGateway state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
