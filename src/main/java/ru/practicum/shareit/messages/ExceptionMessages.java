package ru.practicum.shareit.messages;

import javax.persistence.criteria.From;

public enum ExceptionMessages {

    NOT_FOUND_USER("Пользователь не найден."),
    NOT_FOUND_ITEM("Запись о вещи не найдена."),
    NOT_FOUND_BOOKING("Бронирование для вещи не найдено."),
    NOT_FOUND_REQUEST("Запрос не найден"),
    DUPLICATE_EMAIL("Данный email уже зарегестрирован."),
    APPROVED_STATE("Данное бронирование уже подтверждено владельцем вещи."),
    UNKNOWN_STATE("Unknown state: "),
    END_BEFORE_START("Время окончания бронирования раньше времени старта."),
    ITEM_UNAVAILABLE("Вещь для бронирования недоступна."),
    ITEM_NOT_BOOKING("Вещь еще не бронировали."),
    FROM_NOT_POSITIVE("Номер первого элемента неможет быть отрицательным.");

    public final String label;

    ExceptionMessages(String label) {
        this.label = label;
    }
}
