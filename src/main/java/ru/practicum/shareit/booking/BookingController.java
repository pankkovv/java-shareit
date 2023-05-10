package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.booking.status.StateStatus;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    @PostMapping
    BookingDto bookingItem(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody BookingShort bookingShort){
        return bookingService.bookingItem(userId, bookingShort);
    }

    @PatchMapping("/{bookingId}")
    BookingDto bookingConfirm(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @NotNull @PathVariable Long bookingId, @RequestParam boolean approved){
        return bookingService.bookingConfirm(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    BookingDto getByIdBooking(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId){
        return bookingService.getByIdBooking(userId, bookingId);
    }

    @GetMapping
    List<BookingDto> getByIdListBookings(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam (required = false, defaultValue = "ALL") String state){
        return bookingService.getByIdListBookings(userId, state);
    }

    @GetMapping("/owner")
    List<BookingDto> getByIdOwnerBookingItems(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam (required = false, defaultValue = "ALL") String state){
        return bookingService.getByIdOwnerBookingItems(userId, state);
    }
}
