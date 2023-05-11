package ru.practicum.shareit.item.mapper;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingWithDate;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingAndComments;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
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

    public static ItemDto mapToItemWithBookingAndComments(ItemDtoWithBookingAndComments itemDtoWithBooking) {
        return ItemDto.builder()
                .id(itemDtoWithBooking.getId())
                .owner(itemDtoWithBooking.getOwner())
                .name(itemDtoWithBooking.getName())
                .description(itemDtoWithBooking.getDescription())
                .available(itemDtoWithBooking.getAvailable())
                .build();
    }

    public static ItemDtoWithBookingAndComments mapToItemDtoWithBookingAndComments(Item item, BookingWithDate bookingLast, BookingWithDate bookingNext, List<CommentDto> comments) {
        return ItemDtoWithBookingAndComments.builder()
                .id(item.getId())
                .owner(item.getOwner().getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .lastBooking(bookingLast)
                .nextBooking(bookingNext)
                .comments(comments)
                .build();
    }

    public static List<ItemDtoWithBookingAndComments> mapToItemDtoWithBookingAndComments(List<Item> listItem, HashMap<Long, BookingWithDate> bookingsLast, HashMap<Long, BookingWithDate> bookingsNext, HashMap<Long, List<CommentDto>> comments) {
        List<ItemDtoWithBookingAndComments> listItemDtoWithBooking = new ArrayList<>();
        for (Item item : listItem) {
            BookingWithDate bookingLast = bookingsLast.get(item.getId());
            BookingWithDate bookingNext = bookingsNext.get(item.getId());
            List<CommentDto> commentList = comments.get(item.getId());
            listItemDtoWithBooking.add(mapToItemDtoWithBookingAndComments(item, bookingLast, bookingNext, commentList));
        }
        return listItemDtoWithBooking.stream()
                .sorted(Comparator.comparing(ItemDtoWithBookingAndComments::getId))
                .collect(Collectors.toList());
    }
}
