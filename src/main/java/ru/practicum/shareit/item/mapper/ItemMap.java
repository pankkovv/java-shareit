package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMap {
    public ItemDto transferObj(Item item){
        return ItemDto.builder().id(item.getId()).name(item.getName()).description(item.getDescription()).available(item.isAvailable()).build();
    }
}
