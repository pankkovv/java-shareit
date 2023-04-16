package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    List<User> findAll();
    User getById(long userId);
    User save(User user);
    User update(long userId, User user);
    void delete(long userId);
}
