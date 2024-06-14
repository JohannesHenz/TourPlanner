package com.example.tourplanner;
//helperclass for a @jsonignore that can be applied when writing jsons, but not automatically
import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class TourMixin {
    @JsonIgnore
    private String id;
}
