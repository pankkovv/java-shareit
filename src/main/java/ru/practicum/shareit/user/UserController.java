package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
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
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable long userId){
        return userService.getUserById(userId);
    }

    @PostMapping
    public User saveNewUser(@Valid @RequestBody User user) {
        return userService.saveUser(user);
    }

    @PatchMapping("/{userId}")
    public User updateLastUser(@PathVariable long userId, @RequestBody User user){
       return userService.updateUser(userId, user);
    }

    @DeleteMapping("/{userId}")
    public void deleteLastUser(@PathVariable long userId){
        userService.deleteUser(userId);
    }
}