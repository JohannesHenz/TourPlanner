package com.example.tourplanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private ListView<String> tourList;

    @FXML
    public void initialize() {
        ObservableList<String> tours = FXCollections.observableArrayList("Wienerwald", "Dopplerhütte", "Figlwarté", "Dorfrunde");
        tourList.setItems(tours);
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}