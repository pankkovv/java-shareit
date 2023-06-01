package ru.practicum.server.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.server.booking.dto.BookingWithDate;
import ru.practicum.server.comment.dto.CommentDto;

import java.util.List;

@Data
@Builder
public class ItemDtoWithBookingAndComments {
    private Long id;
    private Long owner;
    private String name;
    private String description;
    private Boolean available;
    private Long request;
    private BookingWithDate lastBooking;
    private BookingWithDate nextBooking;
    private List<CommentDto> comments;
}
