package com.example.tourplanner;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class TourLogs {
    private String id;
    @JsonIgnore
    private Tour tour;
    private String TourId;
    private LocalDate date;
    private float time;
    private String comment;
    private float difficulty;
    private float totalDistance;
    private float totalTime;
    private float rating;

    public TourLogs(){ //braucht leeren constructor für databind
    };
    public String getTourId() {
        return TourId;
    }

    public void setTourId(String tourId) {
        TourId = tourId;
    }

    public String getId() {
        return id;
    }

    public void setId(String logId) {
        id = logId;
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(float difficulty) {
        this.difficulty = difficulty;
    }

    public float getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(float totalDistance) {
        this.totalDistance = totalDistance;
    }

    public float getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(float totalTime) {
        this.totalTime = totalTime;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}