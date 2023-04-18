package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder
public class Item {
    private Long id;
    @NotBlank
    @NotEmpty
    private String name;
    @NotBlank
    @NotEmpty
    private String description;
    @NotBlank
    @NotEmpty
    private String available;
    private Long owner;
    private String request;
}
