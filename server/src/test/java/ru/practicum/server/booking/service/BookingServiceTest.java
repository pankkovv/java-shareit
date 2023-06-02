package ru.practicum.server.booking.service;

import lombok.RequiredArgsConstructor;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.server.booking.dto.BookingDto;
import ru.practicum.server.booking.dto.BookingShort;
import ru.practicum.server.booking.model.Booking;
import ru.practicum.server.booking.status.BookingStatus;
import ru.practicum.server.item.dto.ItemDto;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.item.service.ItemService;
import ru.practicum.server.user.dto.UserDto;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.service.UserService;

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

    LocalDateTime start;
    LocalDateTime end;
    User user;
    User owner;
    UserDto userDto;
    UserDto userOwnerDto;
    Item item;
    ItemDto itemDto;
    BookingShort bookingShort;
    BookingDto bookingDto;
    UserDto userDtoDb;
    UserDto ownerDtoDb;
    ItemDto itemDtoDb;

    @BeforeEach
    void assistant() {
        start = LocalDateTime.now();
        end = LocalDateTime.now().plusMinutes(1);
        user = User.builder().id(1L).name("User").email("user@user.ru").build();
        owner = User.builder().id(2L).name("Owner").email("owner@user.ru").build();
        userDto = UserDto.builder().id(1L).name("User").email("user@user.ru").build();
        userOwnerDto = UserDto.builder().id(2L).name("Owner").email("owner@user.ru").build();
        item = Item.builder().id(1L).owner(owner).name("Item").description("Item items").available(true).request(null).build();
        itemDto = ItemDto.builder().name("Item").description("Item items").available(true).owner(owner.getId()).requestId(null).build();
        bookingShort = BookingShort.builder().start(start).end(end).itemId(item.getId()).build();
        bookingDto = BookingDto.builder().start(start).end(end).status(BookingStatus.WAITING).booker(user).item(item).build();
        userDtoDb = userService.saveUser(userDto);
        ownerDtoDb = userService.saveUser(userOwnerDto);
        itemDtoDb = itemService.saveItem(ownerDtoDb.getId(), itemDto);
    }

    @Test
    public void bookingAddTest() {
        bookingService.bookingAdd(userDtoDb.getId(), bookingShort);

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
        BookingShort bookingShort = BookingShort.builder().start(start).end(end).itemId(itemDtoDb.getId()).build();
        BookingDto bookingDtoDb = bookingService.bookingAdd(userDtoDb.getId(), bookingShort);
        bookingService.bookingConfirm(ownerDtoDb.getId(), bookingDtoDb.getId(), true);

        TypedQuery<Booking> query = em.createQuery("Select u from Booking u where u.booker.id = :bookerId", Booking.class);
        Booking booking = query.setParameter("bookerId", userDtoDb.getId()).getSingleResult();

        MatcherAssert.assertThat(booking.getId(), notNullValue());
        MatcherAssert.assertThat(booking.getStart(), equalTo(bookingDto.getStart()));
        MatcherAssert.assertThat(booking.getEnd(), equalTo(bookingDto.getEnd()));
        MatcherAssert.assertThat(booking.getBookingStatus(), equalTo(BookingStatus.APPROVED));
    }
}
