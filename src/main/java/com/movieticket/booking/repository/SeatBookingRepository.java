package com.movieticket.booking.repository;

import com.movieticket.booking.entity.SeatBooking;
import com.movieticket.booking.entity.SeatStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SeatBookingRepository extends JpaRepository<SeatBooking, Long> {
    List<SeatBooking> findByShowId(Long showId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT sb FROM SeatBooking sb WHERE sb.id = :id")
    Optional<SeatBooking> findByIdForUpdate(@Param("id") Long id);

    // expired locks: status = LOCKED AND lockExpiresAt < now
    List<SeatBooking> findByStatusAndLockExpiresAtBefore(SeatStatus status, LocalDateTime time);

}
