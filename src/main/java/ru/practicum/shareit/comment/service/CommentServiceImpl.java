package ru.practicum.shareit.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentShort;
import ru.practicum.shareit.comment.mapper.CommentMap;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.exception.ValidException;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.messages.LogMessages;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Override
    public CommentDto addComment(Long userId, Long itemId, CommentShort commentShort) {
        Booking booking = bookingRepository.getBookingByBookerIdAndItemId(userId, itemId, LocalDateTime.now());
        if ((booking != null) && !booking.getBookingStatus().equals(BookingStatus.REJECTED)) {
            CommentDto commentDto = CommentDto.builder().text(commentShort.getText()).authorName(booking.getBooker().getName()).created(commentShort.getCreated()).build();
            if (commentDto.getCreated() == null) {
                commentDto.setCreated(LocalDateTime.now());
            }
            log.debug(LogMessages.COMMENT_ADD.label, booking.getItem().getId());
            return CommentMap.mapToCommentDto(commentRepository.save(CommentMap.mapToComment(commentDto, booking.getBooker(), booking.getItem())));
        } else {
            throw new ValidException(ExceptionMessages.ITEM_NOT_BOOKING.label);
        }
    }

    @Override
    public List<CommentDto> getCommentsByItemId(Long itemId) {
        log.debug(LogMessages.COMMENT_ID.label, itemId);
        return CommentMap.mapToCommentDto(commentRepository.findCommentById(itemId));
    }
}
