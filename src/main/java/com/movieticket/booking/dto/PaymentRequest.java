package com.movieticket.booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    @NotNull(message = "bookingId is required")
    private Long bookingId;

    @NotBlank(message = "transactionRef is required")
    private String transactionRef;   // the gateway's payment reference (e.g. "razorpay_pay_abc123")

}
