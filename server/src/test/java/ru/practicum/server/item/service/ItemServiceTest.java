package ru.practicum.server.item.service;

import lombok.RequiredArgsConstructor;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.server.booking.dto.BookingShort;
import ru.practicum.server.booking.service.BookingService;
import ru.practicum.server.comment.dto.CommentDto;
import ru.practicum.server.comment.dto.CommentShort;
import ru.practicum.server.comment.service.CommentService;
import ru.practicum.server.item.dto.ItemDto;
import ru.practicum.server.item.dto.ItemDtoWithBookingAndComments;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.user.dto.UserDto;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(
        properties = "spring.datasource.username=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTest {

    private final EntityManager em;
    private final ItemService itemService;
    private final BookingService bookingService;
    private final UserService userService;
    private final CommentService commentService;

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
        CommentShort commentShort = CommentShort.builder().text("Text test").created(end).build();

        UserDto userDtoDb = userService.saveUser(userDto);
        UserDto ownerDtoDb = userService.saveUser(userOwnerDto);
        ItemDto itemDto = ItemDto.builder().name("Item").description("Item items").available(true).owner(ownerDtoDb.getId()).requestId(null).build();
        List<ItemDtoWithBookingAndComments> itemDtoWithBookingAndCommentsList = List.of(ItemDtoWithBookingAndComments.builder().id(1L).owner(ownerDtoDb.getId()).name(item.getName()).description("Item items").available(true).comments(listCommentDto).build());
        ItemDto itemDtoDb = itemService.saveItem(ownerDtoDb.getId(), itemDto);
        BookingShort bookingShort = BookingShort.builder().start(start).end(end).itemId(itemDtoDb.getId()).build();
        bookingService.bookingAdd(userDtoDb.getId(), bookingShort);
        commentService.addComment(userDtoDb.getId(), itemDtoDb.getId(), commentShort);

        TypedQuery<Item> query = em.createQuery("Select u from Item u where u.owner.id = :ownerId", Item.class);
        List<Item> listItemDto = query.setParameter("ownerId", ownerDtoDb.getId()).getResultList();

        MatcherAssert.assertThat(listItemDto.get(0).getId(), notNullValue());
        MatcherAssert.assertThat(listItemDto.get(0).getDescription(), equalTo(itemDtoWithBookingAndCommentsList.get(0).getDescription()));
        MatcherAssert.assertThat(listItemDto.get(0).getName(), equalTo(itemDtoWithBookingAndCommentsList.get(0).getName()));
        MatcherAssert.assertThat(listItemDto.get(0).getRequest(), equalTo(itemDtoWithBookingAndCommentsList.get(0).getRequest()));
        MatcherAssert.assertThat(listItemDto.get(0).getOwner().getId(), equalTo(itemDtoWithBookingAndCommentsList.get(0).getOwner()));
    }
}
