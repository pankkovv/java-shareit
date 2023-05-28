package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(
        properties = "spring.datasource.username=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTest {
    private final EntityManager em;
    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;

    @Test
    public void bookingAddTest() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusMinutes(1);
        User user = User.builder().id(1L).name("User").email("user@user.ru").build();
        User owner = User.builder().id(2L).name("Owner").email("owner@user.ru").build();
        UserDto userDto = UserDto.builder().id(1L).name("User").email("user@user.ru").build();
        UserDto userOwnerDto = UserDto.builder().id(2L).name("Owner").email("owner@user.ru").build();
        Item item = Item.builder().id(1L).owner(owner).name("Item").description("Item items").available(true).request(null).build();
        ItemDto itemDto = ItemDto.builder().name("Item").description("Item items").available(true).owner(owner.getId()).requestId(null).build();
        BookingShort bookingShort = BookingShort.builder().start(start).end(end).itemId(item.getId()).build();
        BookingDto bookingDto = BookingDto.builder().start(start).end(end).status(BookingStatus.WAITING).booker(user).item(item).build();

        userService.saveUser(userDto);
        userService.saveUser(userOwnerDto);
        itemService.saveItem(owner.getId(), itemDto);
        bookingService.bookingAdd(userDto.getId(), bookingShort);

        TypedQuery<Booking> query = em.createQuery("Select u from Booking u where u.booker.id = :bookerId", Booking.class);
        Booking booking = query.setParameter("bookerId", user.getId()).getSingleResult();

        MatcherAssert.assertThat(booking.getId(), notNullValue());
        MatcherAssert.assertThat(booking.getStart(), equalTo(bookingDto.getStart()));
        MatcherAssert.assertThat(booking.getEnd(), equalTo(bookingDto.getEnd()));
        MatcherAssert.assertThat(booking.getItem(), equalTo(bookingDto.getItem()));
        MatcherAssert.assertThat(booking.getBooker(), equalTo(bookingDto.getBooker()));
        MatcherAssert.assertThat(booking.getBookingStatus(), equalTo(bookingDto.getStatus()));
    }

    @Test
    public void bookingConfirmTest() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusMinutes(1);
        User user = User.builder().id(3L).name("User").email("user@user.ru").build();
        User owner = User.builder().id(4L).name("Owner").email("owner@user.ru").build();
        UserDto userDto = UserDto.builder().id(1L).name("User").email("user@user.ru").build();
        UserDto userOwnerDto = UserDto.builder().id(4L).name("Owner").email("owner@user.ru").build();
        Item item = Item.builder().id(2L).owner(owner).name("Item").description("Item items").available(true).request(null).build();
        ItemDto itemDto = ItemDto.builder().name("Item").description("Item items").available(true).owner(owner.getId()).requestId(null).build();
        BookingDto bookingDto = BookingDto.builder().start(start).end(end).status(BookingStatus.WAITING).booker(user).item(item).build();

        UserDto userDtoDb = userService.saveUser(userDto);
        UserDto ownerDtoDb = userService.saveUser(userOwnerDto);
        ItemDto itemDtoDb = itemService.saveItem(ownerDtoDb.getId(), itemDto);
        BookingShort bookingShort = BookingShort.builder().start(start).end(end).itemId(itemDtoDb.getId()).build();
        BookingDto bookingDtoDb = bookingService.bookingAdd(userDtoDb.getId(), bookingShort);
        bookingService.bookingConfirm(ownerDtoDb.getId(), bookingDtoDb.getId(), true);

        TypedQuery<Booking> query = em.createQuery("Select u from Booking u where u.booker.id = :bookerId", Booking.class);
        Booking booking = query.setParameter("bookerId", userDtoDb.getId()).getSingleResult();

        MatcherAssert.assertThat(booking.getId(), notNullValue());
        MatcherAssert.assertThat(booking.getStart(), equalTo(bookingDto.getStart()));
        MatcherAssert.assertThat(booking.getEnd(), equalTo(bookingDto.getEnd()));
        MatcherAssert.assertThat(booking.getItem(), equalTo(bookingDto.getItem()));
        MatcherAssert.assertThat(booking.getBooker(), equalTo(bookingDto.getBooker()));
        MatcherAssert.assertThat(booking.getBookingStatus(), equalTo(BookingStatus.APPROVED));
    }
}
