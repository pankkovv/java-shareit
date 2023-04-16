package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
class UserRepositoryImpl implements UserRepository {
    private final List<User> users = new ArrayList<>();
    private long localId = 1;

    @Override
    public List<User> findAll() {
        return users;
    }

    @Override
    public User getById(long userId) {
        return users.stream().map(user -> {
            if (user.getId() == userId) {
                return user;
            } else {
                throw new NotFoundException("User not found.");
            }
        }).findFirst().get();
    }

    @Override
    public User save(User user) {
        if (validate(user)) {
            user.setId(generateId());
            users.add(user);
        }
        return user;
    }

    @Override
    public User update(long userId, User user) {
        return users.stream().map(lastUser -> {
            if(validate(user)){
                if (lastUser.getId() == userId) {
                    if (user.getName() != null) {
                        lastUser.setName(user.getName());
                    }
                    if (user.getEmail() != null) {
                        lastUser.setEmail(user.getEmail());
                    }
                }
            }
            return getById(userId);
        }).findFirst().get();
    }

    @Override
    public void delete(long userId) {
        users.removeAll(users.stream().filter(user -> user.getId() == userId).collect(Collectors.toList()));
    }

    private long generateId() {
        return localId++;
    }

    private boolean validate(User user) {
        if (users.stream().filter(lastUser -> lastUser.getEmail().equals(user.getEmail())).collect(Collectors.toList()).isEmpty()) {
            return true;
        } else {
            throw new RuntimeException("Email duplicate conflict.");
        }
    }
}
