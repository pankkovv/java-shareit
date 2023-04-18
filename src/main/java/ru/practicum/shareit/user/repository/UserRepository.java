package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    List<UserDto> findAll();

    UserDto getById(long userId);

    UserDto save(User user);

    UserDto update(long userId, User user);

    void delete(long userId);
}
