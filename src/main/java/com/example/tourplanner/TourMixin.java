package com.example.tourplanner;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class TourMixin {
    @JsonIgnore
    private String id;
}
