package ru.practicum.server.comment.service;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.messages.ExceptionMessages;
import ru.practicum.server.messages.LogMessages;
import ru.practicum.server.booking.model.Booking;
import ru.practicum.server.booking.repository.BookingRepository;
import ru.practicum.server.booking.status.BookingStatus;
import ru.practicum.server.comment.dto.CommentDto;
import ru.practicum.server.comment.dto.CommentShort;
import ru.practicum.server.comment.mapper.CommentMap;
import ru.practicum.server.comment.repository.CommentRepository;
import ru.practicum.server.exception.ValidException;

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
        if (commentShort.getCreated() == null) {
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

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> getCommentsByItemId(Long itemId) {
        log.debug(LogMessages.COMMENT_ID.label, itemId);
        return CommentMap.mapToCommentDto(commentRepository.findCommentById(itemId));
    }
}
