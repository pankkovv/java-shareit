package ru.practicum.server.comment.mapper;

import ru.practicum.server.item.model.Item;
import ru.practicum.server.user.model.User;
import ru.practicum.server.comment.dto.CommentDto;
import ru.practicum.server.comment.model.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentMap {
    public static Comment mapToComment(CommentDto commentDto, User user, Item item) {
        return Comment.builder()
                .id(commentDto.getId())
                .text(commentDto.getText())
                .item(item)
                .user(user)
                .created(commentDto.getCreated())
                .build();
    }

    public static CommentDto mapToCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getUser().getName())
                .created(comment.getCreated())
                .build();
    }

    public static List<CommentDto> mapToCommentDto(List<Comment> listComment) {
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment : listComment) {
            commentDtoList.add(mapToCommentDto(comment));
        }
        return commentDtoList;
    }
}
