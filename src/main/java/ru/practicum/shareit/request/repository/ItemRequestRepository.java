package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findItemRequestByRequestor_Id(Long userId);

//    @Query(value = "select ir " +
//            "from ItemRequest as ir " +
//            "where ir.id != ?1 " +
//            "order by ir.start desc ")
    List<ItemRequest> findItemRequestByIdNotOrderByCreatedDesc(Long userId, Pageable page);

}
