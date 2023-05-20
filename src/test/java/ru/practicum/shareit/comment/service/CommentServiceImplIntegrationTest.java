package ru.practicum.shareit.comment.service;

import lombok.RequiredArgsConstructor;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentShort;
import ru.practicum.shareit.comment.mapper.CommentMap;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.exception.ValidException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.messages.LogMessages;
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
        properties = "spring.datasource.username=postgres",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentServiceImplIntegrationTest {
    private final EntityManager em;
    private final CommentService commentService;
    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;

    @Test
    void addCommentTest() {
        User user = User.builder().id(1L).name("User").email("user@user.ru").build();
        User owner = User.builder().id(2L).name("Owner").email("owner@user.ru").build();

        UserDto userDto = UserDto.builder().id(1L).name("User").email("user@user.ru").build();
        UserDto userOwnerDto = UserDto.builder().id(2L).name("Owner").email("owner@user.ru").build();

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusMinutes(1);

        Item item = Item.builder().id(1L).owner(owner).name("Item").description("Item items").available(true).request(null).build();
        ItemDto itemDto = ItemDto.builder().name("Item").description("Item items").available(true).owner(owner.getId()).requestId(null).build();

        CommentShort commentShort = CommentShort.builder().text("Text test").created(end).build();
        CommentDto commentDto = CommentDto.builder().text("Text test").authorName(user.getName()).created(end).build();

        BookingShort bookingShort = BookingShort.builder().start(start).end(end).itemId(item.getId()).build();

        userService.saveUser(userDto);
        userService.saveUser(userOwnerDto);
        itemService.saveItem(owner.getId(), itemDto);
        bookingService.bookingAdd(userDto.getId(), bookingShort);
        commentService.addComment(user.getId(), item.getId(), commentShort);

        TypedQuery<Comment> query = em.createQuery("Select u from Comment u where u.user.id = :userId", Comment.class);
        Comment comment = query.setParameter("userId", user.getId()).getSingleResult();

        MatcherAssert.assertThat(comment.getId(), notNullValue());
        MatcherAssert.assertThat(comment.getText(), equalTo(commentDto.getText()));
        MatcherAssert.assertThat(comment.getUser().getName(), equalTo(commentDto.getAuthorName()));
        MatcherAssert.assertThat(comment.getItem().getId(), equalTo(item.getId()));
        MatcherAssert.assertThat(comment.getCreated(), equalTo(commentDto.getCreated()));
    }
}
