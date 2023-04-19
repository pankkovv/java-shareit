package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.messages.LogMessages;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMap;
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
    private final UserMap userMap;

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.debug(LogMessages.TRY_GET.toString(), userService.getAllUsers());
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        log.debug(LogMessages.TRY_GET_ID.toString(), userId);
        return userService.getUserById(userId);
    }

    @PostMapping
    public UserDto saveNewUser(@Valid @RequestBody UserDto userDto) {
        User user = userMap.transferFromObj(userDto);
        log.debug(LogMessages.TRY_ADD.toString(), user);
        return userService.saveUser(user);
    }

    @PatchMapping("/{userId}")
    public UserDto updateLastUser(@PathVariable Long userId, @RequestBody UserDto userDto) {
        User user = userMap.transferFromObj(userDto);
        log.debug(LogMessages.TRY_UPDATE.toString(), userId);
        return userService.updateUser(userId, user);
    }

    @DeleteMapping("/{userId}")
    public void deleteLastUser(@PathVariable Long userId) {
        log.debug(LogMessages.TRY_DELETE.toString(), userId);
        userService.deleteUser(userId);
    }
}