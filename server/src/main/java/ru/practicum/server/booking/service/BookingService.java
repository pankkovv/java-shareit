package ru.practicum.server.booking.service;

import ru.practicum.server.booking.dto.BookingDto;
import ru.practicum.server.booking.dto.BookingShort;

import java.util.List;

public interface BookingService {
    BookingDto bookingAdd(Long userId, BookingShort bookingShort);

    BookingDto bookingConfirm(Long userId, Long itemId, boolean approved);

    BookingDto getByIdBooking(Long userId, Long bookingId);

    List<BookingDto> getByIdListBookings(Long userId, String state, Integer from, Integer size);

    List<BookingDto> getByIdOwnerBookingItems(Long userId, String state, Integer from, Integer size);

}
