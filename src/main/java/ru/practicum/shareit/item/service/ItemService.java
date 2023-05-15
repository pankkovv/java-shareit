package ru.practicum.shareit.item.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingAndComments;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Transactional
public interface ItemService {
    List<ItemDtoWithBookingAndComments> getByUserId(Long userId);

    ItemDtoWithBookingAndComments getByItemId(Long userId, Long itemId);

    List<ItemDto> search(String text);

    ItemDto saveItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);

    Item getById(Long itemId);
    List<ItemResponseDto> getRequestById(Long requestId);
}
