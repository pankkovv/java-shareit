package ru.practicum.server.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.server.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@Builder
public class ItemRequestDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    private Long requestor;
    private List<ItemDto> items;
}
