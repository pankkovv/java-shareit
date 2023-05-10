package ru.practicum.shareit.item.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;

import java.util.List;

@Transactional
public interface ItemService {

    List<ItemDtoWithBooking> getByUserId(Long userId);

    public ItemDtoWithBooking getByItemId(Long userId, Long itemId);

    public List<ItemDto> search(String text);

    public ItemDto saveItem(Long userId, ItemDto itemDto);

    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);
}
