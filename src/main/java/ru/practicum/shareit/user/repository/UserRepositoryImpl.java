package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.messages.LogMessages;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMap;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
class UserRepositoryImpl implements UserRepository {
    private final UserMap userMap;
    private final List<User> users = new ArrayList<>();
    private long localId = 1;

    @Override
    public List<UserDto> findAll() {
        log.debug(String.valueOf(LogMessages.GET), users);
        return userMap.transferObj(users);
    }

    @Override
    public UserDto getById(long userId) {
        log.debug(String.valueOf(LogMessages.GET_ID), userId);
        return userMap.transferObj(users.stream().filter(user -> user.getId() == userId).findFirst().orElseThrow(() -> new NotFoundException(ExceptionMessages.NOT_FOUND_USER)));
    }

    @Override
    public UserDto save(User user) {
        if (validate(user)) {
            user.setId(generateId());
            users.add(user);
        }
        log.debug(String.valueOf(LogMessages.ADD), user);
        return userMap.transferObj(user);
    }

    @Override
    public UserDto update(long userId, User user) {
        user.setId(userId);
        validate(user);
        users.stream().forEach(lastUser -> {
            if (lastUser.getId() == userId) {
                updater(lastUser, user);
            }
        });
        log.debug(String.valueOf(LogMessages.UPDATE), getById(userId));
        return getById(userId);
    }

    @Override
    public void delete(long userId) {
        log.debug(String.valueOf(LogMessages.DELETE), userId);
        users.removeAll(users.stream().filter(user -> user.getId() == userId).collect(Collectors.toList()));
    }

    private long generateId() {
        return localId++;
    }

    private boolean validate(User user) {
        if (users.stream().filter(lastUser -> lastUser.getEmail().equals(user.getEmail())).collect(Collectors.toList()).isEmpty()) {
            return true;
        } else if (users.stream().filter(lastUser -> lastUser.getEmail().equals(user.getEmail()) && lastUser.getId() != user.getId()).collect(Collectors.toList()).isEmpty()) {
            return true;
        } else {
            throw new ConflictException(ExceptionMessages.DUPLICATE_EMAIL);
        }
    }

    private User updater(User lastUser, User newUser) {
        if (newUser.getName() != null) {
            lastUser.setName(newUser.getName());
        }
        if (newUser.getEmail() != null) {
            lastUser.setEmail(newUser.getEmail());
        }
        return lastUser;
    }
}
