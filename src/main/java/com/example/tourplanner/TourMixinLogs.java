package com.example.tourplanner;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

public abstract class TourMixinLogs {
    @JsonIgnore
    private List<TourLogs> tourLogs;
}
