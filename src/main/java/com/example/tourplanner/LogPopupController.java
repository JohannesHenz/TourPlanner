package com.example.tourplanner;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LogPopupController {
    @FXML
    private DatePicker DateField;
    @FXML
    private TextField TimeField;
    @FXML
    private TextField CommentField;
    @FXML
    private TextField difficultyField;
    @FXML
    private TextField totalDistanceField;
    @FXML
    private TextField totalTimeField;
    @FXML
    private TextField RatingField;
    @FXML
    private Button NewTourLogSubmit;
    @FXML
    private Label header;

    private LogViewModel viewModel;

    public void initialize() {
        viewModel = new LogViewModel();

        // Bind UI controls to ViewModel properties
        DateField.valueProperty().bindBidirectional(viewModel.dateProperty());
        TimeField.textProperty().bindBidirectional(viewModel.timeProperty());
        CommentField.textProperty().bindBidirectional(viewModel.commentProperty());
        difficultyField.textProperty().bindBidirectional(viewModel.difficultyProperty());
        totalDistanceField.textProperty().bindBidirectional(viewModel.totalDistanceProperty());
        totalTimeField.textProperty().bindBidirectional(viewModel.totalTimeProperty());
        RatingField.textProperty().bindBidirectional(viewModel.ratingProperty());

        // Set header text based on edit mode
        viewModel.isEditProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                header.setText("Edit a Tour Log");
            } else {
                header.setText("Create a Tour Log");
            }
        });
    }

    public void initData(TourLogs log, Tour tour, boolean isEdit) {
        viewModel.initData(log, tour, isEdit);
    }

    @FXML
    private void handleSubmit() {
        try{
        viewModel.validateAndSaveLog();
        // Optionally close the window here or bind close actions in FXML
        Stage stage = (Stage) NewTourLogSubmit.getScene().getWindow();
        stage.close();
        } catch(Exception e){
            showError("Invalid input", e.getMessage());
        }
    }
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}