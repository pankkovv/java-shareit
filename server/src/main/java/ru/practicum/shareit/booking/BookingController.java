package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.messages.LogMessages;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    BookingDto bookingItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @RequestBody BookingShort bookingShort) {
        log.debug(LogMessages.TRY_BOOKING_ITEM.label, bookingShort.getItemId());
        return bookingService.bookingAdd(userId, bookingShort);
    }

    @PatchMapping("/{bookingId}")
    BookingDto bookingConfirm(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable Long bookingId,
                              @RequestParam boolean approved) {
        log.debug(LogMessages.TRY_BOOKING_CONFIRM.label, bookingId);
        return bookingService.bookingConfirm(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    BookingDto getByIdBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable Long bookingId) {
        log.debug(LogMessages.TRY_BOOKING_ID.label, bookingId);
        return bookingService.getByIdBooking(userId, bookingId);
    }

    @GetMapping
    List<BookingDto> getByIdListBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestParam(required = false, defaultValue = "ALL") String state,
                                         @RequestParam (required = false) Integer from,
                                         @RequestParam (required = false) Integer size) {
        log.debug(LogMessages.TRY_BOOKING_USER_STATE.label, state);
        return bookingService.getByIdListBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    List<BookingDto> getByIdOwnerBookingItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam(required = false, defaultValue = "ALL") String state,
                                              @RequestParam (required = false) Integer from,
                                              @RequestParam (required = false) Integer size) {
        log.debug(LogMessages.TRY_BOOKING_OWNER_STATE.label, state);
        return bookingService.getByIdOwnerBookingItems(userId, state, from, size);
    }
}
