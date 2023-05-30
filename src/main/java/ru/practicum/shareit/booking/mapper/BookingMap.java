package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingWithDate;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

public class BookingMap {
    public static Booking mapToBooking(BookingDto bookingDto, Item item, User user) {
        return Booking.builder()
                .id(bookingDto.getId())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .item(item)
                .booker(user)
                .bookingStatus(bookingDto.getStatus())
                .build();
    }

    public static BookingDto mapToBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(booking.getItem())
                .booker(booking.getBooker())
                .status(booking.getBookingStatus())
                .build();
    }

    public static List<BookingDto> mapToBookingDto(List<Booking> bookingList) {
        List<BookingDto> listBookingDto = new ArrayList<>();
        for (Booking booking : bookingList) {
            listBookingDto.add(mapToBookingDto(booking));
        }
        return listBookingDto;
    }

    public static BookingWithDate mapToBookingWithoutDate(Booking booking) {
        if (booking != null) {
            return BookingWithDate.builder()
                    .id(booking.getId())
                    .start(booking.getStart())
                    .end(booking.getEnd())
                    .status(booking.getBookingStatus())
                    .bookerId(booking.getBooker().getId())
                    .build();
        } else {
            return null;
        }
    }
}
