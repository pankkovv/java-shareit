package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMap;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.booking.status.StateStatus;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingDto bookingItem(Long userId, BookingDto bookingDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotOwnerException("User not found."));
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() -> new NotFoundException("Item not found."));
        bookingDto.setBookerId(userId);
        return BookingMap.mapToBookingDto(bookingRepository.save(BookingMap.mapToBooking(bookingDto,item, user)));
    }

    @Override
    public BookingDto bookingConfirm(Long userId, Long itemId, boolean approved) {
        return null;
    }

    @Override
    public BookingDto getByIdBooking(Long userId, Long bookingId) {
        return null;
    }

    @Override
    public List<BookingDto> getByIdListBookings(Long userId, StateStatus stateStatus) {
        return null;
    }

    @Override
    public List<BookingDto> getByIdOwnerBookingItems(Long userId, StateStatus stateStatus) {
        return null;
    }
}
