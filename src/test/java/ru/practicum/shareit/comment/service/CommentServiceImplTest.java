package ru.practicum.shareit.comment.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentShort;
import ru.practicum.shareit.comment.mapper.CommentMap;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.messages.LogMessages;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommentServiceImplTest {
    static CommentServiceImpl commentService = new CommentServiceImpl();
    static CommentRepository commentRepository = Mockito.mock(CommentRepository.class);
    static BookingRepository bookingRepository = Mockito.mock(BookingRepository.class);

    static User user;
    static User owner;
    static Item item;
    static LocalDateTime start;
    static LocalDateTime end;
    static Booking booking;
    static CommentShort commentShort;
    static CommentDto commentDto;
    static Comment comment;

    @BeforeAll
    static void assistant() {
        ReflectionTestUtils.setField(commentService, "bookingRepository", bookingRepository);
        ReflectionTestUtils.setField(commentService, "commentRepository", commentRepository);

        user = User.builder().id(1L).name("User").email("user@user.ru").build();
        owner = User.builder().id(2L).name("Owner").email("owner@user.ru").build();
        item = Item.builder().id(1L).owner(owner).name("Item").description("Item items").available(true).request(null).build();

        start = LocalDateTime.now();
        end = LocalDateTime.now().plusMinutes(1);

        booking = Booking.builder().start(start).end(end).item(item).booker(user).bookingStatus(BookingStatus.WAITING).build();
        commentShort = CommentShort.builder().text("Text test").created(start).build();
        commentDto = CommentDto.builder().text("Text test").authorName(user.getName()).created(start).build();
        comment = Comment.builder().text("Text test").item(item).user(user).created(start).build();
    }

    //normal behavior
    @Test
     void addCommentTest() {
        Mockito.when(bookingRepository.getBookingByBookerIdAndItemId(1L, 1L, start))
                .thenReturn(booking);
        Mockito.when(commentRepository.save(comment))
                .thenReturn(comment);

        Assertions.assertEquals(commentDto, commentService.addComment(1L,1L, commentShort));
    }

    //Reaction to erroneous data
    @Test
    void addCommentErrTest() {
        Mockito.when(bookingRepository.getBookingByBookerIdAndItemId(1L, 1L, end))
                .thenReturn(booking);
        Mockito.when(commentRepository.save(comment))
                .thenReturn(comment);
        final ValidException exception = assertThrows(ValidException.class, () -> commentService.addComment(1L,1L, commentShort));

        Assertions.assertEquals(exception.getMessage(), ExceptionMessages.ITEM_NOT_BOOKING.label);
    }
}