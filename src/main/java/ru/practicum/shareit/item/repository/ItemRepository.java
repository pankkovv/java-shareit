package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    ItemDto addNewItem(Long userId, Item item);

    ItemDto updateItem(Long userId, Long itemId, Item item);

    ItemDto getItemById(Long itemId);

    List<ItemDto> getAllItemUserId(Long userId);

    List<ItemDto> searchItem(String text);
}
