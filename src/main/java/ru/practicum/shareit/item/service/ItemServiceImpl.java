package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMap;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public List<ItemDto> getByUserId(Long userId) {
        return ItemMap.mapToItemDto(itemRepository.findByOwnerId(userId));
    }

    @Override
    public ItemDto getByItemId(Long itemId) {
        return ItemMap.mapToItemDto(itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Item not found.")));
    }

    @Override
    public List<ItemDto> search(String text) {
        if(!text.isEmpty()){
            return ItemMap.mapToItemDto(itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable(text, text, true));
        } else {
            return List.of();
        }
    }

    @Override
    public ItemDto saveItem(Long userId, ItemDto itemDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotOwnerException("User not found."));
        return ItemMap.mapToItemDto(itemRepository.save(ItemMap.mapToItem(itemDto, user)));
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotOwnerException("User not found."));
        ItemDto itemDtoOld = getByItemId(itemId);
        if(itemDto.getName() != null){
            itemDtoOld.setName(itemDto.getName());
        }
        if(itemDto.getDescription() != null){
            itemDtoOld.setDescription(itemDto.getDescription());
        }
        if(itemDto.getAvailable() != null){
            itemDtoOld.setAvailable(itemDto.getAvailable());
        }
        if(itemDto.getRequest() != null){
            itemDtoOld.setRequest(itemDto.getRequest());
        }
        return saveItem(userId, itemDtoOld);
    }
}
