package ru.practicum.gateway.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.gateway.item.dto.ItemDtoGateway;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@Builder
public class ItemRequestDtoGateway {
    private Long id;
    @NotBlank
    private String description;
    private LocalDateTime created;
    private Long requestor;
    private List<ItemDtoGateway> items;
}
