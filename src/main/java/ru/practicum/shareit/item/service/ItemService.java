package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item save(long userId, Item item);
    Item update(long itemId, Item item);
    Item getById(long itemId);
    List<Item> getAllItem(long userId);
    Item search(String text);
}
