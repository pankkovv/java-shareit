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
        log.debug(LogMessages.GET.label, users);
        return userMap.transferObj(users);
    }

    @Override
    public UserDto getById(Long userId) {
        log.debug(LogMessages.GET_ID.label, userId);
        return userMap.transferObj(users.stream().filter(user -> user.getId() == userId).findFirst().orElseThrow(() -> new NotFoundException(ExceptionMessages.NOT_FOUND_USER.label)));
    }

    @Override
    public UserDto save(User user) {
        if (validate(user)) {
            user.setId(generateId());
            users.add(user);
        }
        log.debug(LogMessages.ADD.label, user);
        return userMap.transferObj(user);
    }

    @Override
    public UserDto update(Long userId, User user) {
        user.setId(userId);
        validate(user);
        users.forEach(lastUser -> {
            if (lastUser.getId() == userId) {
                updater(lastUser, user);
            }
        });
        log.debug(LogMessages.UPDATE.label, getById(userId));
        return getById(userId);
    }

    @Override
    public void delete(Long userId) {
        log.debug(LogMessages.DELETE.label, userId);
        users.removeAll(users.stream().filter(user -> user.getId() == userId).collect(Collectors.toList()));
    }

    private long generateId() {
        return localId++;
    }

    private boolean validate(User user) {
        if (users.stream().noneMatch(lastUser -> lastUser.getEmail().equals(user.getEmail()))) {
            return true;
        } else if (users.stream().noneMatch(lastUser -> lastUser.getEmail().equals(user.getEmail()) && lastUser.getId() != user.getId())) {
            return true;
        } else {
            throw new ConflictException(ExceptionMessages.DUPLICATE_EMAIL.label);
        }
    }

    private void updater(User lastUser, User newUser) {
        if (newUser.getName() != null) {
            lastUser.setName(newUser.getName());
        }
        if (newUser.getEmail() != null) {
            lastUser.setEmail(newUser.getEmail());
        }
    }
}
