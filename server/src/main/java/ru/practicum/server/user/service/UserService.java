package ru.practicum.server.user.service;

import ru.practicum.server.user.dto.UserDto;
import ru.practicum.server.user.model.User;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface UserService {
    List<UserDto> getAllUsers();

    UserDto getByUserId(Long userId);

    UserDto saveUser(UserDto userDto);

    UserDto updateUser(Long userId, UserDto userDto);

    void deleteUser(Long userId);

    User findById(Long userId);
}
