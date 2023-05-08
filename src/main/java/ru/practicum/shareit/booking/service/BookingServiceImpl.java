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
import ru.practicum.shareit.exception.NotStateException;
import ru.practicum.shareit.exception.ValidException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

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
        bookingDto.setStatus(BookingStatus.WAITING);
        bookingDto.setItem(item);
        validation(bookingDto);
        return BookingMap.mapToBookingDto(bookingRepository.save(BookingMap.mapToBooking(bookingDto, item, user)));
    }

    @Override
    public BookingDto bookingConfirm(Long userId, Long bookingId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Booking not found."));
        if (Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            if (approved) {
                booking.setBookingStatus(BookingStatus.APPROVED);
            } else {
                booking.setBookingStatus(BookingStatus.REJECTED);
            }
            return BookingMap.mapToBookingDto(bookingRepository.save(booking));
        } else {
            throw new NotFoundException("Item not found.");
        }
    }

    @Override
    public BookingDto getByIdBooking(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Booking not found."));
        return BookingMap.mapToBookingDto(bookingRepository.getByIdBooking(bookingId, userId));
    }

    @Override
    public List<BookingDto> getByIdListBookings(Long userId, String state) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotOwnerException("user not found."));
        StateStatus stateStatus = StateStatus.valueOfLabel(state);
        if (stateStatus != null) {
            switch (stateStatus) {
                case ALL:
                    return BookingMap.mapToBookingDto(bookingRepository.getBookingByBookerId(userId));
                case CURRENT:
                    return BookingMap.mapToBookingDto(bookingRepository.getBookingByUserIdAndBookingStatus(userId, BookingStatus.APPROVED.label));
                case PAST:
                    return BookingMap.mapToBookingDto(bookingRepository.getBookingByUserIdAndBookingStatus(userId, BookingStatus.CANCELED.label));
                case FUTURE:
                    return BookingMap.mapToBookingDto(bookingRepository.getBookingByUserIdAndBookingStatusFuture(userId, BookingStatus.WAITING.label, BookingStatus.APPROVED.label));
                case WAITING:
                    return BookingMap.mapToBookingDto(bookingRepository.getBookingByUserIdAndBookingStatus(userId, BookingStatus.WAITING.label));
                case REJECTED:
                    return BookingMap.mapToBookingDto(bookingRepository.getBookingByUserIdAndBookingStatus(userId, BookingStatus.REJECTED.label));
                default:
                    return List.of();
            }
        } else {
            throw new NotStateException("Unknown state: " + state);
        }
    }

    @Override
    public List<BookingDto> getByIdOwnerBookingItems(Long userId, String state) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user not found."));
        StateStatus stateStatus = StateStatus.valueOfLabel(state);
        if (stateStatus != null) {
            switch (stateStatus) {
                case ALL:
                    return BookingMap.mapToBookingDto(bookingRepository.getBookingByOwnerId(userId));
                case CURRENT:
                    return BookingMap.mapToBookingDto(bookingRepository.getByItemOwnerIdAndBookingStatus(userId, BookingStatus.APPROVED.label));
                case PAST:
                    return BookingMap.mapToBookingDto(bookingRepository.getByItemOwnerIdAndBookingStatus(userId, BookingStatus.CANCELED.label));
                case FUTURE:
                    return BookingMap.mapToBookingDto(bookingRepository.getBookingByItemOwnerIdAndBookingStatusFuture(userId,  BookingStatus.WAITING.label, BookingStatus.APPROVED.label));
                case WAITING:
                    return BookingMap.mapToBookingDto(bookingRepository.getByItemOwnerIdAndBookingStatus(userId, BookingStatus.WAITING.label));
                case REJECTED:
                    return BookingMap.mapToBookingDto(bookingRepository.getByItemOwnerIdAndBookingStatus(userId, BookingStatus.REJECTED.label));
                default:
                    return List.of();
            }
        } else {
            throw new NotStateException("Unknown state: " + state);
        }
    }

    void validation(BookingDto bookingDto) {
        if (bookingDto.getEnd().isBefore(bookingDto.getStart()) || bookingDto.getEnd().isEqual(bookingDto.getStart())) {
            throw new ValidException("End time is before start time.");
        }
        if (!bookingDto.getItem().getAvailable()) {
            throw new ValidException("The item is unavailable.");
        }
    }
}
