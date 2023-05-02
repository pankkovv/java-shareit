package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface UserService {
    @Transactional
    List<UserDto> getAllUsers();

    @Transactional
    UserDto getByUserId(Long userId);

    @Transactional
    UserDto saveUser(User user);

    @Transactional
    UserDto updateUser(Long userId, User user);

    @Transactional
    void deleteUser(Long userId);
}
