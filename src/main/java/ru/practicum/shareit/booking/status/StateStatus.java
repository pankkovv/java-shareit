package ru.practicum.shareit.booking.status;

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

    public static StateStatus valueOfLabel(String label) {
        for (StateStatus e : values()) {
            if (e.label.equals(label)) {
                return e;
            }
        }
        return null;
    }
}
