package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    @Autowired
    private final ItemRepository itemRepository;

    @Override
    public ItemDto save(long userId, Item item) {
        return itemRepository.addNewItem(userId, item);
    }

    @Override
    public ItemDto update(long userId, long itemId, Item item) {
        return itemRepository.updateItem(userId, itemId, item);
    }

    @Override
    public ItemDto getById(long itemId) {
        return itemRepository.getItemById(itemId);
    }

    @Override
    public List<ItemDto> getAllItem(long userId) {
        return itemRepository.getAllItemUserId(userId);
    }

    @Override
    public List<ItemDto> search(String text) {
        return itemRepository.searchItem(text);
    }
}
