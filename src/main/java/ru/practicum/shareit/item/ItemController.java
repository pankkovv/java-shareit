package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentShort;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.comment.service.CommentService;
import ru.practicum.shareit.item.dto.ItemDto;

import ru.practicum.shareit.item.dto.ItemDtoWithBookingAndComments;
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
    private final CommentService commentService;
    @GetMapping
    public List<ItemDtoWithBookingAndComments> getByUserId(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug(LogMessages.TRY_GET.label, userId);
        return itemService.getByUserId(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoWithBookingAndComments getByItemId(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        log.debug(LogMessages.TRY_GET_ID.label, itemId);
        return itemService.getByItemId(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@NotBlank @RequestParam String text) {
        log.debug(LogMessages.TRY_GET_SEARCH.label, text);
        return itemService.search(text);
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemDto itemDto) {
        log.debug(LogMessages.TRY_ADD.label, itemDto);
        return itemService.saveItem(userId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId, @Valid @RequestBody CommentShort commentShort){
        return commentService.addComment(userId, itemId, commentShort);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId, @RequestBody ItemDto itemDto) {
        log.debug(LogMessages.TRY_UPDATE.label, itemDto);
        return itemService.updateItem(userId, itemId, itemDto);
    }
}
