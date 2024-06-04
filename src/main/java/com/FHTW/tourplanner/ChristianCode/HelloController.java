package com.FHTW.tourplanner.ChristianCode;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
<<<<<<< HEAD:src/main/java/com/example/tourplanner/HelloController.java
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
=======
>>>>>>> 759f379290b406b4b78cf9fd582b08e4535f7a3d:src/main/java/com/FHTW/tourplanner/ChristianCode/HelloController.java
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.time.LocalDate;

public class HelloController {
    @FXML
    private ListView<Tour> tourList;
    @FXML
    private TextField tourImageField;
    @FXML
    private ObservableList<Tour> tours;
    @FXML
    private ListView<TourLog> tourLogList;
    @FXML
    private TextField tourLogDescriptionField;
    @FXML
    private TextField tourLogDateField;
    @FXML
    private ObservableList<TourLog> tourLogs;
    @FXML
    private Tour SelectedTour;

    public Tour GetSelectedTour(){
        return SelectedTour;
    }

    public void initialize() {
        // Ensure tourList and tourLogList are not null
        if (tourList != null && tourLogList != null) {
            // Initialize tours and tourLogs
            tours = FXCollections.observableArrayList();
            tourLogs = FXCollections.observableArrayList();

            // Set items for tourList and tourLogList
            tourList.setItems(tours);
            tourLogList.setItems(tourLogs);
        } else {
            // Handle the case where tourList or tourLogList is null
            System.err.println("tourList or tourLogList is null");
        }
    }

    @FXML
    private void addTour() { //hervorragender Template für edit später :P
        SelectedTour = new Tour("New Tour");
        tourList.getItems().add(SelectedTour);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddPopup.fxml"));
            Parent root1 = fxmlLoader.load();
            PopupController controller = fxmlLoader.getController();
            controller.initData(SelectedTour);
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        }
        catch (Exception e){
            System.out.println("Cant load new window");
        }
    }


    @FXML
    private void editTour() {
        // Implement editing a tour functionality here
        // You can get the selected item from the list view and modify it
        Tour selectedTour = tourList.getSelectionModel().getSelectedItem();
        if (selectedTour != null) {
            // Implement editing functionality here
        }
    }

    @FXML
    private void deleteTour() {
        // Implement deleting a tour functionality here
        // You can get the selected item from the list view and remove it
        Tour selectedTour = tourList.getSelectionModel().getSelectedItem();
        if (selectedTour != null) {
            tourList.getItems().remove(selectedTour);
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