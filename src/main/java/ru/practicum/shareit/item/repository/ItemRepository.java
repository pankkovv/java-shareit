package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, CrudRepository<Item, Long> {
    List<Item> findByOwnerId(Long userId);

    @Query(value = "select i " +
            "from Item as i " +
            "where (lower(i.name) like lower(concat('%', ?1, '%')) or lower(i.description) like lower(concat('%', ?1, '%'))) and i.available = true ")
    List<Item> search(String text);
}
