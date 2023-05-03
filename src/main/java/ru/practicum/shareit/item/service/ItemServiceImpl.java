package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMap;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public List<ItemDto> getByUserId(Long userId) {
        return ItemMap.mapToItemDto(itemRepository.findItemsByOwnerId(userId));
    }

    @Override
    public ItemDto getByItemId(Long itemId) {
        return ItemMap.mapToItemDto(itemRepository.findById(itemId).orElseThrow(() -> new NotOwnerException("User not found.")));
    }

    @Override
    public List<ItemDto> search(String text) {
        return ItemMap.mapToItemDto(itemRepository.searchItemByNameAndDescription(text));
    }

    @Override
    public ItemDto saveItem(Long userId, ItemDto itemDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotOwnerException("User not found."));
        Item item = itemRepository.save(ItemMap.mapToItem(itemDto, user));
        return ItemMap.mapToItemDto(item);
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        return ItemMap.mapToItemDto(itemRepository.updateItemByOwnerIdAndId(userId, itemId));
    }
}
