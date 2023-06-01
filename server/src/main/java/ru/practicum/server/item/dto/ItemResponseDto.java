package ru.practicum.server.item.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemResponseDto {
    private Long id;
    private String name;
    private Long ownerId;
    private Long itemId;
    private Long requestId;
}
