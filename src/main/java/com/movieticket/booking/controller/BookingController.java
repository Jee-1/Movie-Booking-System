package com.movieticket.booking.controller;

import com.movieticket.booking.dto.BookingResponse;
import com.movieticket.booking.dto.ConfirmBookingRequest;
import com.movieticket.booking.dto.LockSeatsRequest;
import com.movieticket.booking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/lock")
    public ResponseEntity<String> lockSeats(@Valid @RequestBody LockSeatsRequest req,
                                            Authentication authentication) {
        String email = authentication.getName();        // ← the logged-in user's email, from the JWT
        bookingService.lockSeats(req, email);
        return ResponseEntity.ok("Seats locked successfully");
    }

    @PostMapping("/confirm")
    public BookingResponse confirmBooking(@Valid @RequestBody ConfirmBookingRequest req,
                                          Authentication authentication) {
        return bookingService.confirmBooking(req, authentication.getName());
    }
}

