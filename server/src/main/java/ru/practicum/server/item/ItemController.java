package ru.practicum.server.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.comment.dto.CommentDto;
import ru.practicum.server.comment.dto.CommentShort;
import ru.practicum.server.comment.service.CommentService;
import ru.practicum.server.item.dto.ItemDto;
import ru.practicum.server.item.dto.ItemDtoWithBookingAndComments;
import ru.practicum.server.item.service.ItemService;
import ru.practicum.server.messages.LogMessages;

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
    public List<ItemDtoWithBookingAndComments> getByUserId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                           @RequestParam(required = false) Integer from,
                                                           @RequestParam(required = false) Integer size) {
        log.debug(LogMessages.TRY_GET.label, userId);
        return itemService.getByUserId(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ItemDtoWithBookingAndComments getByItemId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @PathVariable Long itemId) {
        log.debug(LogMessages.TRY_GET_ID.label, itemId);
        return itemService.getByItemId(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text, @RequestParam(required = false) Integer from,
                                    @RequestParam(required = false) Integer size) {
        log.debug(LogMessages.TRY_GET_SEARCH.label, text);
        return itemService.search(text, from, size);
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @RequestBody ItemDto itemDto) {
        log.debug(LogMessages.TRY_ADD.label, itemDto);
        return itemService.saveItem(userId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable Long itemId,
                                 @RequestBody CommentShort commentShort) {
        log.debug(LogMessages.TRY_COMMENT_ADD.label, itemId);
        return commentService.addComment(userId, itemId, commentShort);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable Long itemId,
                              @RequestBody ItemDto itemDto) {
        log.debug(LogMessages.TRY_UPDATE.label, itemDto);
        return itemService.updateItem(userId, itemId, itemDto);
    }
}
