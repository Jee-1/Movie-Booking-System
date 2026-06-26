package com.movieticket.booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieRequest {

    @NotBlank(message = "title is required")
    private String title;

    @NotBlank(message = "genre is required")
    private String genre;

    @Positive(message = "durationMins must be greater than 0")
    private int durationMins;

    @NotBlank(message = "language is required")
    private String language;

}
