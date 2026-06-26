package com.movieticket.booking.service;

import com.movieticket.booking.dto.BookingResponse;
import com.movieticket.booking.dto.ConfirmBookingRequest;
import com.movieticket.booking.dto.LockSeatsRequest;
import com.movieticket.booking.entity.*;
import com.movieticket.booking.exception.ResourceNotFoundException;
import com.movieticket.booking.repository.BookingRepository;
import com.movieticket.booking.repository.BookingSeatRepository;
import com.movieticket.booking.repository.SeatBookingRepository;
import com.movieticket.booking.repository.ShowRepository;
import com.movieticket.booking.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {

    private final SeatBookingRepository seatBookingRepository;
    private final UserRepository userRepository;
    private final ShowRepository showRepository;
    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;

    private static final int LOCK_MINUTES = 5;

    public BookingService(SeatBookingRepository seatBookingRepository,
                          UserRepository userRepository,
                          ShowRepository showRepository,
                          BookingRepository bookingRepository,
                          BookingSeatRepository bookingSeatRepository) {
        this.seatBookingRepository = seatBookingRepository;
        this.userRepository = userRepository;
        this.showRepository = showRepository;
        this.bookingRepository = bookingRepository;
        this.bookingSeatRepository = bookingSeatRepository;
    }

    @Transactional   // the FOR UPDATE lock only holds inside a transaction
    public void lockSeats(LockSeatsRequest req, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        for (Long id : req.getSeatBooking()) {
            SeatBooking sb = seatBookingRepository.findByIdForUpdate(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Seat Not Found: " + id));
            if (sb.getStatus() != SeatStatus.AVAILABLE)
                throw new RuntimeException("Seat already taken: " + sb.getSeat().getSeatNumber());
            sb.setStatus(SeatStatus.LOCKED);
            sb.setLockedBy(user.getId());
            sb.setLockExpiresAt(LocalDateTime.now().plusMinutes(LOCK_MINUTES));
            seatBookingRepository.save(sb);
        }
    }

    @Transactional   // booking + all seat updates must commit (or roll back) together
    public BookingResponse confirmBooking(ConfirmBookingRequest req, String userEmail) {
        // 1. who is acting (from the JWT) + what show
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        Show show = showRepository.findById(req.getShowId())
                .orElseThrow(() -> new ResourceNotFoundException("Show not found: " + req.getShowId()));

        // 2. create the order header now (status PENDING until payment in Phase 10).
        //    Save it first so it has an id — BookingSeat rows need it as their FK.
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setShow(show);
        booking.setStatus(BookingStatus.PENDING);
        booking.setCreatedAt(LocalDateTime.now());
        booking.setTotalAmount(BigDecimal.ZERO);          // running total, filled below
        Booking savedBooking = bookingRepository.save(booking);

        BigDecimal total = BigDecimal.ZERO;
        List<String> seatNumbers = new ArrayList<>();

        // 3. turn each LOCKED-by-me seat into a BOOKED line item
        for (Long id : req.getSeatBookingIds()) {
            SeatBooking sb = seatBookingRepository.findByIdForUpdate(id)   // re-lock the row
                    .orElseThrow(() -> new ResourceNotFoundException("Seat Not Found: " + id));

            // 3a. it must be LOCKED by THIS user
            if (sb.getStatus() != SeatStatus.LOCKED || !user.getId().equals(sb.getLockedBy())) {
                throw new RuntimeException("Seat is not locked by you: " + sb.getSeat().getSeatNumber());
            }
            // 3b. the lock must not have expired
            if (sb.getLockExpiresAt() == null || sb.getLockExpiresAt().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Lock expired for seat: " + sb.getSeat().getSeatNumber());
            }

            // 3c. flip to BOOKED and clear the lock fields
            sb.setStatus(SeatStatus.BOOKED);
            sb.setLockedBy(null);
            sb.setLockExpiresAt(null);
            seatBookingRepository.save(sb);

            // 3d. record the line item (which physical seat this booking covers)
            BookingSeat bookingSeat = new BookingSeat();
            bookingSeat.setBooking(savedBooking);
            bookingSeat.setSeat(sb.getSeat());
            bookingSeatRepository.save(bookingSeat);

            // 3e. add this seat's price to the total + record its number for the response
            total = total.add(show.getBasePrice());
            seatNumbers.add(sb.getSeat().getSeatNumber());
        }

        // 4. finalize the header total and return a clean DTO (never the raw entity)
        savedBooking.setTotalAmount(total);
        bookingRepository.save(savedBooking);

        return new BookingResponse(
                savedBooking.getId(),
                show.getMovie().getTitle(),
                show.getScreen().getScreenName(),
                show.getShowTime(),
                seatNumbers,
                total,
                savedBooking.getStatus()
        );
    }
}
