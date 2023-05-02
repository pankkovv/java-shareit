package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query(value = "select it.*" +
            "from items as it" +
            "where it.name ilike '%:?1' or it.description ilike '%:?1'" +
            "group by it.id", nativeQuery = true)
    List<Item> searchItemByNameAndDescription(String text);
}
