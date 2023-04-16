package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<Item> getByUserId(@RequestHeader("X-Later-User-Id") long userId) {
        return itemService.getAllItem(userId);
    }

    @GetMapping("/{itemId}")
    public Item getByItemId(@PathVariable long itemId){
        return itemService.getById(itemId);
    }

    @GetMapping("/search")
    public Item searchItem(@RequestParam String text){
        return itemService.search(text);
    }

    @PostMapping
    public Item add(@RequestHeader("X-Later-User-Id") Long userId,
                    @RequestBody Item item) {
        return itemService.save(userId, item);
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@PathVariable long itemId, Item item){
        return itemService.update(itemId, item);
    }
}
