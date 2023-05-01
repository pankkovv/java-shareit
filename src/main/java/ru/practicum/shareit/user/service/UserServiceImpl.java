package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<UserDto> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public UserDto getUserById(long userId) {
        return repository.getById(userId);
    }

    @Override
    public UserDto saveUser(User user) {
        return repository.save(user);
    }

    @Override
    public UserDto updateUser(long userId, User user) {
        return repository.update(userId, user);
    }

    @Override
    public void deleteUser(long userId) {
        repository.delete(userId);
    }


}
