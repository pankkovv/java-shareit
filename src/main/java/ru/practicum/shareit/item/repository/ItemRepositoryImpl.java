package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ItemRepositoryImpl implements ItemRepository{
    private HashMap<Long, List<Item>> items = new HashMap<>();
    private long localId = 0;

    @Override
    public Item addNewItem(Item item) {
        item.setId(localId);
        localId++;
        items.compute(item.getUserId(), (userId, userItems) -> {
            if(userItems == null){
                userItems = new ArrayList<>();
            }
            userItems.add(item);
            return userItems;
        });
        return item;
    }

    @Override
    public Item updateItem(long itemId, Item item) {
//        if(!items.values().stream().flatMap(Collection::stream).filter(item1 -> item1.getId() == itemId).collect(Collectors.toList()).isEmpty()){
//
//        }
        items.compute(item.getUserId(), (userId, userItems) -> {
            if(!userItems.contains(item)){
                userItems.add(item);
                userItems.add(item);
            } else {
             userItems.stream().forEach(item1 -> item1 = item);
            }
            return userItems;
        });
        return item;
    }

    @Override
    public Item getItemById(long itemId) {
        return items.values().stream().flatMap(Collection::stream).filter(item -> item.getId() == itemId).findFirst().get();
    }

    @Override
    public List<Item> getAllItemUserId(long userId) {
        return items.getOrDefault(userId, Collections.emptyList());
    }

    @Override
    public Item searchItem(String text) {
        return items.values().stream().flatMap(Collection::stream).filter(item -> item.getName().toLowerCase().equals(text.toLowerCase()) || item.getDescription().toLowerCase().equals(text.toLowerCase())).findFirst().get();
    }
}
