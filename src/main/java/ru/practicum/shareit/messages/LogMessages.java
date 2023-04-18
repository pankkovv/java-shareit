package ru.practicum.shareit.messages;

public enum LogMessages {
    GET("Объект успешно получен: {}"),
    GET_ID("Объект успешно получен по id: {}"),
    ADD("Объект успешно добавлен: {}"),
    UPDATE("Объект успешно обновлен: {}"),
    DELETE("Объект успешно удален по id: {}"),
    SEARCH("Объект успешно найден: {}"),
    TRY_GET("Попытка получить объект: {}"),
    TRY_GET_ID("Попытка получить объект по id: {}"),
    TRY_ADD("Попытка добавить объект: {}"),
    TRY_UPDATE("Попытка обновить объект: {}"),
    TRY_GET_SEARCH("Попытка поиска объекта: text = {}"),
    TRY_DELETE("Попытка удалить объект: {}");
    private final String textLog;

    LogMessages(String textLog) {
        this.textLog = textLog;
    }

    @Override
    public String toString() {
        return textLog;
    }
    }
