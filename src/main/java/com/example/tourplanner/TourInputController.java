package com.example.tourplanner;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class TourInputController {
    @FXML
    private TextField nameField;


    // Other fields for tour description, from, to, transport type, tour distance, estimated time, route information

    private TourManager tourManager;

    public void setTourManager(TourManager tourManager) {
        this.tourManager = tourManager;
    }

    @FXML
    private void addTour() {
        // Implement logic to add the tour with the input data
        // For example:
        String name = nameField.getText();
        // Get other input fields and create a Tour object
        //Tour newTour = new Tour(name);
        // Add the tour to the tour manager
        //tourManager.addTour(newTour);
    }

    @FXML
    private void cancel() {
        // Implement logic to close the input window without adding a tour
        // For example: close the window or clear input fields
    }
}
