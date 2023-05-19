package ru.practicum.shareit.comment.service;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentShort;
import ru.practicum.shareit.comment.mapper.CommentMap;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.exception.ValidException;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.messages.LogMessages;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@NoArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public CommentDto addComment(Long userId, Long itemId, CommentShort commentShort) {
        if(commentShort.getCreated() == null){
            commentShort.setCreated(LocalDateTime.now());
        }
        Booking booking = bookingRepository.getBookingByBookerIdAndItemId(userId, itemId, commentShort.getCreated());
        if ((booking != null) && !booking.getBookingStatus().equals(BookingStatus.REJECTED)) {
            CommentDto commentDto = CommentDto.builder().text(commentShort.getText()).authorName(booking.getBooker().getName()).created(commentShort.getCreated()).build();
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
