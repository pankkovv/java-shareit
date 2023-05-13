package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.mapper.BookingMap;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.booking.status.StateStatus;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.messages.LogMessages;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    @Override
    public BookingDto bookingAdd(Long userId, BookingShort bookingShort) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotOwnerException(ExceptionMessages.NOT_FOUND_USER.label));
        Item item = itemRepository.findById(bookingShort.getItemId())
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.NOT_FOUND_ITEM.label));
        if (!item.getOwner().equals(user)) {
            BookingDto bookingDto = BookingDto.builder()
                    .start(bookingShort.getStart())
                    .end(bookingShort.getEnd())
                    .booker(user)
                    .item(item)
                    .status(BookingStatus.WAITING)
                    .build();
            validation(bookingDto);
            log.debug(LogMessages.BOOKING_ITEM.label, item.getId());
            return BookingMap.mapToBookingDto(bookingRepository.save(BookingMap.mapToBooking(bookingDto, item, user)));
        } else {
            throw new NotFoundException(ExceptionMessages.NOT_FOUND_ITEM.label);
        }
    }

    @Override
    public BookingDto bookingConfirm(Long userId, Long bookingId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException(ExceptionMessages.NOT_FOUND_BOOKING.label));
        if (Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            if (booking.getBookingStatus().equals(BookingStatus.WAITING)) {
                if (approved) {
                    booking.setBookingStatus(BookingStatus.APPROVED);
                } else {
                    booking.setBookingStatus(BookingStatus.REJECTED);
                }
                log.debug(LogMessages.BOOKING_CONFIRM.label, bookingId);
                return BookingMap.mapToBookingDto(bookingRepository.save(booking));
            } else {
                throw new NotStateException(ExceptionMessages.APPROVED_STATE.label);
            }
        } else {
            throw new NotBookingException(ExceptionMessages.NOT_FOUND_BOOKING.label);
        }
    }

    @Override
    public BookingDto getByIdBooking(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotBookingException(ExceptionMessages.NOT_FOUND_BOOKING.label));
        if (Objects.equals(booking.getBooker().getId(), userId) || Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            log.debug(LogMessages.BOOKING_ID.label, bookingId);
            return BookingMap.mapToBookingDto(bookingRepository.getByIdBooking(bookingId, userId));
        } else {
            throw new NotBookingException(ExceptionMessages.NOT_FOUND_BOOKING.label);
        }
    }

    @Override
    public List<BookingDto> getByIdListBookings(Long userId, String state) {
        validationExistUser(userId);
        try{
        StateStatus stateStatus = StateStatus.valueOf(state);
            switch (stateStatus) {
                case ALL:
                    log.debug(LogMessages.BOOKING_USER_STATE.label, state);
                    return BookingMap.mapToBookingDto(bookingRepository.getBookingByBookerIdAll(userId));
                case CURRENT:
                    log.debug(LogMessages.BOOKING_USER_STATE.label, state);
                    return BookingMap.mapToBookingDto(bookingRepository.getBookingByUserIdAndBookingStatusCurrent(userId, LocalDateTime.now()));
                case PAST:
                    log.debug(LogMessages.BOOKING_USER_STATE.label, state);
                    return BookingMap.mapToBookingDto(bookingRepository.getBookingByUserIdAndBookingStatusPast(userId, LocalDateTime.now()));
                case FUTURE:
                    log.debug(LogMessages.BOOKING_USER_STATE.label, state);
                    return BookingMap.mapToBookingDto(bookingRepository.getBookingByUserIdAndBookingStatusFuture(userId, LocalDateTime.now()));
                case WAITING:
                    log.debug(LogMessages.BOOKING_USER_STATE.label, state);
                    return BookingMap.mapToBookingDto(bookingRepository.getBookingByUserIdAndBookingStatusWaiting(userId, BookingStatus.WAITING));
                case REJECTED:
                    log.debug(LogMessages.BOOKING_USER_STATE.label, state);
                    return BookingMap.mapToBookingDto(bookingRepository.getBookingByUserIdAndBookingStatusRejected(userId, BookingStatus.REJECTED));
                default:
                    return List.of();
            }
        } catch (IllegalArgumentException e){
            throw new NotStateException(ExceptionMessages.UNKNOWN_STATE.label + state);
        }
    }

    @Override
    public List<BookingDto> getByIdOwnerBookingItems(Long userId, String state) {
        validationExistUser(userId);
        try{
        StateStatus stateStatus = StateStatus.valueOf(state);
            switch (stateStatus) {
                case ALL:
                    log.debug(LogMessages.BOOKING_OWNER_STATE.label, state);
                    return BookingMap.mapToBookingDto(bookingRepository.getBookingByOwnerIdAll(userId));
                case CURRENT:
                    log.debug(LogMessages.BOOKING_OWNER_STATE.label, state);
                    return BookingMap.mapToBookingDto(bookingRepository.getByItemOwnerIdAndBookingStatusCurrent(userId, LocalDateTime.now()));
                case PAST:
                    log.debug(LogMessages.BOOKING_OWNER_STATE.label, state);
                    return BookingMap.mapToBookingDto(bookingRepository.getByItemOwnerIdAndBookingStatusPast(userId, LocalDateTime.now()));
                case FUTURE:
                    log.debug(LogMessages.BOOKING_OWNER_STATE.label, state);
                    return BookingMap.mapToBookingDto(bookingRepository.getBookingByItemOwnerIdAndBookingStatusFuture(userId, LocalDateTime.now()));
                case WAITING:
                    log.debug(LogMessages.BOOKING_OWNER_STATE.label, state);
                    return BookingMap.mapToBookingDto(bookingRepository.getBookingByItemOwnerIdAndBookingStatusWaiting(userId, BookingStatus.WAITING));
                case REJECTED:
                    log.debug(LogMessages.BOOKING_OWNER_STATE.label, state);
                    return BookingMap.mapToBookingDto(bookingRepository.getBookingByItemOwnerIdAndBookingStatusRejected(userId, BookingStatus.REJECTED));
                default:
                    return List.of();
            }
        } catch (IllegalArgumentException e){
            throw new NotStateException(ExceptionMessages.UNKNOWN_STATE.label + state);
        }
    }

    void validation(BookingDto bookingDto) {
        if (bookingDto.getEnd().isBefore(bookingDto.getStart()) || bookingDto.getEnd().isEqual(bookingDto.getStart())) {
            throw new ValidException(ExceptionMessages.END_BEFORE_START.label);
        }
        if (!bookingDto.getItem().getAvailable()) {
            throw new ValidException(ExceptionMessages.ITEM_UNAVAILABLE.label);
        }
    }

    void validationExistUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotOwnerException(ExceptionMessages.NOT_FOUND_USER.label));
    }
}
