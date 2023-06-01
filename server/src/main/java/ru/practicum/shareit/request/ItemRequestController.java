package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.messages.LogMessages;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    ItemRequestDto addRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @RequestBody ItemRequestDto itemRequestDto) {
        log.debug(LogMessages.TRY_ADD_REQUEST.label, itemRequestDto);
        return itemRequestService.addRequest(userId, itemRequestDto);
    }

    @GetMapping
    List<ItemRequestDto> getRequest(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug(LogMessages.TRY_GET_REQUEST.label, userId);
        return itemRequestService.getRequest(userId);
    }

    @GetMapping("/all")
    List<ItemRequestDto> getRequestAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @RequestParam(required = false) Integer from,
                                       @RequestParam(required = false) Integer size) {
        log.debug(LogMessages.TRY_GET_REQUEST_ALL.label, userId);
        return itemRequestService.getRequestAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    ItemRequestDto getRequestId(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long requestId) {
        log.debug(LogMessages.TRY_GET_REQUEST_ID.label, requestId);
        return itemRequestService.getRequestId(userId, requestId);
    }
}
