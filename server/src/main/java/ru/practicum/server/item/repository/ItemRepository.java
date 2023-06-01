package ru.practicum.server.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.server.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwnerId(Long userId, Pageable page);

    @Query(value = "select i " +
            "from Item as i " +
            "where (lower(i.name) like lower(concat('%', ?1, '%')) or lower(i.description) like lower(concat('%', ?1, '%'))) and i.available = true ")
    List<Item> search(String text, Pageable page);

    List<Item> findItemByRequest_Id(Long requestId);
}
