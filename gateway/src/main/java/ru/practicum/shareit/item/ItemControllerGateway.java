package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDtoGateway;
import ru.practicum.shareit.item.dto.ItemDtoGateway;
import ru.practicum.shareit.messages.LogMessages;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * TODO Sprint add-controllers.
 */
@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemControllerGateway {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @Valid @RequestBody ItemDtoGateway itemDtogateway) {
        log.debug(LogMessages.TRY_ADD.label, itemDtogateway);
        return itemClient.addItem(userId, itemDtogateway);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@NotNull @RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable long itemId,
                                             @RequestBody @Valid CommentDtoGateway commentDtoGateway) {
        log.debug(LogMessages.TRY_COMMENT_ADD.label, itemId);
        return itemClient.addComment(userId, itemId, commentDtoGateway);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@NotNull @RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable long itemId,
                                             @RequestBody ItemDtoGateway itemDtogateway) {
        log.debug(LogMessages.TRY_UPDATE.label, itemDtogateway);
        return itemClient.upItem(userId, itemId, itemDtogateway);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestParam @NotBlank String text,
                                             @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                             @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.debug(LogMessages.TRY_GET_SEARCH.label, text);
        return itemClient.searchItem(text, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@NotNull @RequestHeader("X-Sharer-User-Id") long userId,
                                          @PathVariable long itemId) {
        log.debug(LogMessages.TRY_GET_ID.label, itemId);
        return itemClient.getItem(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getItems(@NotNull @RequestHeader("X-Sharer-User-Id") long userId,
                                           @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                           @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.debug(LogMessages.TRY_GET.label, userId);
        return itemClient.getItems(userId, from, size);
    }
}
