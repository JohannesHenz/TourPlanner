package com.FHTW.tourplanner.ChristianCode;

import javafx.scene.image.Image;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

public class Tour {
    private String name;
    private String description;
    private String from;
    private String to;
    private String transportType;
    private double distance;
    private double estimatedTime;
    private Image routeInformation;

    public Tour(String name) {
        this.name = name;
    }

    public String toString(){
        return name;
    }

    public Tour(String name, String description) {
        this.name = name;
        this.description = description;
    }
    // Getters and setters for all properties

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

    public Image getRouteInformation() {
        return routeInformation;
    }

    public void setRouteInformation(Image routeInformation) {
        this.routeInformation = routeInformation;
    }
}