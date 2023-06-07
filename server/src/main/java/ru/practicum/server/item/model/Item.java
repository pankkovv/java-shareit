package ru.practicum.server.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.server.user.model.User;
import ru.practicum.server.request.model.ItemRequest;

import javax.persistence.*;


/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    private String name;
    private String description;
    @Column(name = "is_available")
    private Boolean available;
    @OneToOne
    @JoinColumn(name = "request_id")
    private ItemRequest request;
}
