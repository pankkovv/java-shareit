package ru.practicum.shareit.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.comment.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "select c.* from comments as c " +
            "where c.item_id = ?1 ", nativeQuery = true)
    List<Comment> findCommentById(Long itemId);
}
