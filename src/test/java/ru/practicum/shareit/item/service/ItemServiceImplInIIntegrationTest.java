package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentShort;
import ru.practicum.shareit.comment.service.CommentService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingAndComments;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(
        properties = "spring.datasource.username=postgres",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImplInIIntegrationTest {

    private final EntityManager em;
    private final ItemService itemService;
    private final BookingService bookingService;
    private final UserService userService;
    private final CommentService commentService;
    private final ItemRequestService itemRequestService;

    @Test
    void getByUserIdTest() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusMinutes(1);

        User user = User.builder().id(1L).name("User").email("user@user.ru").build();
        User owner = User.builder().id(2L).name("Owner").email("owner@user.ru").build();

        UserDto userDto = UserDto.builder().id(1L).name("User").email("user@user.ru").build();
        UserDto userOwnerDto = UserDto.builder().id(2L).name("Owner").email("owner@user.ru").build();

        List<CommentDto> listCommentDto = List.of(CommentDto.builder().text("Text test").authorName(user.getName()).created(end).build());

        Item item = Item.builder().id(1L).owner(owner).name("Item").description("Item items").available(true).request(null).build();
        ItemDto itemDto = ItemDto.builder().name("Item").description("Item items").available(true).owner(owner.getId()).requestId(null).build();
        List<ItemDtoWithBookingAndComments> itemDtoWithBookingAndCommentsList = List.of(ItemDtoWithBookingAndComments.builder().id(1L).owner(owner.getId()).name(item.getName()).description("Item items").available(true).comments(listCommentDto).build());

        CommentShort commentShort = CommentShort.builder().text("Text test").created(end).build();

        BookingShort bookingShort = BookingShort.builder().start(start).end(end).itemId(item.getId()).build();

        userService.saveUser(userDto);
        userService.saveUser(userOwnerDto);
        itemService.saveItem(owner.getId(), itemDto);
        bookingService.bookingAdd(userDto.getId(), bookingShort);
        commentService.addComment(user.getId(), item.getId(), commentShort);

        TypedQuery<Item> query = em.createQuery("Select u from Item u where u.owner.id = :ownerId", Item.class);
        List<Item> listItemDto = query.setParameter("ownerId", owner.getId()).getResultList();

        MatcherAssert.assertThat(listItemDto.get(0).getId(), notNullValue());
        MatcherAssert.assertThat(listItemDto.get(0).getDescription(), equalTo(itemDtoWithBookingAndCommentsList.get(0).getDescription()));
        MatcherAssert.assertThat(listItemDto.get(0).getName(), equalTo(itemDtoWithBookingAndCommentsList.get(0).getName()));
        MatcherAssert.assertThat(listItemDto.get(0).getRequest(), equalTo(itemDtoWithBookingAndCommentsList.get(0).getRequest()));
        MatcherAssert.assertThat(listItemDto.get(0).getOwner().getId(), equalTo(itemDtoWithBookingAndCommentsList.get(0).getOwner()));
    }
}
