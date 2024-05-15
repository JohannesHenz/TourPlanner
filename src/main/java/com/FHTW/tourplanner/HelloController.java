package com.FHTW.tourplanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

import java.time.LocalDate;

public class HelloController {
    @FXML
    private ListView<Tour> tourList;

    @FXML
    private TextField tourNameField;

    @FXML
    private TextField tourImageField;

    private ObservableList<Tour> tours;


        @FXML
        private ListView<TourLog> tourLogList;

        @FXML
        private TextField tourLogDescriptionField;

        @FXML
        private TextField tourLogDateField;

        private ObservableList<TourLog> tourLogs;

    public void initialize() {
        tours = FXCollections.observableArrayList();
        tourList.setItems(tours);
        tourLogs = FXCollections.observableArrayList();
        tourLogList.setItems(tourLogs);
    }

    @FXML
    protected void onAddTourButtonClick() {
        String name = tourNameField.getText();
        Image image = new Image(tourImageField.getText());

        // Validate user input
        if (name == null || name.isEmpty() || image == null) {
            // Show error message
            return;
        }

        // Add new tour
        tours.add(new Tour(name, image));

        // Clear input fields
        tourNameField.clear();
        tourImageField.clear();
    }

    @FXML
    protected void onEditTourButtonClick() {
        Tour selectedTour = tourList.getSelectionModel().getSelectedItem();

        if (selectedTour != null) {
            String name = tourNameField.getText();
            Image image = new Image(tourImageField.getText());

            // Validate user input
            if (name == null || name.isEmpty() || image == null) {
                // Show error message
                return;
            }

            // Update selected tour
            selectedTour.setName(name);
            selectedTour.setImage(image);

            // Clear input fields
            tourNameField.clear();
            tourImageField.clear();
        }
    }

    @FXML
    protected void onDeleteTourButtonClick() {
        Tour selectedTour = tourList.getSelectionModel().getSelectedItem();

        if (selectedTour != null) {
            // Delete selected tour
            tours.remove(selectedTour);
        }
    }

    @FXML
    protected void onAddTourLogButtonClick() {
        String description = tourLogDescriptionField.getText();
        LocalDate date = LocalDate.parse(tourLogDateField.getText());

        // Validate user input
        if (description == null || description.isEmpty() || date == null) {
            // Show error message
            return;
        }

        // Add new tour log
        tourLogs.add(new TourLog(description, date));

        // Clear input fields
        tourLogDescriptionField.clear();
        tourLogDateField.clear();
    }

    @FXML
    protected void onEditTourLogButtonClick() {
        TourLog selectedTourLog = tourLogList.getSelectionModel().getSelectedItem();

        if (selectedTourLog != null) {
            String description = tourLogDescriptionField.getText();
            LocalDate date = LocalDate.parse(tourLogDateField.getText());

            // Validate user input
            if (description == null || description.isEmpty() || date == null) {
                // Show error message
                return;
            }

            // Update selected tour log
            selectedTourLog.setDescription(description);
            selectedTourLog.setDate(date);

            // Clear input fields
            tourLogDescriptionField.clear();
            tourLogDateField.clear();
        }
    }

    @FXML
    protected void onDeleteTourLogButtonClick() {
        TourLog selectedTourLog = tourLogList.getSelectionModel().getSelectedItem();

        if (selectedTourLog != null) {
            // Delete selected tour log
            tourLogs.remove(selectedTourLog);
        }
    }
}