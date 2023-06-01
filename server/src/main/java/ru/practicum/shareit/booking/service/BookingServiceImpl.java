package ru.practicum.shareit.booking.service;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.mapper.BookingMap;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.booking.status.StateStatus;
import ru.practicum.shareit.exception.NotBookingException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotStateException;
import ru.practicum.shareit.exception.ValidException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.messages.LogMessages;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@NoArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;

    @Override
    public BookingDto bookingAdd(Long userId, BookingShort bookingShort) {
        User user = validationExistUser(userId);
        Item item = itemService.getById(bookingShort.getItemId());
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    @Override
    public List<BookingDto> getByIdListBookings(Long userId, String state, Integer from, Integer size) {
        Pageable page = paged(from, size);
        validationExistUser(userId);
        try {
            StateStatus stateStatus = StateStatus.valueOf(state);
            switch (stateStatus) {
                case ALL:
                    log.debug(LogMessages.BOOKING_USER_STATE.label, state);
                    return BookingMap.mapToBookingDto(bookingRepository.getBookingByBookerIdAll(userId, page));
                case CURRENT:
                    log.debug(LogMessages.BOOKING_USER_STATE.label, state);
                    return BookingMap.mapToBookingDto(bookingRepository.getBookingByUserIdAndBookingStatusCurrent(userId, LocalDateTime.now(), page));
                case PAST:
                    log.debug(LogMessages.BOOKING_USER_STATE.label, state);
                    return BookingMap.mapToBookingDto(bookingRepository.getBookingByUserIdAndBookingStatusPast(userId, LocalDateTime.now(), page));
                case FUTURE:
                    log.debug(LogMessages.BOOKING_USER_STATE.label, state);
                    return BookingMap.mapToBookingDto(bookingRepository.getBookingByUserIdAndBookingStatusFuture(userId, LocalDateTime.now(), page));
                case WAITING:
                    log.debug(LogMessages.BOOKING_USER_STATE.label, state);
                    return BookingMap.mapToBookingDto(bookingRepository.getBookingByUserIdAndBookingStatusWaiting(userId, BookingStatus.WAITING, page));
                case REJECTED:
                    log.debug(LogMessages.BOOKING_USER_STATE.label, state);
                    return BookingMap.mapToBookingDto(bookingRepository.getBookingByUserIdAndBookingStatusRejected(userId, BookingStatus.REJECTED, page));
                default:
                    return List.of();
            }
        } catch (IllegalArgumentException e) {
            throw new NotStateException(ExceptionMessages.UNKNOWN_STATE.label + state);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingDto> getByIdOwnerBookingItems(Long userId, String state, Integer from, Integer size) {
        Pageable page = paged(from, size);
        validationExistUser(userId);
        try {
            StateStatus stateStatus = StateStatus.valueOf(state);
            switch (stateStatus) {
                case ALL:
                    log.debug(LogMessages.BOOKING_OWNER_STATE.label, state);
                    return BookingMap.mapToBookingDto(bookingRepository.getBookingByOwnerIdAll(userId, page));
                case CURRENT:
                    log.debug(LogMessages.BOOKING_OWNER_STATE.label, state);
                    return BookingMap.mapToBookingDto(bookingRepository.getByItemOwnerIdAndBookingStatusCurrent(userId, LocalDateTime.now(), page));
                case PAST:
                    log.debug(LogMessages.BOOKING_OWNER_STATE.label, state);
                    return BookingMap.mapToBookingDto(bookingRepository.getByItemOwnerIdAndBookingStatusPast(userId, LocalDateTime.now(), page));
                case FUTURE:
                    log.debug(LogMessages.BOOKING_OWNER_STATE.label, state);
                    return BookingMap.mapToBookingDto(bookingRepository.getBookingByItemOwnerIdAndBookingStatusFuture(userId, LocalDateTime.now(), page));
                case WAITING:
                    log.debug(LogMessages.BOOKING_OWNER_STATE.label, state);
                    return BookingMap.mapToBookingDto(bookingRepository.getBookingByItemOwnerIdAndBookingStatusWaiting(userId, BookingStatus.WAITING, page));
                case REJECTED:
                    log.debug(LogMessages.BOOKING_OWNER_STATE.label, state);
                    return BookingMap.mapToBookingDto(bookingRepository.getBookingByItemOwnerIdAndBookingStatusRejected(userId, BookingStatus.REJECTED, page));
                default:
                    return List.of();
            }
        } catch (IllegalArgumentException e) {
            throw new NotStateException(ExceptionMessages.UNKNOWN_STATE.label + state);
        }
    }

    public void validation(BookingDto bookingDto) {
        if (bookingDto.getEnd().isBefore(bookingDto.getStart()) || bookingDto.getEnd().isEqual(bookingDto.getStart())) {
            throw new ValidException(ExceptionMessages.END_BEFORE_START.label);
        }
        if (!bookingDto.getItem().getAvailable()) {
            throw new ValidException(ExceptionMessages.ITEM_UNAVAILABLE.label);
        }
    }

    User validationExistUser(Long userId) {
        return userService.findById(userId);
    }

    public Pageable paged(Integer from, Integer size) {
        Pageable page;
        if (from != null && size != null) {
            if (from < 0 || size < 0) {
                throw new NotStateException(ExceptionMessages.FROM_NOT_POSITIVE.label);
            }
            page = PageRequest.of(from > 0 ? from / size : 0, size);
        } else {
            page = PageRequest.of(0, 4);
        }
        return page;
    }
}
