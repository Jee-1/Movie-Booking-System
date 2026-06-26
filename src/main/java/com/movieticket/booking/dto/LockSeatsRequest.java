package com.movieticket.booking.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LockSeatsRequest {

    @NotNull(message = "showId is required")
    private Long showId;

    @NotEmpty(message = "at least one seat must be selected")
    private List<Long> seatBooking;

}
