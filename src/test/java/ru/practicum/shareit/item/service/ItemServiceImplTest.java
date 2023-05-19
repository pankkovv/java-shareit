package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.service.CommentService;
import ru.practicum.shareit.exception.NotStateException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingAndComments;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;

class ItemServiceImplTest {
    static ItemServiceImpl itemService = new ItemServiceImpl();
    static BookingRepository bookingRepository = Mockito.mock(BookingRepository.class);
    static ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
    static UserService userService = Mockito.mock(UserService.class);
    static CommentService commentService = Mockito.mock(CommentService.class);
    static ItemRequestService itemRequestService = Mockito.mock(ItemRequestService.class);

    static User user;
    static User owner;
    static Item item;
    static Booking booking;
    static List<CommentDto> listCommentDto;
    static LocalDateTime start;
    static LocalDateTime end;


    @BeforeAll
    static void assistant() {
        ReflectionTestUtils.setField(itemService, "bookingRepository", bookingRepository);
        ReflectionTestUtils.setField(itemService, "itemRepository", itemRepository);
        ReflectionTestUtils.setField(itemService, "commentService", commentService);
        ReflectionTestUtils.setField(itemService, "userService", userService);
        ReflectionTestUtils.setField(itemService, "itemRequestService", itemRequestService);

        user = User.builder().id(1L).name("User").email("user@user.ru").build();
        owner = User.builder().id(2L).name("Owner").email("owner@user.ru").build();
        item = Item.builder().owner(owner).name("Item").description("Item items").available(true).request(null).build();

        start = LocalDateTime.now();
        end = LocalDateTime.now().plusMinutes(1);

        booking = Booking.builder().start(start).end(end).item(item).booker(user).bookingStatus(BookingStatus.WAITING).build();

        listCommentDto = List.of(CommentDto.builder().text("Text test").authorName(user.getName()).created(end).build());
    }

    //normal behavior
    @Test
    void getByUserIdTest() {
        List<ItemDtoWithBookingAndComments> itemDtoWithBookingAndCommentsList =  List.of(ItemDtoWithBookingAndComments.builder().id(1L).owner(owner.getId()).name(item.getName()).description("Item items").available(true).comments(listCommentDto).build());
        List<Item> listItem = List.of(Item.builder().id(1L).owner(owner).name("Item").description("Item items").available(true).request(null).build());


        Mockito.when(itemRepository.findByOwnerId(1L, Pageable.ofSize(4)))
                .thenReturn(listItem);
        Mockito.when(bookingRepository.findByItemIdLast(1L, 1L, start))
                .thenReturn(booking);
        Mockito.when(bookingRepository.findByItemIdNext(1L, 1L, start))
                .thenReturn(booking);
        Mockito.when(commentService.getCommentsByItemId(anyLong()))
                .thenReturn(listCommentDto);

        Assertions.assertEquals(itemDtoWithBookingAndCommentsList, itemService.getByUserId(1L, 0, 4));
    }

    @Test
    void saveItemTest() {
        ItemDto itemDto = ItemDto.builder().name("Item").description("Item items").available(true).owner(owner.getId()).requestId(null).build();

        Mockito.when(itemRepository.save(item))
                .thenReturn(item);
        Mockito.when(userService.findById(anyLong()))
                .thenReturn(owner);
        assertEquals(itemDto, itemService.saveItem(user.getId(), itemDto));
    }

    @Test
    void updateItemTest() {
       Optional<Item> itemOpt = Optional.of(Item.builder().owner(owner).name("Item").description("Item items").available(true).request(null).build());
       Item itemUpdate = Item.builder().owner(owner).name("Update").description("Update").available(false).request(null).build();
       ItemDto itemDtoUpdate = ItemDto.builder().owner(owner.getId()).name("Update").description("Update").available(false).requestId(null).build();


        Mockito.when(itemRepository.findById(1L))
                .thenReturn(itemOpt);
        Mockito.when(bookingRepository.findByItemIdLast(1L, 1L, start))
                .thenReturn(booking);
        Mockito.when(bookingRepository.findByItemIdNext(1L, 1L, start))
                .thenReturn(booking);
        Mockito.when(commentService.getCommentsByItemId(anyLong()))
                .thenReturn(listCommentDto);
        Mockito.when(userService.findById(anyLong()))
                        .thenReturn(owner);
        Mockito.when(itemRepository.save(itemUpdate))
                        .thenReturn(itemUpdate);

        assertEquals(itemDtoUpdate, itemService.updateItem(1L, 1L, itemDtoUpdate));
    }

    @Test
    void pagedTest() {
        Pageable page = PageRequest.of(0, 4);
        Assertions.assertEquals(page, itemService.paged(0, 4));
    }
    //Reaction to erroneous data
    @Test
    void searchErrTest() {
        Assertions.assertEquals(List.of(), itemService.search("test", 0, 4));
    }

    @Test
    void pagedErrTest() {
        final NotStateException exception = assertThrows(NotStateException.class, () -> itemService.paged(-1, 4));

        Assertions.assertEquals(exception.getMessage(), ExceptionMessages.FROM_NOT_POSITIVE.label);
    }
}