package ru.practicum.shareit.booking.status;

public enum BookingStatus {
    WAITING("Новое бронирование, ожидает подтверждения владельца."),
    APPROVED("Бронирование подтверждено владельцем."),
    REJECTED("Бронирование отклонено владельцем."),
    CANCELED("Бронирование отменено создателем.");

    public final String label;

    BookingStatus(String label) {
        this.label = label;
    }
}
