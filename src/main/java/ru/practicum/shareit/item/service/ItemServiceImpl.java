package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingWithoutDate;
import ru.practicum.shareit.booking.mapper.BookingMap;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.mapper.ItemMap;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public List<ItemDtoWithBooking> getByUserId(Long userId) {
        HashMap<Long ,List<BookingWithoutDate>> bookings = new HashMap<>();
        List<Item> items = itemRepository.findByOwnerId(userId);
        for(Item i : items){
            bookings.put(i.getId(), BookingMap.mapToBookingWithoutDate(bookingRepository.findByItem_Id(userId, i.getId())));
        }
        return ItemMap.mapToItemDtoWithBooking(items, bookings);
    }

    @Override
    public ItemDtoWithBooking getByItemId(Long userId, Long itemId) {
        List<BookingWithoutDate> bookings = BookingMap.mapToBookingWithoutDate(bookingRepository.findByItem_Id(userId, itemId));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Item not found."));
        return ItemMap.mapToItemDtoWithBooking(item, bookings);
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
        ItemDto itemDtoOld = ItemMap.mapToItemWithoutBooking(getByItemId(userId, itemId));
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
