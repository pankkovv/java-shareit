package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long>, CrudRepository<Booking, Long> {
    @Query(value = "select b.* from bookings as b " +
            "left join items as i on b.item_id = i.id " +
            "where b.id = ?1 and (b.booker_id = ?2 or i.owner_id = ?2) " +
            "group by b.id", nativeQuery = true)
    Booking getByIdBooking(Long bookingId, Long bookerId);


    @Query(value = "select b.* from bookings as b " +
            "left join items as i on b.item_id = i.id " +
            "left join users as u on i.owner_id = u.id " +
            "where b.booker_id = ?1 " +
            "order by b.start_date desc", nativeQuery = true)
    List<Booking> getBookingByBookerId(Long userId);


    @Query(value = "select b.* from bookings as b " +
            "left join items as i on b.item_id = i.id " +
            "left join users as u on i.owner_id = u.id " +
            "where b.booker_id = ?1 and b.booking_status like ?2 " +
            "group by b.id " +
            "order by b.start_date desc", nativeQuery = true)
    List<Booking> getBookingByUserIdAndBookingStatus(Long userId, String bookingStatus);

    @Query(value = "select b.* from bookings as b " +
            "left join items as i on b.item_id = i.id " +
            "left join users as u on i.owner_id = u.id " +
            "where b.booker_id = ?1 and (b.booking_status like ?2 or b.booking_status like ?3) " +
            "group by b.id " +
            "order by b.start_date desc", nativeQuery = true)
    List<Booking> getBookingByUserIdAndBookingStatusFuture(Long userId, String bookingStatusOne, String bookingStatusTwo);


    @Query(value = "select b.* from bookings as b " +
            "left join items i on i.id = b.item_id " +
            "where i.owner_id = ?1 " +
            "order by b.start_date desc", nativeQuery = true)
    List<Booking> getBookingByOwnerId(Long userId);


    @Query(value = "select b.* from bookings as b " +
            "left join items i on i.id = b.item_id " +
            "where i.owner_id = ?1 and b.booking_status = ?2 " +
            "order by b.start_date desc", nativeQuery = true)
    List<Booking> getByItemOwnerIdAndBookingStatus(Long userId, String bookingStatus);

    @Query(value = "select b.* from bookings as b " +
            "left join items as i on b.item_id = i.id " +
            "where i.owner_id = ?1 and (b.booking_status like ?2 or b.booking_status like ?3) " +
            "order by b.start_date desc", nativeQuery = true)
    List<Booking> getBookingByItemOwnerIdAndBookingStatusFuture(Long userId, String bookingStatusOne, String bookingStatusTwo);

    @Query(value = "select b.* from bookings as b " +
            "left join items as i on b.item_id = i.id " +
            "left join users as u on i.owner_id = u.id " +
            "where i.owner_id = ?1 and i.id = ?2 and b.booking_status like 'APPROVED' " +
            "order by b.start_date desc " +
            "limit 1", nativeQuery = true)
    Booking findByItemIdLast(Long userId, Long itemId);

    @Query(value = "select b.* from bookings as b " +
            "left join items as i on b.item_id = i.id " +
            "left join users as u on i.owner_id = u.id " +
            "where i.owner_id = ?1 and i.id = ?2 and b.booking_status like 'APPROVED' and b.start_date > ?3 " +
            "order by b.start_date asc " +
            "limit 1", nativeQuery = true)
    Booking findByItemIdNext(Long userId, Long itemId, LocalDateTime time);

    @Query(value = "select b.* from bookings as b " +
            "left join items as i on b.item_id = i.id " +
            "left join users as u on i.owner_id = u.id " +
            "where b.booker_id = ?1 and i.id = ?2 and i.owner_id != ?1 and b.start_date < ?3 " +
            "order by b.end_date desc " +
            "limit 1", nativeQuery = true)
    Booking getBookingByBookerIdAndItemId(Long userId, Long itemId, LocalDateTime time);
}
