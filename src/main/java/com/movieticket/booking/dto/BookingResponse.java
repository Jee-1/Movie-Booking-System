package com.movieticket.booking.dto;

import com.movieticket.booking.entity.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class BookingResponse {

    private Long bookingId;
    private String movieTitle;
    private String screenName;
    private LocalDateTime showTime;
    private List<String> seatNumbers;
    private BigDecimal totalAmount;
    private BookingStatus status;
}
