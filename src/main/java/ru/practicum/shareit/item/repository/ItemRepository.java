package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, CrudRepository<Item, Long> {

    List<Item> findByOwnerId(Long userId);
    List<Item> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String t1, String t2);
}
