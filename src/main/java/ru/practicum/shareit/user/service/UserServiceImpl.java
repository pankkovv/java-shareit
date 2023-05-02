package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMap;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers() {
        return null;
    }

    @Override
    public UserDto getByUserId(Long userId) {
        User user = userRepository.getById(userId);
        return UserMap.mapToUserDto(user);
    }

    @Override
    public UserDto saveUser(User user) {
        return null;
    }

    @Override
    public UserDto updateUser(Long userId, User user) {
        return null;
    }

    @Override
    public void deleteUser(Long userId) {

    }
}
