package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder
public class Item {
    private long id;
    private long userId;
    private String name;
    private String description;
    private boolean available;
}
