package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    List<UserDto> findAll();

    UserDto getById(Long userId);

    UserDto save(User user);

    UserDto update(Long userId, User user);

    void delete(Long userId);
}
