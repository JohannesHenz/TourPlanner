package com.example.tourplanner;

//import javafx.scene.image.Image;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.collections.ObservableList;

public class Tour {
    private TourManager manager = TourManager.getInstance();
    private String id;
    private String name;
    private String description;
    private String from;
    private String to;
    private String transportType;
    private double distance;
    private double estimatedTime;
    @JsonIgnore
    private double popularity;
    @JsonIgnore
    private double childFriendliness;

    //private Image routeInformation;

    public Tour(){ //braucht leeren constructor für databind
    };

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

    public double getPopularity() {
    TourLogManager logManager = TourLogManager.getInstance();
        return logManager.getTourLogs().stream()
                .filter(log -> id.equals(log.getTourId()))
                .count();
    }

    public double getChildFriendliness() {
        TourLogManager logManager = TourLogManager.getInstance();
        //wir rechnen einfach tourloganzahl/schwierigkeitssumme für das inverse der durchschnittsschwierigkeit
        return (logManager.getTourLogs().stream()
                        .filter(log -> id.equals(log.getTourId()))
                        .count()
                /
                logManager.getTourLogs().stream()
                        .filter(log -> id.equals(log.getTourId()))
                        .mapToDouble(TourLog::getDifficulty)
                        .sum()
        );
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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
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