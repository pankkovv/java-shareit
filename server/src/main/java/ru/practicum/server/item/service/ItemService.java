package ru.practicum.server.item.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.item.dto.ItemDto;
import ru.practicum.server.item.dto.ItemDtoWithBookingAndComments;
import ru.practicum.server.item.model.Item;

import java.util.List;

@Transactional
public interface ItemService {
    List<ItemDtoWithBookingAndComments> getByUserId(Long userId, Integer from, Integer size);

    ItemDtoWithBookingAndComments getByItemId(Long userId, Long itemId);

    List<ItemDto> search(String text, Integer from, Integer size);

    ItemDto saveItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);

    Item getById(Long itemId);

}
