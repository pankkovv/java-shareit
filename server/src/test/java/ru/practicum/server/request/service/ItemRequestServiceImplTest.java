package ru.practicum.server.request.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.server.exception.NotRequestException;
import ru.practicum.server.exception.NotStateException;
import ru.practicum.server.item.dto.ItemDto;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.item.repository.ItemRepository;
import ru.practicum.server.messages.ExceptionMessages;
import ru.practicum.server.request.dto.ItemRequestDto;
import ru.practicum.server.request.model.ItemRequest;
import ru.practicum.server.request.repository.ItemRequestRepository;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    void getRequestTest() {
        LocalDateTime start = LocalDateTime.now();
        User user = User.builder().id(1L).name("User").email("user@user.ru").build();
        List<Item> listItem = List.of(Item.builder().id(1L).owner(user).name("Item").description("Item items").available(true).request(null).build());
        List<ItemDto> listItemDto = List.of(ItemDto.builder().id(1L).name("Item").description("Item items").available(true).owner(user.getId()).requestId(null).build());
        List<ItemRequest> listItemRequest = List.of(ItemRequest.builder().id(1L).description("Test").created(start).requestor(user).build());
        List<ItemRequestDto> listItemRequestDto = List.of(ItemRequestDto.builder().id(1L).description("Test").created(start).requestor(user.getId()).items(listItemDto).build());

        Mockito.when(userService.findById(anyLong()))
                .thenReturn(user);
        Mockito.when(itemRequestRepository.findItemRequestByRequestor_Id(1L))
                .thenReturn(listItemRequest);
        Mockito.when(itemRepository.findItemByRequest_Id(anyLong()))
                .thenReturn(listItem);

        assertEquals(listItemRequestDto, itemRequestService.getRequest(1L));
    }

    //Reaction to empty data
    @Test
    void getRequestAllEmptyTest() {
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

        final NotStateException exception = assertThrows(NotStateException.class, () -> itemRequestService.getRequestAll(1L, -1, 4));

        assertEquals(exception.getMessage(), ExceptionMessages.FROM_NOT_POSITIVE.label);
    }

    @Test
    void addRequestErrTest() {
        LocalDateTime start = LocalDateTime.now();
        User user = User.builder().id(1L).name("User").email("user@user.ru").build();
        ItemRequest itemRequest = ItemRequest.builder().id(1L).description("Test").created(start).requestor(user).build();

        Mockito.when(itemRequestRepository.save(itemRequest))
                .thenReturn(itemRequest);

        final NotRequestException exception = assertThrows(NotRequestException.class, () -> itemRequestService.getRequestId(10L, 1L));

        assertEquals(exception.getMessage(), ExceptionMessages.NOT_FOUND_REQUEST.label);
    }
}