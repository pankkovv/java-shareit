package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingWithDate;
import ru.practicum.shareit.booking.mapper.BookingMap;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.service.CommentService;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingAndComments;
import ru.practicum.shareit.item.mapper.ItemMap;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final CommentService commentService;

    @Override
    public List<ItemDtoWithBookingAndComments> getByUserId(Long userId) {
        HashMap<Long, BookingWithDate> bookingsLast = new HashMap<>();
        HashMap<Long, BookingWithDate> bookingsNext = new HashMap<>();
        HashMap<Long, List<CommentDto>> comments = new HashMap<>();
        List<Item> items = itemRepository.findByOwnerId(userId);
        for (Item i : items) {
            bookingsLast.put(i.getId(), BookingMap.mapToBookingWithoutDate(bookingRepository.findByItemIdLast(userId, i.getId(), LocalDateTime.now())));
            bookingsNext.put(i.getId(), BookingMap.mapToBookingWithoutDate(bookingRepository.findByItemIdNext(userId, i.getId(), LocalDateTime.now())));
            comments.put(i.getId(), commentService.getCommentsByItemId(i.getId()));
        }
        return ItemMap.mapToItemDtoWithBookingAndComments(items, bookingsLast, bookingsNext, comments);
    }

    @Override
    public ItemDtoWithBookingAndComments getByItemId(Long userId, Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.NOT_FOUND_ITEM.label));
        BookingWithDate bookingLast = BookingMap.mapToBookingWithoutDate(bookingRepository.findByItemIdLast(userId, itemId, LocalDateTime.now()));
        BookingWithDate bookingNext = BookingMap.mapToBookingWithoutDate(bookingRepository.findByItemIdNext(userId, itemId, LocalDateTime.now()));
        List<CommentDto> comments = commentService.getCommentsByItemId(itemId);
        return ItemMap.mapToItemDtoWithBookingAndComments(item, bookingLast, bookingNext, comments);
    }

    @Override
    public List<ItemDto> search(String text) {
        if (!text.isEmpty()) {
            return ItemMap.mapToItemDto(itemRepository.search(text));
        } else {
            return List.of();
        }
    }

    @Override
    public ItemDto saveItem(Long userId, ItemDto itemDto) {
        User user = userService.findById(userId);
        return ItemMap.mapToItemDto(itemRepository.save(ItemMap.mapToItem(itemDto, user)));
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
        if (itemDto.getRequest() != null) {
            itemDtoOld.setRequest(itemDto.getRequest());
        }
        return saveItem(userId, itemDtoOld);
    }

    @Override
    public Item getById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.NOT_FOUND_ITEM.label));
    }

    void validateExistUser(Long userId) {
        userService.findById(userId);
    }
}
