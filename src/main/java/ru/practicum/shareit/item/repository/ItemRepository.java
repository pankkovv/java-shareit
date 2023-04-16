package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item addNewItem(Item item);
    Item updateItem(long itemId, Item item);
    Item getItemById(long itemId);
    List<Item> getAllItemUserId(long userId);
    Item searchItem(String text);
}
