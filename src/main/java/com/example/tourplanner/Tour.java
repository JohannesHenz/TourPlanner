package com.example.tourplanner;

//import javafx.scene.image.Image;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.collections.ObservableList;

import java.util.List;

public class Tour {
    private TourManager manager = TourManager.getInstance();
    private String id;
    private String name;
    private String description;
    private String fromLocation;
    private String toLocation;
    private String transportType;
    private double distance;
    private double estimatedTime;
    private String mapImageUrl;
    private double popularity;
    @JsonIgnore
    private double childFriendliness;
    private List<TourLogs> tourLogs;

    //private Image routeInformation;

    public String getMapImageUrl() {
        return mapImageUrl;
    }

    public void setMapImageUrl(String mapImageUrl) {
        this.mapImageUrl = mapImageUrl;
    }

    public Tour(){ //braucht leeren constructor für databind
    };
    public List<TourLogs> getTourLogs() {
        return tourLogs;
    } // with this setup the list should be ignored for serialization but not deserialization
    public void setTourLogs(List<TourLogs> tourLogs) {
        this.tourLogs = tourLogs;
    }

    public Tour(String name) {
        this.id = manager.getNewID();
        this.name = name;
    }

    public Tour(String name, String description) {
        this.id = manager.getNewID();
        this.name = name;
        this.description = description;
    }
    // Getters and setters for all properties
    @JsonIgnore
    public double getPopularity() {
    TourLogManager logManager = TourLogManager.getInstance();
        return logManager.getTourLogs().stream()
                .filter(log -> id.equals(log.getTourId()))
                .count();
    }
    @JsonIgnore
    public double getChildFriendliness() {
        TourLogManager logManager = TourLogManager.getInstance();
        //wir rechnen einfach tourloganzahl/schwierigkeitssumme für das inverse der durchschnittsschwierigkeit
        return (10*logManager.getTourLogs().stream()
                        .filter(log -> id.equals(log.getTourId()))
                        .count()
                /
                logManager.getTourLogs().stream()
                        .filter(log -> id.equals(log.getTourId()))
                        .mapToDouble(TourLogs::getDifficulty)
                        .sum()
        );
    }
    @JsonIgnore
    public double getAverageTravelTime(){
        TourLogManager logManager = TourLogManager.getInstance();
        //wir rechnen einfach tourloganzahl/schwierigkeitssumme für das inverse der durchschnittsschwierigkeit
        return logManager.getTourLogs().stream()
                        .filter(log -> id.equals(log.getTourId()))
                        .mapToDouble(TourLogs::getTotalTime)
                        .sum()
                /
        logManager.getTourLogs().stream()
                .filter(log -> id.equals(log.getTourId()))
                .count();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String from) {
        this.fromLocation = from;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String to) {
        this.toLocation = to;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(double estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    @Override
    public String toString() {
        return name;
    }
    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id=id;
    }
}