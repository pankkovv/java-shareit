package ru.practicum.shareit.booking.mapper;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class BookingMap {
    public static Booking mapToBooking(BookingDto bookingDto, Item item, User user) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd hh:mm:ss");
        LocalDateTime dateTimeStart = LocalDateTime.parse(bookingDto.getStart(), dateTimeFormatter);
        LocalDateTime dateTimeEnd = LocalDateTime.parse(bookingDto.getEnd(), dateTimeFormatter);
        return Booking.builder()
                .id(bookingDto.getId())
                .start(dateTimeStart)
                .end(dateTimeEnd)
                .item(item)
                .booker(user)
                .bookingStatus(bookingDto.getBookingStatus())
                .build();
    }

    public static BookingDto mapToBookingDto(Booking booking) {
        String dateTimeStart = DateTimeFormatter
                .ofPattern("yyyy.MM.dd hh:mm:ss")
                .format(booking.getStart());
        String dateTimeEnd = DateTimeFormatter
                .ofPattern("yyyy.MM.dd hh:mm:ss")
                .format(booking.getEnd());

        return BookingDto.builder()
                .id(booking.getId())
                .start(dateTimeStart)
                .end(dateTimeEnd)
                .itemId(booking.getItem().getId())
                .bookerId(booking.getBooker().getId())
                .bookingStatus(booking.getBookingStatus())
                .build();
    }

    public static List<BookingDto> mapToBookingDto(List<Booking> bookingList) {
        List<BookingDto> listBookingDto = new ArrayList<>();
        for (Booking booking : bookingList) {
            listBookingDto.add(mapToBookingDto(booking));
        }
        return listBookingDto;
    }
}
