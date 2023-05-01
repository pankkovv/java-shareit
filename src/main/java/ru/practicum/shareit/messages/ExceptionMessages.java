package ru.practicum.shareit.messages;

public enum ExceptionMessages {

    NOT_FOUND_USER("Пользователь не найден."),
    NOT_FOUND_ITEM("Запись о вещи не найдена."),
    DUPLICATE_EMAIL("Данный email уже зарегестрирован.");

    public final String label;

    ExceptionMessages(String label) {
        this.label = label;
    }
}
