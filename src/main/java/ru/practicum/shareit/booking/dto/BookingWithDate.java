package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.status.BookingStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingWithDate {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status;
    private Long bookerId;
    private Long itemId;
}
