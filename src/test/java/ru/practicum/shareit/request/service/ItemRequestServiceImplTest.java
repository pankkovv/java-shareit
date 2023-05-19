package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.exception.NotStateException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;

class ItemRequestServiceImplTest {
    static ItemRequestServiceImpl itemRequestService = new ItemRequestServiceImpl();

    static ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
    static ItemRequestRepository itemRequestRepository = Mockito.mock(ItemRequestRepository.class);
    static UserService userService = Mockito.mock(UserService.class);

    @BeforeAll
    static void assistant() {
        ReflectionTestUtils.setField(itemRequestService, "itemRepository", itemRepository);
        ReflectionTestUtils.setField(itemRequestService, "itemRequestRepository", itemRequestRepository);
        ReflectionTestUtils.setField(itemRequestService, "userService", userService);
    }

    @Test
    void addRequestTest() {
        LocalDateTime start = LocalDateTime.now();
        List<ItemDto> listItems = List.of(ItemDto.builder().name("Item").description("Item items").available(true).owner(1L).requestId(null).build());
        User user = User.builder().id(1L).name("User").email("user@user.ru").build();
        ItemRequest itemRequest = ItemRequest.builder().id(1L).description("Test").created(start).requestor(user).build();
        ItemRequestDto itemRequestDto = ItemRequestDto.builder().id(1L).description("Test").created(start).requestor(user.getId()).items(null).build();

        Mockito.when(userService.findById(anyLong()))
                .thenReturn(user);
        Mockito.when(itemRequestRepository.save(itemRequest))
                        .thenReturn(itemRequest);

        assertEquals(itemRequestDto, itemRequestService.addRequest(1L, itemRequestDto));
    }

    @Test
    void getRequestAllTest() {
        LocalDateTime start = LocalDateTime.now();
        User user = User.builder().id(1L).name("User").email("user@user.ru").build();
        List<Item> listItem = List.of(Item.builder().id(1L).owner(user).name("Item").description("Item items").available(true).request(null).build());
        List<ItemDto> listItemDto = List.of(ItemDto.builder().id(1L).name("Item").description("Item items").available(true).owner(user.getId()).requestId(null).build());
        List<ItemRequest> listItemRequest = List.of(ItemRequest.builder().id(1L).description("Test").created(start).requestor(user).build());
        List<ItemRequestDto> listItemRequestDto = List.of(ItemRequestDto.builder().id(1L).description("Test").created(start).requestor(user.getId()).items(listItemDto).build());

        Mockito.when(userService.findById(anyLong()))
                .thenReturn(user);
        Mockito.when(itemRequestRepository.findItemRequestByIdNotOrderByCreatedDesc(1L, Pageable.ofSize(4)))
                        .thenReturn(listItemRequest);
        Mockito.when(itemRepository.findItemByRequest_Id(anyLong()))
                .thenReturn(listItem);

        assertEquals(listItemRequestDto, itemRequestService.getRequestAll(1L, 0, 4));
    }

    //Reaction to empty data
    @Test
    void getRequestAllEmptyTest() {
        LocalDateTime start = LocalDateTime.now();
        User user = User.builder().id(1L).name("User").email("user@user.ru").build();
        List<Item> listItem = List.of(Item.builder().id(1L).owner(user).name("Item").description("Item items").available(true).request(null).build());
        List<ItemDto> listItemDto = List.of(ItemDto.builder().id(1L).name("Item").description("Item items").available(true).owner(user.getId()).requestId(null).build());
        List<ItemRequest> listItemRequest = List.of(ItemRequest.builder().id(1L).description("Test").created(start).requestor(user).build());

        Mockito.when(userService.findById(anyLong()))
                .thenReturn(user);
        Mockito.when(itemRequestRepository.findItemRequestByIdNotOrderByCreatedDesc(1L, Pageable.ofSize(4)))
                .thenReturn(listItemRequest);
        Mockito.when(itemRepository.findItemByRequest_Id(anyLong()))
                .thenReturn(listItem);

        assertEquals(List.of(), itemRequestService.getRequestAll(1L, null, 4));
    }

    //Reaction to erroneous data
    @Test
    void getRequestAllErrTest() {
        LocalDateTime start = LocalDateTime.now();
        User user = User.builder().id(1L).name("User").email("user@user.ru").build();
        List<Item> listItem = List.of(Item.builder().id(1L).owner(user).name("Item").description("Item items").available(true).request(null).build());
        List<ItemRequest> listItemRequest = List.of(ItemRequest.builder().id(1L).description("Test").created(start).requestor(user).build());

        Mockito.when(userService.findById(anyLong()))
                .thenReturn(user);
        Mockito.when(itemRequestRepository.findItemRequestByIdNotOrderByCreatedDesc(1L, Pageable.ofSize(4)))
                .thenReturn(listItemRequest);
        Mockito.when(itemRepository.findItemByRequest_Id(anyLong()))
                .thenReturn(listItem);

        final NotStateException exception = assertThrows(NotStateException.class, () -> itemRequestService.getRequestAll(1L,-1, 4));

        assertEquals(exception.getMessage(), ExceptionMessages.FROM_NOT_POSITIVE.label);
    }
}