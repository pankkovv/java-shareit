package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Item updateItemByOwnerIdAndId(Long ownerId, Long id);

    List<Item> findItemsByOwnerId(Long userId);

    @Query(value = "select it.*" +
            "from items as it" +
            "where it.name ilike '%:?1' or it.description ilike '%:?1'" +
            "group by it.id", nativeQuery = true)
    List<Item> searchItemByNameAndDescription(String text);
}
