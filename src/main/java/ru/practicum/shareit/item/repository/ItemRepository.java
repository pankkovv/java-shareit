package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    ItemDto addNewItem(long userId, Item item);

    ItemDto updateItem(long userId, long itemId, Item item);

    ItemDto getItemById(long itemId);

    List<ItemDto> getAllItemUserId(long userId);

    List<ItemDto> searchItem(String text);
}
