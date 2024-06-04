package com.FHTW.tourplanner.Model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TourLog {
    private LocalDateTime dateTime;
    private String comment;
    private String difficulty;
    private int totalDistance;
    private int totalTime;
    private int rating;


}