package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.messages.LogMessages;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> getByUserId(@NotNull @RequestHeader("X-Sharer-User-Id") long userId) {
        log.debug(String.valueOf(LogMessages.TRY_GET), userId);
        return itemService.getAllItem(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getByItemId(@PathVariable long itemId) {
        log.debug(String.valueOf(LogMessages.TRY_GET_ID), itemId);
        return itemService.getById(itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@NotBlank @RequestParam String text) {
        log.debug(String.valueOf(LogMessages.TRY_GET_SEARCH), text);
        return itemService.search(text);
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") long userId, @Valid @RequestBody Item item) {
        log.debug(String.valueOf(LogMessages.TRY_ADD), item);
        return itemService.save(userId, item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@NotNull @RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId, @RequestBody Item item) {
        log.debug(String.valueOf(LogMessages.TRY_UPDATE), item);
        return itemService.update(userId, itemId, item);
    }
}
