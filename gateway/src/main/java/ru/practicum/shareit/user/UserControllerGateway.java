package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.messages.LogMessages;
import ru.practicum.shareit.user.dto.UserDtoGateway;

import javax.validation.Valid;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserControllerGateway {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> addUser(@Valid @RequestBody UserDtoGateway userDtoGateway) {
        log.debug(LogMessages.TRY_ADD.label, userDtoGateway);
        return userClient.addUser(userDtoGateway);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable Long userId,
                                             @RequestBody UserDtoGateway userDtoGateway) {
        log.debug(LogMessages.TRY_UPDATE.label, userId);
        return userClient.upUser(userId, userDtoGateway);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long userId) {
        log.debug(LogMessages.TRY_DELETE.label, userId);
        return userClient.rmUser(userId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable Long userId) {
        log.debug(LogMessages.TRY_GET_ID.label, userId);
        return userClient.getUser(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        log.debug(LogMessages.TRY_GET.label);
        return userClient.getUsers();
    }
}