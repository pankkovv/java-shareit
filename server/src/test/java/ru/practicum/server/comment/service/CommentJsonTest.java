package ru.practicum.server.comment.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.server.comment.dto.CommentShort;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class CommentJsonTest {
    @Autowired
    private JacksonTester<CommentShort> jsonCommentShort;

    @Test
    void testCommentShort() throws IOException {
        LocalDateTime end = LocalDateTime.of(2025,5,25,15,15,13);
        CommentShort commentShort = CommentShort.builder().text("Text test").created(end).build();

        JsonContent<CommentShort> result = jsonCommentShort.write(commentShort);

        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("Text test");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(end.toString());
    }
}
