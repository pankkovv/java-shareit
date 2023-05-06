package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.booking.status.StateStatus;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    @PostMapping
    BookingDto bookingItem(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @Valid BookingDto bookingDto){
        return bookingService.bookingItem(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    BookingDto bookingConfirm(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @NotNull @PathVariable Long bookingId, @RequestParam boolean approved){
        return null;
    }

    @GetMapping("/{bookingId}")
    BookingDto getByIdBooking(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId){
        return null;
    }

    @GetMapping
    List<BookingDto> getByIdListBookings(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam (required = false, defaultValue = "ALL") StateStatus stateStatus){
        return null;
    }

    @GetMapping("/owner")
    List<BookingDto> getByIdOwnerBookingItems(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam StateStatus stateStatus){
        return null;
    }
}
