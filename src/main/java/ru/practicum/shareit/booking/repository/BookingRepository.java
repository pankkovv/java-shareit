package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.booking.status.StateStatus;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query(value = "select b.* from bookings as b " +
            "left join items as i on b.item_id = i.id " +
            "where b.id = ?1 and (b.booker_id = ?2 or i.owner_id = ?3) " +
            "group by b.id", nativeQuery = true)
    Booking getByIdBooking(Long bookingid, Long bookerId, Long ownerId);

    @Query(value = "select b.* from bookings as b " +
            "left join items as i on b.item_id = i.id " +
            "left join users as u on i.owner_id = u.id " +
            "where u.id = ?1 and b.booking_status like ?2 " +
            "group by b.id " +
            "order by b.start_date desc", nativeQuery = true)
    List<Booking> getBookingByUserIdAndBookingStatus(Long userId, BookingStatus bookingStatus);

    @Query(value = "select b.* from bookings as b " +
            "left join items as i on b.item_id = i.id " +
            "left join users as u on i.owner_id = u.id " +
            "where b.booker_id = ?1 and (b.booking_status like ?2 or b.booking_status like ?3)" +
            "group by b.id " +
            "order by b.start_date desc", nativeQuery = true)
    List<Booking> getBookingByUserIdAndBookingStatusFuture(Long userId, BookingStatus bookingStatusOne, BookingStatus bookingStatusTwo);
    List<Booking> findBookingByItem_Owner_IdAndBookingStatus(Long userId, BookingStatus bookingStatus);


    @Query(value = "select b.* from bookings as b " +
            "left join items as i on b.item_id = i.id " +
            "left join users as u on i.owner_id = u.id " +
            "where b.booker_id = ?1 " +
            "order by b.start_date desc", nativeQuery = true)
    List<Booking> findBookingByBookerId(Long userId);
}
