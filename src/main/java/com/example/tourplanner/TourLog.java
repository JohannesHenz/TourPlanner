package com.example.tourplanner;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class TourLog {
    private StringProperty description;
    private ObjectProperty<LocalDate> date;

    public TourLog(String description, LocalDate date) {
        this.description = new SimpleStringProperty(description);
        this.date = new SimpleObjectProperty<>(date);
    }


    public StringProperty descriptionProperty() {
        return description;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public LocalDate getDate() {
        return date.get();
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }



    // getters and setters
}