package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface UserService {
    List<UserDto> getAllUsers();

    UserDto getByUserId(Long userId);

    UserDto saveUser(UserDto userDto);

    UserDto updateUser(Long userId, UserDto userDto);

    void deleteUser(Long userId);
}
