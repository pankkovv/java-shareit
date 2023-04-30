package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMap;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.messages.LogMessages;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ItemRepositoryImpl implements ItemRepository {
    private final ItemMap itemMap;
    private final UserRepository userRepository;
    private final HashMap<Long, List<Item>> items = new HashMap<>();
    private long localId = 1;

    @Override
    public ItemDto addNewItem(Long userId, Item item) {
        userRepository.getById(userId);
        item.setOwner(userId);
        item.setId(generateId());
        items.compute(item.getOwner(), (ownerId, userItems) -> {
            if (userItems == null) {
                userItems = new ArrayList<>();
            }
            userItems.add(item);
            return userItems;
        });
        log.debug(LogMessages.ADD.label, item);
        return itemMap.transferObj(item);
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, Item item) {
        item.setId(itemId);
        item.setOwner(userId);
        items.values().stream().flatMap(Collection::stream).forEach(lastItem -> {
            if (Objects.equals(lastItem.getId(), itemId)) {
                validate(lastItem, item);
                updater(lastItem, item);
            }
        });
        log.debug(LogMessages.UPDATE.label, getItemById(itemId));
        return getItemById(itemId);
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        log.debug(LogMessages.TRY_GET_ID.label, itemId);
        return itemMap.transferObj(items.values().stream().flatMap(Collection::stream).filter(item -> item.getId().equals(itemId)).findFirst().orElseThrow(() -> new NotFoundException(ExceptionMessages.NOT_FOUND_ITEM.label)));
    }

    @Override
    public List<ItemDto> getAllItemUserId(Long userId) {
        log.debug(LogMessages.GET.label, userId);
        return itemMap.transferObj(items.getOrDefault(userId, Collections.emptyList()));
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        if (!text.isEmpty()) {
            List<Item> searchItem = items.values().stream().flatMap(Collection::stream).filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase()) || item.getDescription().toLowerCase().contains(text.toLowerCase())) && item.getAvailable().equals("true")).collect(Collectors.toList());
            log.debug(LogMessages.SEARCH.label, searchItem);
            return itemMap.transferObj(searchItem);
        } else {
            return List.of();
        }
    }

    private long generateId() {
        return localId++;
    }

    private void validate(Item lastItem, Item newItem) {
        userRepository.getById(newItem.getOwner());
        if (!Objects.equals(lastItem.getOwner(), newItem.getOwner())) {
            throw new NotFoundException(ExceptionMessages.NOT_FOUND_ITEM.label);
        }
    }

    private void updater(Item lastItem, Item newItem) {
        if (newItem.getName() != null) {
            lastItem.setName(newItem.getName());
        }
        if (newItem.getDescription() != null) {
            lastItem.setDescription(newItem.getDescription());
        }
        if (newItem.getAvailable() != null) {
            lastItem.setAvailable(newItem.getAvailable());
        }
    }
}
