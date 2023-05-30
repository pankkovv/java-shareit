package ru.practicum.shareit.item.service;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.mapper.BookingMap;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.service.CommentService;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotStateException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingAndComments;
import ru.practicum.shareit.item.mapper.ItemMap;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.request.mapper.ItemRequestMap;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
@NoArgsConstructor
@Slf4j
class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ItemRequestService itemRequestService;

    @Transactional(readOnly = true)
    @Override
    public List<ItemDtoWithBookingAndComments> getByUserId(Long userId, Integer from, Integer size) {
        Pageable page = paged(from, size);
        HashMap<Long, BookingWithDate> bookingsLast = new HashMap<>();
        HashMap<Long, BookingWithDate> bookingsNext = new HashMap<>();
        HashMap<Long, List<CommentDto>> comments = new HashMap<>();
        List<Item> items = itemRepository.findByOwnerId(userId, page);
        for (Item i : items) {
            bookingsLast.put(i.getId(), BookingMap.mapToBookingWithoutDate(bookingRepository.findByItemIdLast(userId, i.getId(), LocalDateTime.now())));
            bookingsNext.put(i.getId(), BookingMap.mapToBookingWithoutDate(bookingRepository.findByItemIdNext(userId, i.getId(), LocalDateTime.now())));
            comments.put(i.getId(), commentService.getCommentsByItemId(i.getId()));
        }
        return ItemMap.mapToItemDtoWithBookingAndComments(items, bookingsLast, bookingsNext, comments);
    }

    @Transactional(readOnly = true)
    @Override
    public ItemDtoWithBookingAndComments getByItemId(Long userId, Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.NOT_FOUND_ITEM.label));
        BookingWithDate bookingLast = BookingMap.mapToBookingWithoutDate(bookingRepository.findByItemIdLast(userId, itemId, LocalDateTime.now()));
        BookingWithDate bookingNext = BookingMap.mapToBookingWithoutDate(bookingRepository.findByItemIdNext(userId, itemId, LocalDateTime.now()));
        List<CommentDto> comments = commentService.getCommentsByItemId(itemId);
        return ItemMap.mapToItemDtoWithBookingAndComments(item, bookingLast, bookingNext, comments);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemDto> search(String text, Integer from, Integer size) {
        if (!text.isEmpty()) {
            Pageable page = paged(from, size);
            return ItemMap.mapToItemDto(itemRepository.search(text, page));
        } else {
            return List.of();
        }
    }

    @Override
    public ItemDto saveItem(Long userId, ItemDto itemDto) {
        User user = userService.findById(userId);
        ItemRequest itemRequest = null;
        if (itemDto.getRequestId() != null) {
            itemRequest = ItemRequestMap.mapToItemRequest(itemRequestService.getRequestId(userId, itemDto.getRequestId()), user);
        }
        return ItemMap.mapToItemDto(itemRepository.save(ItemMap.mapToItem(itemDto, user, itemRequest)));
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        validateExistUser(userId);
        ItemDto itemDtoOld = ItemMap.mapToItemWithBookingAndComments(getByItemId(userId, itemId));
        if (itemDto.getName() != null) {
            itemDtoOld.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            itemDtoOld.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            itemDtoOld.setAvailable(itemDto.getAvailable());
        }
        if (itemDto.getRequestId() != null) {
            itemDtoOld.setRequestId(itemDto.getRequestId());
        }
        return saveItem(userId, itemDtoOld);
    }

    @Transactional(readOnly = true)
    @Override
    public Item getById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.NOT_FOUND_ITEM.label));
    }

    void validateExistUser(Long userId) {
        userService.findById(userId);
    }

    public Pageable paged(Integer from, Integer size) {
        if (from != null) {
            if (from < 0) {
                throw new NotStateException(ExceptionMessages.FROM_NOT_POSITIVE.label);
            } else if (size > 0) {
                return PageRequest.of(from > 0 ? from / size : 0, size);
            } else {
                return PageRequest.of(0, 4);
            }
        } else {
            return PageRequest.of(0, 4);
        }
    }
}
