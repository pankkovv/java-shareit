package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.messages.LogMessages;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.debug(String.valueOf(LogMessages.TRY_GET), userService.getAllUsers());
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable long userId) {
        log.debug(String.valueOf(LogMessages.TRY_GET_ID), userId);
        return userService.getUserById(userId);
    }

    @PostMapping
    public UserDto saveNewUser(@Valid @RequestBody User user) {
        log.debug(String.valueOf(LogMessages.TRY_ADD), user);
        return userService.saveUser(user);
    }

    @PatchMapping("/{userId}")
    public UserDto updateLastUser(@PathVariable long userId, @RequestBody User user) {
        log.debug(String.valueOf(LogMessages.TRY_UPDATE), userId);
        return userService.updateUser(userId, user);
    }

    @DeleteMapping("/{userId}")
    public void deleteLastUser(@PathVariable long userId) {
        log.debug(String.valueOf(LogMessages.TRY_DELETE), userId);
        userService.deleteUser(userId);
    }
}