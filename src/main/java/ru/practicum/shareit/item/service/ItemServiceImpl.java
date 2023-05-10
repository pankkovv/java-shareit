package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingWithDate;
import ru.practicum.shareit.booking.mapper.BookingMap;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.mapper.CommentMap;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingAndComments;
import ru.practicum.shareit.item.mapper.ItemMap;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<ItemDtoWithBookingAndComments> getByUserId(Long userId) {
        ArrayList<BookingWithDate> bookingsLast = new ArrayList<>();
        ArrayList<BookingWithDate> bookingsNext = new ArrayList<>();

        HashMap<Long, List<CommentDto>> comments = new HashMap<>();
        List<Item> items = itemRepository.findByOwnerId(userId);
        for(Item i : items){
            bookingsLast.add(BookingMap.mapToBookingWithoutDate(bookingRepository.findByItemIdLast(userId, i.getId())));
            bookingsNext.add(BookingMap.mapToBookingWithoutDate(bookingRepository.findByItemIdNext(userId, i.getId(), LocalDateTime.now())));
            comments.put(i.getId(), CommentMap.mapToCommentDto(commentRepository.findCommentById(i.getId())));
        }
        return ItemMap.mapToItemDtoWithBookingAndComments(items, bookingsLast, bookingsNext, comments);
    }

    @Override
    public ItemDtoWithBookingAndComments getByItemId(Long userId, Long itemId) {
        BookingWithDate bookingLast = BookingMap.mapToBookingWithoutDate(bookingRepository.findByItemIdLast(userId, itemId));
        BookingWithDate bookingNext = BookingMap.mapToBookingWithoutDate(bookingRepository.findByItemIdNext(userId, itemId, LocalDateTime.now()));
        List<CommentDto> comments = CommentMap.mapToCommentDto(commentRepository.findCommentById(itemId));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Item not found."));
        return ItemMap.mapToItemDtoWithBookingAndComments(item, bookingLast, bookingNext, comments);
    }

    @Override
    public List<ItemDto> search(String text) {
        if(!text.isEmpty()){
            return ItemMap.mapToItemDto(itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable(text, text, true));
        } else {
            return List.of();
        }
    }

    @Override
    public ItemDto saveItem(Long userId, ItemDto itemDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotOwnerException("User not found."));
        return ItemMap.mapToItemDto(itemRepository.save(ItemMap.mapToItem(itemDto, user)));
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotOwnerException("User not found."));
        ItemDto itemDtoOld = ItemMap.mapToItemWithBookingAndComments(getByItemId(userId, itemId));
        if(itemDto.getName() != null){
            itemDtoOld.setName(itemDto.getName());
        }
        if(itemDto.getDescription() != null){
            itemDtoOld.setDescription(itemDto.getDescription());
        }
        if(itemDto.getAvailable() != null){
            itemDtoOld.setAvailable(itemDto.getAvailable());
        }
        if(itemDto.getRequest() != null){
            itemDtoOld.setRequest(itemDto.getRequest());
        }
        return saveItem(userId, itemDtoOld);
    }
}
