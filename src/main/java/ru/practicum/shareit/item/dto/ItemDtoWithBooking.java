package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingWithoutDate;
import ru.practicum.shareit.booking.model.Booking;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class ItemDtoWithBooking {
    private Long id;
    private Long owner;
    private String name;
    private String description;
    private Boolean available;
    private Long request;
    private BookingWithoutDate lastBooking;
    private BookingWithoutDate nextBooking;
}
