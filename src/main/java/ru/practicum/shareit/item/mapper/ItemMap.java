package ru.practicum.shareit.item.mapper;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingWithoutDate;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
public class ItemMap {

    public static Item mapToItem(ItemDto itemDto, User user) {
        return Item.builder()
                .id(itemDto.getId())
                .owner(user)
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }

    public static ItemDto mapToItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .owner(item.getOwner().getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static List<ItemDto> mapToItemDto(List<Item> listItem) {
        List<ItemDto> listItemDto = new ArrayList<>();
        for (Item item : listItem) {
            listItemDto.add(mapToItemDto(item));
        }
        return listItemDto;
    }

    public static ItemDto mapToItemWithoutBooking(ItemDtoWithBooking itemDtoWithBooking) {
        return ItemDto.builder()
                .id(itemDtoWithBooking.getId())
                .owner(itemDtoWithBooking.getOwner())
                .name(itemDtoWithBooking.getName())
                .description(itemDtoWithBooking.getDescription())
                .available(itemDtoWithBooking.getAvailable())
                .build();
    }

    public static ItemDtoWithBooking mapToItemDtoWithBooking(Item item, List<BookingWithoutDate> bookings) {
        if (bookings.isEmpty()) {
            return ItemDtoWithBooking.builder()
                    .id(item.getId())
                    .owner(item.getOwner().getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.getAvailable())
                    .lastBooking(null)
                    .nextBooking(null)
                    .build();
        } else if (bookings.size() == 1) {
            return ItemDtoWithBooking.builder()
                    .id(item.getId())
                    .owner(item.getOwner().getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.getAvailable())
                    .lastBooking(bookings.get(0))
                    .nextBooking(null)
                    .build();
        } else {
            return ItemDtoWithBooking.builder()
                    .id(item.getId())
                    .owner(item.getOwner().getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.getAvailable())
                    .lastBooking(bookings.get(0))
                    .nextBooking(bookings.get(bookings.size() - 1))
                    .build();
        }

    }

    public static List<ItemDtoWithBooking> mapToItemDtoWithBooking(List<Item> listItem, HashMap<Long, List<BookingWithoutDate>> bookings) {
        List<ItemDtoWithBooking> listItemDtoWithBooking = new ArrayList<>();
        for (Item item : listItem) {
            List<BookingWithoutDate> bookingList = bookings.get(item.getId());
            listItemDtoWithBooking.add(mapToItemDtoWithBooking(item, bookingList));
        }
        return listItemDtoWithBooking.stream()
                .sorted(Comparator.comparing(ItemDtoWithBooking::getId))
                .collect(Collectors.toList());
    }

}
