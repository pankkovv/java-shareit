package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
interface ItemService {

    @Transactional
    List<ItemDto> getByUserId(Long userId);
    @Transactional
    public ItemDto getByItemId(Long itemId);
    @Transactional
    public List<ItemDto> search(String text);

    @Transactional
    public ItemDto saveItem(Long userId, ItemDto itemDto);

    @Transactional
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);
}
