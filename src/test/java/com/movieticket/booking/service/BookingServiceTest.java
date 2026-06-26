package com.movieticket.booking.service;

import com.movieticket.booking.dto.LockSeatsRequest;
import com.movieticket.booking.entity.Seat;
import com.movieticket.booking.entity.SeatBooking;
import com.movieticket.booking.entity.SeatStatus;
import com.movieticket.booking.entity.User;
import com.movieticket.booking.repository.BookingRepository;
import com.movieticket.booking.repository.BookingSeatRepository;
import com.movieticket.booking.repository.SeatBookingRepository;
import com.movieticket.booking.repository.ShowRepository;
import com.movieticket.booking.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock private SeatBookingRepository seatBookingRepository;
    @Mock private UserRepository userRepository;
    @Mock private ShowRepository showRepository;
    @Mock private BookingRepository bookingRepository;
    @Mock private BookingSeatRepository bookingSeatRepository;

    @InjectMocks private BookingService bookingService;

    private User john() {
        User u = new User();
        u.setId(5L);
        u.setEmail("john@movies.com");
        return u;
    }

    @Test
    void lockSeats_whenSeatAvailable_locksItForTheUser() {
        when(userRepository.findByEmail("john@movies.com")).thenReturn(Optional.of(john()));

        SeatBooking sb = new SeatBooking();
        sb.setId(1L);
        sb.setStatus(SeatStatus.AVAILABLE);
        when(seatBookingRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(sb));

        LockSeatsRequest req = new LockSeatsRequest(10L, List.of(1L));
        bookingService.lockSeats(req, "john@movies.com");

        // the seat should now be LOCKED, stamped with the user's id
        assertEquals(SeatStatus.LOCKED, sb.getStatus());
        assertEquals(5L, sb.getLockedBy());
        assertNotNull(sb.getLockExpiresAt());
        verify(seatBookingRepository).save(sb);
    }

    @Test
    void lockSeats_whenSeatAlreadyTaken_throwsAndDoesNotSave() {
        when(userRepository.findByEmail("john@movies.com")).thenReturn(Optional.of(john()));

        Seat seat = new Seat();
        seat.setSeatNumber("A1");
        SeatBooking taken = new SeatBooking();
        taken.setId(1L);
        taken.setStatus(SeatStatus.BOOKED);   // not available
        taken.setSeat(seat);
        when(seatBookingRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(taken));

        LockSeatsRequest req = new LockSeatsRequest(10L, List.of(1L));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> bookingService.lockSeats(req, "john@movies.com"));
        assertTrue(ex.getMessage().contains("already taken"));
        verify(seatBookingRepository, never()).save(any());
    }
}
