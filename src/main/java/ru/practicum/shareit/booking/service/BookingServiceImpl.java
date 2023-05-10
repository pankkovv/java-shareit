package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShort;
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
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;


    @Override
    public BookingDto bookingItem(Long userId, BookingShort bookingShort) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotOwnerException("User not found."));
        Item item = itemRepository.findById(bookingShort.getItemId()).orElseThrow(() -> new NotFoundException("Item not found."));
        List<Booking> booking = bookingRepository.getBookingByBookerId(userId);
        if(!item.getOwner().equals(user)){
            BookingDto bookingDto = BookingDto.builder().start(bookingShort.getStart()).end(bookingShort.getEnd()).booker(user).item(item).status(BookingStatus.WAITING).build();
            validation(bookingDto);
            return BookingMap.mapToBookingDto(bookingRepository.save(BookingMap.mapToBooking(bookingDto, item, user)));
        } else {
            throw new NotFoundException("Item not found.");
        }
    }

    @Override
    public BookingDto bookingConfirm(Long userId, Long bookingId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Booking not found."));
        if (Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            if(booking.getBookingStatus().equals(BookingStatus.WAITING)){
                if (approved) {
                    booking.setBookingStatus(BookingStatus.APPROVED);
                } else {
                    booking.setBookingStatus(BookingStatus.REJECTED);
                }
                return BookingMap.mapToBookingDto(bookingRepository.save(booking));
            } else {
                throw new NotStateException("State is approved.");
            }
        } else {
            throw new NotFoundException("Item not found.");
        }
    }

    @Override
    public BookingDto getByIdBooking(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Booking not found."));
        if(booking.getBooker().getId() == userId || booking.getItem().getOwner().getId() == userId){
            return BookingMap.mapToBookingDto(bookingRepository.getByIdBooking(bookingId, userId));
        } else {
            throw new NotFoundException("Booking not found.");
        }
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
                    return BookingMap.mapToBookingDto(bookingRepository.getBookingByItemOwnerIdAndBookingStatusFuture(userId, BookingStatus.WAITING.label, BookingStatus.APPROVED.label));
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
