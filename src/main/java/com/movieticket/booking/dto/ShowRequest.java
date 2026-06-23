package com.movieticket.booking.dto;

import com.movieticket.booking.entity.Movie;
import com.movieticket.booking.entity.Screen;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShowRequest {

    private Long movieId;
    private Long screenId;
    private LocalDateTime showTime;
    private BigDecimal basePrice;

}
