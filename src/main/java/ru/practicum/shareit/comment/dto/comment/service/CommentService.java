package ru.practicum.shareit.comment.dto.comment.service;

import ru.practicum.shareit.comment.dto.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.comment.dto.CommentShort;

import java.util.List;

public interface CommentService {
    public CommentDto addComment(Long userId, Long itemId, CommentShort commentShort);

    public List<CommentDto> getCommentsByItemId(Long itemId);
}
