package com.example.tourplanner;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class PopupController {
    private TourManager manager = TourManager.getInstance();
    @FXML
    private TextField NameField;
    @FXML
    private TextField TourDescriptionField;
    @FXML
    private TextField FromField;
    @FXML
    private TextField ToField;
    @FXML
    private TextField TransportTypeField;
    @FXML
    private Button NewTourSubmit;
    @FXML
    private Tour SelectedTour;
    @FXML
    private boolean isEdit;
    @FXML
    private Label focusLabel;

    public void initData(Tour tour, boolean isEdit){
        //damit der setText geht, weil sonst regt heißt cant open window
        focusLabel.requestFocus();
        this.SelectedTour = tour;
        this.isEdit = isEdit;
        if(this.isEdit){
            NameField.setText(SelectedTour.getName());
            TourDescriptionField.setText(SelectedTour.getDescription());
            FromField.setText(SelectedTour.getFrom());
            ToField.setText(SelectedTour.getTo());
            TransportTypeField.setText(SelectedTour.getTransportType());
        }
    }

    // Method to handle the action when the confirm button is clicked
    @FXML
    private void handleSubmit() {
        if(!isEdit) {
            // Retrieve information from text fields
            String name = NameField.getText();
            String description = TourDescriptionField.getText();
            String from = FromField.getText();
            String to = ToField.getText();
            String transportType = TransportTypeField.getText();
            // Create a new instance of Tour with the retrieved information
            this.SelectedTour = new Tour(name, description); // Assuming Tour has a constructor that takes name and description
            System.out.println(name + "  " + description);
            this.SelectedTour.setFrom(from);
            this.SelectedTour.setTo(to);
            this.SelectedTour.setTransportType(transportType);
            if (name == null || name.isEmpty() || description == null) {
                // Show error message
                return;
            }
            manager.addTour(this.SelectedTour);
            // Close the window
            Stage stage = (Stage) NewTourSubmit.getScene().getWindow();
            stage.close();
        }
        else{
            // Retrieve information from text fields
            String name = NameField.getText();
            String description = TourDescriptionField.getText();
            String from = FromField.getText();
            String to = ToField.getText();
            String transportType = TransportTypeField.getText();
            // Create a new instance of Tour with the retrieved information
            this.SelectedTour.setName(name);
            this.SelectedTour.setDescription(description);
            this.SelectedTour.setFrom(from);
            this.SelectedTour.setTo(to);
            this.SelectedTour.setTransportType(transportType);
            if (name == null || name.isEmpty() || description == null) {
                // Show error message
                return;
            }
            manager.editTour(this.SelectedTour);
            // Close the window
            Stage stage = (Stage) NewTourSubmit.getScene().getWindow();
            stage.close();
        }
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
