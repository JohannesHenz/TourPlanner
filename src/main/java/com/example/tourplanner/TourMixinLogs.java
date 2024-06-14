package com.example.tourplanner;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
//helperclass for a @jsonignore that can be applied when writing jsons, but not automatically
public abstract class TourMixinLogs {
    @JsonIgnore
    private List<TourLogs> tourLogs;
}
