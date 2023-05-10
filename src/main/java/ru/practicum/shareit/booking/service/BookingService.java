package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.StateStatus;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface BookingService {

    public BookingDto bookingItem(Long userId, BookingShort bookingShort);

    public BookingDto bookingConfirm(Long userId, Long itemId, boolean approved);

    public BookingDto getByIdBooking(Long userId, Long bookingId);

    public List<BookingDto> getByIdListBookings(Long userId, String state);

    public List<BookingDto> getByIdOwnerBookingItems(Long userId, String state);
}
