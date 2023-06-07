package ru.practicum.server.comment.service;

import ru.practicum.server.comment.dto.CommentDto;
import ru.practicum.server.comment.dto.CommentShort;

import java.util.List;

public interface CommentService {
    public CommentDto addComment(Long userId, Long itemId, CommentShort commentShort);

    public List<CommentDto> getCommentsByItemId(Long itemId);
}
