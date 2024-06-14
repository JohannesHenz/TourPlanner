package com.example.tourplanner;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;

public class PopupController {
    private TourViewModel viewModel = new TourViewModel();

    @FXML
    private TextField NameField;
    @FXML
    private TextField TourDescriptionField;
    @FXML
    private TextField FromField;
    @FXML
    private TextField ToField;
    @FXML
    private ComboBox<String> TransportTypeField;
    @FXML
    private Button NewTourSubmit;
    @FXML
    private Label header;
    @FXML
    private ListView<String> fromSuggestions;
    @FXML
    private ListView<String> toSuggestions;

    private Tour SelectedTour;
    private String originalFromLocation;
    private String originalToLocation;
    @FXML
    public void initialize() {
        // Bind UI components to ViewModel properties
        NameField.textProperty().bindBidirectional(viewModel.nameProperty());
        TourDescriptionField.textProperty().bindBidirectional(viewModel.descriptionProperty());
        FromField.textProperty().bindBidirectional(viewModel.fromLocationProperty());
        ToField.textProperty().bindBidirectional(viewModel.toLocationProperty());
        TransportTypeField.valueProperty().bindBidirectional(viewModel.transportTypeProperty());
        fromSuggestions.itemsProperty().bind(viewModel.fromSuggestionsProperty());
        toSuggestions.itemsProperty().bind(viewModel.toSuggestionsProperty());
        TransportTypeField.setItems(FXCollections.observableArrayList("car", "bike", "walking"));
    }

    public void initData(Tour tour, boolean isEdit) {
        if(isEdit) {
            this.SelectedTour = tour;
            originalFromLocation = SelectedTour.getFromLocation();
            originalToLocation = SelectedTour.getToLocation();
        }
        viewModel.initialize(tour, isEdit);
        header.setText(isEdit ? "Edit a Tour" : "Create a New Tour");

        FromField.addEventHandler(KeyEvent.KEY_RELEASED, event -> viewModel.fetchSuggestions(FromField.getText(), viewModel.fromSuggestionsProperty()));
        ToField.addEventHandler(KeyEvent.KEY_RELEASED, event -> viewModel.fetchSuggestions(ToField.getText(), viewModel.toSuggestionsProperty()));

        fromSuggestions.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Platform.runLater(() -> FromField.setText(newValue));
            }
        });

        toSuggestions.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Platform.runLater(() -> ToField.setText(newValue));
            }
        });
    }

    @FXML
    private void handleSubmit() {
        try {
            viewModel.validateAndSaveTour(originalFromLocation, originalToLocation);
            Stage stage = (Stage) NewTourSubmit.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
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