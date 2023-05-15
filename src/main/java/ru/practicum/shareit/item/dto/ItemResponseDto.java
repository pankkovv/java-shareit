package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemResponseDto {
    private Long id;
    private Long itemId;
    private String title;
    private Long ownerId;
}
