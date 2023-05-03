package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.status.BookingStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-bookings.
 */
@Data
@Builder
public class BookingDto {
    private Long id;
    @NotNull
    private String start;
    @NotNull
    private String end;
    @NotNull
    private Long item;
    @NotNull
    private Long booker;
    @NotBlank
    @NotEmpty
    private BookingStatus bookingStatus;
}
