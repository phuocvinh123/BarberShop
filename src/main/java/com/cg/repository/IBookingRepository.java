package com.cg.repository;

import com.cg.domain.Booking;
import com.cg.service.bookingService.bookingResponse.BookingListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IBookingRepository extends JpaRepository<Booking,Long> {
    @Query(value = "SELECT b FROM Booking b " +
            "WHERE " +
            "b.name LIKE :search OR " +
            "b.phoneNumber LIKE :search OR " +
            "b.stylist.name LIKE :search OR " +
            "EXISTS (SELECT 1 FROM BookingDetail bd WHERE bd.booking = b AND bd.hairDetail.name LIKE :search)")
    Page<Booking> searchEverything(String search, Pageable pageable);

    List<Booking> findBookingsByStylistId(Long id);
    List<Booking> findBookingsByUserId(Long id);
    List<Booking> findBookingsByCustomerId(Long id);

    List<Booking> findBookingById(Long id);
}


