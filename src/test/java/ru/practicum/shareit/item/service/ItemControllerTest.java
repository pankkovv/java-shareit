package ru.practicum.shareit.item.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentShort;
import ru.practicum.shareit.comment.service.CommentService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingAndComments;
import ru.practicum.shareit.messages.LogMessages;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@WebMvcTest(controllers = UserController.class)

public class ItemControllerTest  {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemService itemService;
    @MockBean
    CommentService commentService;

    @Autowired
    private MockMvc mvc;


    @GetMapping
    public List<ItemDtoWithBookingAndComments> getByUserId(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam(required = false) Integer from, @RequestParam (required = false) Integer size) {
        return itemService.getByUserId(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ItemDtoWithBookingAndComments getByItemId(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        return itemService.getByItemId(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@NotBlank @RequestParam String text, @RequestParam (required = false) Integer from, @RequestParam (required = false) Integer size) {
        return itemService.search(text, from, size);
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemDto itemDto) {
        return itemService.saveItem(userId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId, @Valid @RequestBody CommentShort commentShort) {
        return commentService.addComment(userId, itemId, commentShort);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId, @RequestBody ItemDto itemDto) {
        return itemService.updateItem(userId, itemId, itemDto);
    }
}
