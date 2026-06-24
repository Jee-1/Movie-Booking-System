package com.movieticket.booking.dto;

import com.movieticket.booking.entity.SeatStatus;
import com.movieticket.booking.entity.SeatType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeatMapItem {

    private Long seatBookingId;
    private String seatNumber;
    private SeatType seatType;
    private SeatStatus status;

}
