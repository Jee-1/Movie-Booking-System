package com.movieticket.booking.service;

import com.movieticket.booking.dto.PaymentRequest;
import com.movieticket.booking.dto.PaymentResponse;
import com.movieticket.booking.entity.Booking;
import com.movieticket.booking.entity.BookingStatus;
import com.movieticket.booking.entity.Payments;
import com.movieticket.booking.entity.PaymentStatus;
import com.movieticket.booking.exception.ResourceNotFoundException;
import com.movieticket.booking.repository.BookingRepository;
import com.movieticket.booking.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    public PaymentService(PaymentRepository paymentRepository, BookingRepository bookingRepository) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public PaymentResponse processPayment(PaymentRequest req, String userEmail) {

        // 1. IDEMPOTENCY: if we've already processed this transactionRef, return the existing result.
        //    (Gateways retry/resend the same confirmation — we must not pay or confirm twice.)
        Payments existing = paymentRepository.findByTransactionRef(req.getTransactionRef()).orElse(null);
        if (existing != null) {
            return toResponse(existing, existing.getBooking(), "Payment already processed (idempotent)");
        }

        // 2. load the booking
        Booking booking = bookingRepository.findById(req.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + req.getBookingId()));

        // 3. OWNERSHIP: you can only pay for your own booking (identity from the JWT)
        if (!booking.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("This booking does not belong to you");
        }

        // 4. STATE check: only a PENDING booking can be paid
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new RuntimeException("Booking is not payable. Current status: " + booking.getStatus());
        }

        // 5. record the payment (simulated gateway → always SUCCESS here)
        Payments payment = new Payments();
        payment.setBooking(booking);
        payment.setAmount(booking.getTotalAmount());
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setTransactionRef(req.getTransactionRef());
        Payments savedPayment = paymentRepository.save(payment);

        // 6. confirm the booking (PENDING -> CONFIRMED) — atomic with the payment row
        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);

        return toResponse(savedPayment, booking, "Payment successful, booking confirmed");
    }

    private PaymentResponse toResponse(Payments p, Booking booking, String message) {
        return new PaymentResponse(
                p.getId(),
                booking.getId(),
                p.getAmount(),
                p.getStatus(),
                booking.getStatus(),
                p.getTransactionRef(),
                message
        );
    }
}
