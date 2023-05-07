package ru.practicum.shareit.booking.status;

public enum StateStatus {
    ALL("Все"),
    CURRENT("Текущие"),
    PAST("Завершенные"),
    FUTURE("Будущие"),
    WAITING("Ожидающие подтверждения"),
    REJECTED("Отклоненные");

    public final String label;

    StateStatus(String label) {
        this.label = label;
    }
}
