package com.movieticket.booking.dto;

import com.movieticket.booking.entity.Theater;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScreenRequest {

    private Long theaterId;
    private String screenName;
    private int totalSeats;

}
