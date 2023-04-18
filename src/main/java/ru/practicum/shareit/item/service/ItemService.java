package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto save(long userId, Item item);

    ItemDto update(long userId, long itemId, Item item);

    ItemDto getById(long itemId);

    List<ItemDto> getAllItem(long userId);

    List<ItemDto> search(String text);
}
