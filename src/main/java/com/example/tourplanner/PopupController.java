package com.example.tourplanner;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PopupController {

    @FXML
    private TextField NameField;
    @FXML
    private TextField TourDescriptionField;
    @FXML
    private Button NewTourSubmit;

    @FXML
    private Tour SelectedTour;

    public void initData(Tour tour){
        this.SelectedTour = tour;
        System.out.println(SelectedTour.getName());
        NameField.setPromptText(SelectedTour.getName());
    }

    // Method to handle the action when the confirm button is clicked
    @FXML
    private void handleSubmit() {
        // Retrieve information from text fields
        String name = NameField.getText();

        String description = TourDescriptionField.getText();
        // Create a new instance of Tour with the retrieved information
        this.SelectedTour = new Tour(name, description); // Assuming Tour has a constructor that takes name and description
        System.out.println(name + "  " +description);

        if (name == null || name.isEmpty() || description == null) {
            // Show error message
            return;
        }
        //hier muss die tour zurückgereicht werden

        // Close the window
        Stage stage = (Stage) NewTourSubmit.getScene().getWindow();
        stage.close();
    }

    /*@FXML
    protected void onEditTourButtonClick() { //rough draft at this point
        //Tour selectedTour = tourList.getSelectionModel().getSelectedItem();

        if (selectedTour != null) {
            String name = NameField.getText();
            String description = TourDescriptionField.getText();

            // Validate user input
            if (name == null || name.isEmpty() || description == null) {
                // Show error message
                return;
            }

            // Update selected tour
            selectedTour.setName(name);
            selectedTour.setDescription(description);

            // Clear input fields
            NameField.clear();
            TourDescriptionField.clear();
        }
    }*/
}
