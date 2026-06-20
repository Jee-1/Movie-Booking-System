package com.movieticket.booking.repository;

import com.movieticket.booking.entity.SeatBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatBookingRepository extends JpaRepository<SeatBooking, Long> {
    List<SeatBooking> findByShowId(Long showId);

}
