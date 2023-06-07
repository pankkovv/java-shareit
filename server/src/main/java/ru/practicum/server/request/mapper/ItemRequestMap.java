package ru.practicum.server.request.mapper;

import lombok.NoArgsConstructor;
import ru.practicum.server.user.model.User;
import ru.practicum.server.request.dto.ItemRequestDto;
import ru.practicum.server.request.model.ItemRequest;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class ItemRequestMap {
    public static ItemRequest mapToItemRequest(ItemRequestDto itemRequestDto, User user) {
        return ItemRequest.builder()
                .id(itemRequestDto.getId())
                .description(itemRequestDto.getDescription())
                .requestor(user)
                .created(itemRequestDto.getCreated())
                .build();
    }

    public static ItemRequestDto mapToItemRequestDto(ItemRequest itemRequest) {
        if (itemRequest != null) {
            return ItemRequestDto.builder()
                    .id(itemRequest.getId())
                    .description(itemRequest.getDescription())
                    .requestor(itemRequest.getRequestor().getId())
                    .created(itemRequest.getCreated())
                    .build();
        } else {
            return null;
        }
    }

    public static List<ItemRequestDto> mapToItemRequestDto(List<ItemRequest> itemRequestList) {
        List<ItemRequestDto> itemRequestDtoList = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequestList) {
            itemRequestDtoList.add(mapToItemRequestDto(itemRequest));
        }
        return itemRequestDtoList;
    }
}
