package ru.practicum.shareit.request;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    @PostMapping
    ItemRequestDto addRequest(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody ItemRequestDto ItemRequestDto){
        return null;
    }

    @GetMapping
    ItemRequestDto getRequest(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId){
        return null;
    }

    @GetMapping("/all")
    ItemRequestDto getRequestAll(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam (defaultValue = "0") Integer from, @RequestParam (defaultValue = "4") Integer size){
        return null;
    }

    @GetMapping("/{requestId}")
    ItemRequestDto getRequestId(@PathVariable Integer requestId){
        return null;
    }
}
