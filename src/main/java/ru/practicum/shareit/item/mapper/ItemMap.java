package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemMap {
    public ItemDto transferObj(Item item) {
        return ItemDto.builder().id(item.getId()).owner(item.getOwner()).name(item.getName()).description(item.getDescription()).available(Boolean.parseBoolean(item.getAvailable())).build();
    }

    public List<ItemDto> transferObj(List<Item> listItem) {
        List<ItemDto> listItemDto = new ArrayList<>();
        listItem.stream().forEach(item -> {
            listItemDto.add(ItemDto.builder().id(item.getId()).owner(item.getOwner()).name(item.getName()).description(item.getDescription()).available(Boolean.parseBoolean(item.getAvailable())).build());
        });
        return listItemDto;
    }
}
