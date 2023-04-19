package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemMap {
    public ItemDto transferToObj(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .owner(item.getOwner())
                .name(item.getName())
                .description(item.getDescription())
                .available(Boolean.parseBoolean(item.getAvailable()))
                .build();
    }

    public List<ItemDto> transferToObj(List<Item> listItem) {
        List<ItemDto> listItemDto = new ArrayList<>();
        listItem.forEach(item -> listItemDto.add(ItemDto.builder()
                .id(item.getId())
                .owner(item.getOwner())
                .name(item.getName())
                .description(item.getDescription())
                .available(Boolean.parseBoolean(item.getAvailable()))
                .build()));
        return listItemDto;
    }

    public Item transferFromObj(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .owner(itemDto.getOwner())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(String.valueOf(itemDto.getAvailable()))
                .build();
    }

}
