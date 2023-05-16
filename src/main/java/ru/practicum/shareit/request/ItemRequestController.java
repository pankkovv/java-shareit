package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    ItemRequestDto addRequest(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemRequestDto itemRequestDto){
        return itemRequestService.addRequest(userId, itemRequestDto);
    }

    @GetMapping
    List<ItemRequestDto> getRequest(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId){
        return itemRequestService.getRequest(userId);
    }

    @GetMapping("/all")
    List<ItemRequestDto> getRequestAll(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam (required = false) Integer from, @RequestParam (required = false) Integer size){
        return itemRequestService.getRequestAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    ItemRequestDto getRequestId(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long requestId){
        return itemRequestService.getRequestId(userId, requestId);
    }
}
