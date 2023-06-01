package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.exception.NotBookingException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotStateException;
import ru.practicum.shareit.exception.ValidException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

class BookingServiceImplTest {
    static BookingServiceImpl bookingService = new BookingServiceImpl();
    static BookingRepository bookingRepository = Mockito.mock(BookingRepository.class);
    static UserService userService = Mockito.mock(UserService.class);
    static ItemService itemService = Mockito.mock(ItemService.class);

    static User user;
    static User owner;
    static Item item;

    static BookingDto bookingDto;
    static Booking booking;
    static LocalDateTime start;
    static LocalDateTime end;


    @BeforeAll
    static void assistant() {
        ReflectionTestUtils.setField(bookingService, "bookingRepository", bookingRepository);
        ReflectionTestUtils.setField(bookingService, "userService", userService);
        ReflectionTestUtils.setField(bookingService, "itemService", itemService);

        user = User.builder().id(1L).name("User").email("user@user.ru").build();
        owner = User.builder().id(2L).name("Owner").email("owner@user.ru").build();
        item = Item.builder().id(1L).owner(owner).name("Item").description("Item items").available(true).request(null).build();

        start = LocalDateTime.of(2020,5,5,14,25,11);
        end = LocalDateTime.of(2025,5,5,14,27,11);

        bookingDto = BookingDto.builder().start(start).end(end).status(BookingStatus.WAITING).booker(user).item(item).build();
        booking = Booking.builder().start(start).end(end).item(item).booker(user).bookingStatus(BookingStatus.WAITING).build();
    }


    //normal behavior
    @Test
    public void bookingAddTest() {
        BookingShort bookingShort = BookingShort.builder().start(start).end(end).itemId(1L).build();

        Mockito.when(bookingRepository.save(booking))
                .thenReturn(booking);

        Mockito.when(userService.findById(anyLong()))
                .thenReturn(user);

        Mockito.when(itemService.getById(anyLong()))
                .thenReturn(item);

        Assertions.assertEquals(bookingDto, bookingService.bookingAdd(user.getId(), bookingShort));
    }

    @Test
    public void bookingConfirmTest() {
        Item itemTwo = Item.builder().id(1L).owner(owner).name("Item").description("Item items").available(true).request(null).build();

        Booking bookingApproved = Booking.builder().id(1L).start(start).end(end).item(item).booker(user).bookingStatus(BookingStatus.APPROVED).build();
        Booking bookingRejected = Booking.builder().id(2L).start(start).end(end).item(itemTwo).booker(user).bookingStatus(BookingStatus.REJECTED).build();

        Optional<Booking> bookingOptional = Optional.of(Booking.builder().id(1L).start(start).end(end).item(item).booker(user).bookingStatus(BookingStatus.WAITING).build());
        Optional<Booking> bookingOptionalTwo = Optional.of(Booking.builder().id(2L).start(start).end(end).item(item).booker(user).bookingStatus(BookingStatus.WAITING).build());

        BookingDto bookingDtoApproved = BookingDto.builder().id(1L).start(start).end(end).status(BookingStatus.APPROVED).booker(user).item(item).build();
        BookingDto bookingDtoRejected = BookingDto.builder().id(2L).start(start).end(end).status(BookingStatus.REJECTED).booker(user).item(item).build();


        Mockito.when(bookingRepository.findById(1L))
                .thenReturn(bookingOptional);
        Mockito.when(bookingRepository.findById(2L))
                .thenReturn(bookingOptionalTwo);

        Mockito.when(bookingRepository.save(bookingApproved))
                .thenReturn(bookingApproved);
        Mockito.when(bookingRepository.save(bookingRejected))
                .thenReturn(bookingRejected);


        Assertions.assertEquals(bookingDtoApproved, bookingService.bookingConfirm(owner.getId(), 1L, true));
        Assertions.assertEquals(bookingDtoRejected, bookingService.bookingConfirm(owner.getId(), 2L, false));
    }

    @Test
    void getByIdBookingTest() {
        Optional<Booking> bookingOptional = Optional.of(Booking.builder().id(1L).start(start).end(end).item(item).booker(user).bookingStatus(BookingStatus.WAITING).build());

        Mockito.when(bookingRepository.findById(1L))
                .thenReturn(bookingOptional);
        Mockito.when(bookingRepository.getByIdBooking(1L, 1L))
                .thenReturn(booking);

        Assertions.assertEquals(bookingDto, bookingService.getByIdBooking(1L, 1L));
    }

    @Test
    void getByIdListBookingsTest() {
        List<BookingDto> listBookingDto = List.of(BookingDto.builder().start(start).end(end).status(BookingStatus.WAITING).booker(user).item(item).build());
        List<Booking> listBooking = List.of(Booking.builder().start(start).end(end).item(item).booker(user).bookingStatus(BookingStatus.WAITING).build());

        Mockito.when(bookingRepository.getBookingByBookerIdAll(1L, Pageable.ofSize(4)))
                .thenReturn(listBooking);
        Mockito.when(bookingRepository.getBookingByUserIdAndBookingStatusCurrent(anyLong(), any(), any()))
                .thenReturn(listBooking);
        Mockito.when(bookingRepository.getBookingByUserIdAndBookingStatusPast(anyLong(), any(), any()))
                .thenReturn(listBooking);
        Mockito.when(bookingRepository.getBookingByUserIdAndBookingStatusFuture(anyLong(), any(), any()))
                .thenReturn(listBooking);
        Mockito.when(bookingRepository.getBookingByUserIdAndBookingStatusWaiting(1L, BookingStatus.WAITING, Pageable.ofSize(4)))
                .thenReturn(listBooking);
        Mockito.when(bookingRepository.getBookingByUserIdAndBookingStatusRejected(1L, BookingStatus.REJECTED, Pageable.ofSize(4)))
                .thenReturn(listBooking);

        Assertions.assertEquals(listBookingDto, bookingService.getByIdListBookings(1L, "ALL", 0, 4));
        Assertions.assertEquals(listBookingDto, bookingService.getByIdListBookings(1L, "CURRENT", 0, 4));
        Assertions.assertEquals(listBookingDto, bookingService.getByIdListBookings(1L, "PAST", 0, 4));
        Assertions.assertEquals(listBookingDto, bookingService.getByIdListBookings(1L, "FUTURE", 0, 4));
        Assertions.assertEquals(listBookingDto, bookingService.getByIdListBookings(1L, "WAITING", 0, 4));
        Assertions.assertEquals(listBookingDto, bookingService.getByIdListBookings(1L, "REJECTED", 0, 4));
    }

    @Test
    void getByIdOwnerBookingItemsTest() {
        List<BookingDto> listBookingDto = List.of(BookingDto.builder().start(start).end(end).status(BookingStatus.WAITING).booker(user).item(item).build());
        List<Booking> listBooking = List.of(Booking.builder().start(start).end(end).item(item).booker(user).bookingStatus(BookingStatus.WAITING).build());

        Mockito.when(bookingRepository.getBookingByOwnerIdAll(1L, Pageable.ofSize(4)))
                .thenReturn(listBooking);
        Mockito.when(bookingRepository.getByItemOwnerIdAndBookingStatusCurrent(anyLong(), any(), any()))
                .thenReturn(listBooking);
        Mockito.when(bookingRepository.getByItemOwnerIdAndBookingStatusPast(anyLong(), any(), any()))
                .thenReturn(listBooking);
        Mockito.when(bookingRepository.getBookingByItemOwnerIdAndBookingStatusFuture(anyLong(), any(), any()))
                .thenReturn(listBooking);
        Mockito.when(bookingRepository.getBookingByItemOwnerIdAndBookingStatusWaiting(1L, BookingStatus.WAITING, Pageable.ofSize(4)))
                .thenReturn(listBooking);
        Mockito.when(bookingRepository.getBookingByItemOwnerIdAndBookingStatusRejected(1L, BookingStatus.REJECTED, Pageable.ofSize(4)))
                .thenReturn(listBooking);

        Assertions.assertEquals(listBookingDto, bookingService.getByIdOwnerBookingItems(1L, "ALL", 0, 4));
        Assertions.assertEquals(listBookingDto, bookingService.getByIdOwnerBookingItems(1L, "CURRENT", 0, 4));
        Assertions.assertEquals(listBookingDto, bookingService.getByIdOwnerBookingItems(1L, "PAST", 0, 4));
        Assertions.assertEquals(listBookingDto, bookingService.getByIdOwnerBookingItems(1L, "FUTURE", 0, 4));
        Assertions.assertEquals(listBookingDto, bookingService.getByIdOwnerBookingItems(1L, "WAITING", 0, 4));
        Assertions.assertEquals(listBookingDto, bookingService.getByIdOwnerBookingItems(1L, "REJECTED", 0, 4));
    }

    @Test
    void pagedTest() {
        Pageable page = PageRequest.of(0, 4);
        Assertions.assertEquals(page, bookingService.paged(0, 4));
    }

    //Reaction to erroneous data
    @Test
    public void bookingAddErrTest() {
        BookingShort bookingShort = BookingShort.builder().start(start).end(end).itemId(1L).build();

        Mockito.when(bookingRepository.save(booking))
                .thenReturn(booking);

        Mockito.when(userService.findById(anyLong()))
                .thenReturn(owner);

        Mockito.when(itemService.getById(anyLong()))
                .thenReturn(item);
        final NotFoundException exception = assertThrows(NotFoundException.class, () -> bookingService.bookingAdd(user.getId(), bookingShort));

        Assertions.assertEquals(exception.getMessage(), ExceptionMessages.NOT_FOUND_ITEM.label);
    }

    @Test
    public void bookingConfirmErrTest() {
        Optional<Booking> bookingOptional = Optional.of(Booking.builder().id(1L).start(start).end(end).item(item).booker(user).bookingStatus(BookingStatus.APPROVED).build());

        Mockito.when(bookingRepository.findById(1L))
                .thenReturn(bookingOptional);

        final NotBookingException exception = assertThrows(NotBookingException.class, () -> bookingService.bookingConfirm(user.getId(), 1L, true));
        final NotStateException exceptionTwo = assertThrows(NotStateException.class, () -> bookingService.bookingConfirm(owner.getId(), 1L, true));


        Assertions.assertEquals(exception.getMessage(), ExceptionMessages.NOT_FOUND_BOOKING.label);
        Assertions.assertEquals(exceptionTwo.getMessage(), ExceptionMessages.APPROVED_STATE.label);
    }

    @Test
    void getByIdBookingErrTest() {
        Optional<Booking> bookingOptional = Optional.of(Booking.builder().id(1L).start(start).end(end).item(item).booker(user).bookingStatus(BookingStatus.WAITING).build());

        Mockito.when(bookingRepository.findById(1L))
                .thenReturn(bookingOptional);
        Mockito.when(bookingRepository.getByIdBooking(1L, 1L))
                .thenReturn(booking);

        final NotBookingException exception = assertThrows(NotBookingException.class, () -> bookingService.getByIdBooking(3L, 1L));

        Assertions.assertEquals(exception.getMessage(), ExceptionMessages.NOT_FOUND_BOOKING.label);
    }

    @Test
    void getByIdListErrBookingsTest() {
        List<Booking> listBooking = List.of(Booking.builder().start(start).end(end).item(item).booker(user).bookingStatus(BookingStatus.WAITING).build());

        Mockito.when(bookingRepository.getBookingByBookerIdAll(1L, Pageable.ofSize(4)))
                .thenReturn(listBooking);

        final NotStateException exception = assertThrows(NotStateException.class, () -> bookingService.getByIdListBookings(1L, "STATE", 0, 4));

        Assertions.assertEquals(exception.getMessage(), ExceptionMessages.UNKNOWN_STATE.label + "STATE");
    }

    @Test
    void getByIdOwnerBookingItemsErrTest() {
        List<Booking> listBooking = List.of(Booking.builder().start(start).end(end).item(item).booker(user).bookingStatus(BookingStatus.WAITING).build());

        Mockito.when(bookingRepository.getBookingByOwnerIdAll(1L, Pageable.ofSize(4)))
                .thenReturn(listBooking);

        final NotStateException exception = assertThrows(NotStateException.class, () -> bookingService.getByIdOwnerBookingItems(1L, "STATE", 0, 4));

        Assertions.assertEquals(exception.getMessage(), ExceptionMessages.UNKNOWN_STATE.label + "STATE");
    }

    @Test
    void pagedErrTest() {
        final NotStateException exception = assertThrows(NotStateException.class, () -> bookingService.paged(-1, 4));

        Assertions.assertEquals(exception.getMessage(), ExceptionMessages.FROM_NOT_POSITIVE.label);
    }

    @Test
    void validationTest() {
        Item itemErr = Item.builder().id(1L).owner(owner).name("Item").description("Item items").available(false).request(null).build();
        BookingDto bookingDtoErrOne = BookingDto.builder().start(end).end(start).status(BookingStatus.WAITING).booker(user).item(item).build();
        BookingDto bookingDtoErrTwo = BookingDto.builder().start(start).end(end).status(BookingStatus.WAITING).booker(user).item(itemErr).build();

        final ValidException exceptionOne = assertThrows(ValidException.class, () -> bookingService.validation(bookingDtoErrOne));
        final ValidException exceptionTwo = assertThrows(ValidException.class, () -> bookingService.validation(bookingDtoErrTwo));

        Assertions.assertEquals(exceptionOne.getMessage(), ExceptionMessages.END_BEFORE_START.label);
        Assertions.assertEquals(exceptionTwo.getMessage(), ExceptionMessages.ITEM_UNAVAILABLE.label);
    }
}