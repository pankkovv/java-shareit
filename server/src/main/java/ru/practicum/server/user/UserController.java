package ru.practicum.server.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.messages.LogMessages;
import ru.practicum.server.user.dto.UserDto;
import ru.practicum.server.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.debug(LogMessages.TRY_GET.label, userService.getAllUsers());
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        log.debug(LogMessages.TRY_GET_ID.label, userId);
        return userService.getByUserId(userId);
    }

    @PostMapping
    public UserDto saveNewUser(@RequestBody UserDto userDto) {
        log.debug(LogMessages.TRY_ADD.label, userDto);
        return userService.saveUser(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto updateLastUser(@PathVariable Long userId,
                                  @RequestBody UserDto userDto) {
        log.debug(LogMessages.TRY_UPDATE.label, userId);
        return userService.updateUser(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteLastUser(@PathVariable Long userId) {
        log.debug(LogMessages.TRY_DELETE.label, userId);
        userService.deleteUser(userId);
    }
}