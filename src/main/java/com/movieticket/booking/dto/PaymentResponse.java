package com.movieticket.booking.dto;

import com.movieticket.booking.entity.BookingStatus;
import com.movieticket.booking.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class PaymentResponse {

    private Long paymentId;
    private Long bookingId;
    private BigDecimal amount;
    private PaymentStatus paymentStatus;
    private BookingStatus bookingStatus;
    private String transactionRef;
    private String message;
}
