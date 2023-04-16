package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;

@Component
@AllArgsConstructor
public class ItemServiceImpl implements ItemService{

    @Autowired
    private final ItemRepository itemRepository;

    @Override
    public Item save(long userId, Item item) {
        item.setUserId(userId);
        return itemRepository.addNewItem(item);
    }

    @Override
    public Item update(long itemId, Item item) {
        return itemRepository.updateItem(itemId, item);
    }

    @Override
    public Item getById(long itemId) {
        return itemRepository.getItemById(itemId);
    }

    @Override
    public List<Item> getAllItem(long userId) {
        return itemRepository.getAllItemUserId(userId);
    }

    @Override
    public Item search(String text) {
        return itemRepository.searchItem(text);
    }
}
