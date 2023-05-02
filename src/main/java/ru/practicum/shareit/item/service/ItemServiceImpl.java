package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMap;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public List<ItemDto> getByUserId(Long userId) {
        return null;
    }

    @Override
    public ItemDto getByItemId(Long itemId) {
        Item item = itemRepository.getById(itemId);
        return ItemMap.mapToItemDto(item);
    }

    @Override
    public List<ItemDto> search(String text) {
        return ItemMap.mapToItemDto(itemRepository.searchItemByNameAndDescription(text));
    }

    @Override
    public ItemDto saveItem(Long userId, ItemDto itemDto) {
        User user = userRepository.getById(userId);
//                .orElseThrow(() -> new NotOwnerException("User not found."));
        Item item = itemRepository.save(ItemMap.mapToItem(itemDto, user));
        return ItemMap.mapToItemDto(item);
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
//        Item item = itemRepository.;
        return null;
    }
}
