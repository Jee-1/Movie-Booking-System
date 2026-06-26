package com.movieticket.booking.service;

import com.movieticket.booking.entity.SeatBooking;
import com.movieticket.booking.entity.SeatStatus;
import com.movieticket.booking.repository.SeatBookingRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

// Background janitor: frees seats that were LOCKED but never paid for.
@Component
public class LockCleanupJob {

    private final SeatBookingRepository seatBookingRepository;

    public LockCleanupJob(SeatBookingRepository seatBookingRepository) {
        this.seatBookingRepository = seatBookingRepository;
    }

    // runs every 60 seconds
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void releaseExpiredLocks() {
        List<SeatBooking> expired = seatBookingRepository
                .findByStatusAndLockExpiresAtBefore(SeatStatus.LOCKED, LocalDateTime.now());

        for (SeatBooking sb : expired) {
            sb.setStatus(SeatStatus.AVAILABLE);
            sb.setLockedBy(null);
            sb.setLockExpiresAt(null);
            seatBookingRepository.save(sb);
        }

        if (!expired.isEmpty()) {
            System.out.println("[LockCleanupJob] Released " + expired.size() + " expired seat lock(s)");
        }
    }
}
