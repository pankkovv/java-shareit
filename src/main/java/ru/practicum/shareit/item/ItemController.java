package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMap;
import ru.practicum.shareit.item.model.Item;
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
//    private final ItemService itemService;
//    private final ItemMap itemMap;

//    @GetMapping
//    public List<ItemDto> getByUserId(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
//        log.debug(LogMessages.TRY_GET.label, userId);
//        return itemService.getItems(userId);
//    }
//
//    @GetMapping("/{itemId}")
//    public ItemDto getByItemId(@PathVariable Long itemId) {
//        log.debug(LogMessages.TRY_GET_ID.label, itemId);
//        return itemService.getByItemId(itemId);
//    }
//
//    @GetMapping("/search")
//    public List<ItemDto> searchItem(@NotBlank @RequestParam String text) {
//        log.debug(LogMessages.TRY_GET_SEARCH.label, text);
//        return itemService.findByNameAndDescriptionContainingIgnoreCase(text);
//    }
//
//    @PostMapping
//    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemDto itemDto) {
//        log.debug(LogMessages.TRY_ADD.label, item);
//        return itemService.save(userId, item);
//    }
//
//    @PatchMapping("/{itemId}")
//    public ItemDto updateItem(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId, @RequestBody ItemDto itemDto) {
//        log.debug(LogMessages.TRY_UPDATE.label, item);
//        return itemService.update(userId, itemId, itemDto);
//    }
}
