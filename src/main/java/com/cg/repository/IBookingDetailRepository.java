package com.cg.repository;

import com.cg.domain.BookingDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBookingDetailRepository extends JpaRepository<BookingDetail,Long> {
}
