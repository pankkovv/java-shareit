package ru.practicum.shareit.messages;

public enum LogMessages {
    GET("Объект успешно получен: {}"),
    GET_ID("Объект успешно получен по id: {}"),
    ADD("Объект успешно добавлен: {}"),
    UPDATE("Объект успешно обновлен: {}"),
    DELETE("Объект успешно удален по id: {}"),
    SEARCH("Объект успешно найден: {}"),
    BOOKING_ITEM("Успешное бронирование вещи: {}"),
    BOOKING_CONFIRM("Успешное подтверждение бронирования по id: {}"),
    BOOKING_ID("Успешное получение бронирования по id: {}"),
    BOOKING_USER_STATE("Успешное получение бронирования user по state: {}"),
    BOOKING_OWNER_STATE("Успешное получение бронирования owner по state: {}"),
    COMMENT_ADD("Успешное создание комментария к вещи: {}"),
    COMMENT_ID("Успешное получение комментария комментария по id: {}"),
    TRY_GET("Попытка получить объект: {}"),
    TRY_GET_ID("Попытка получить объект по id: {}"),
    TRY_ADD("Попытка добавить объект: {}"),
    TRY_UPDATE("Попытка обновить объект: {}"),
    TRY_GET_SEARCH("Попытка поиска объекта: text = {}"),
    TRY_DELETE("Попытка удалить объект: {}"),
    TRY_BOOKING_ITEM("Попытка забронировать вещь: {}"),
    TRY_BOOKING_CONFIRM("Попытка подтверждения бронирования по id: {}"),
    TRY_BOOKING_ID("Попытка получить бронирования по id: {}"),
    TRY_BOOKING_USER_STATE("Попытка получить бронирования user по state: {}"),
    TRY_BOOKING_OWNER_STATE("Попытка получить бронирования owner по state: {}"),
    TRY_COMMENT_ADD("Попытка создать комментарий к вещи: {}");


    public final String label;

    LogMessages(String label) {
        this.label = label;
    }
}
