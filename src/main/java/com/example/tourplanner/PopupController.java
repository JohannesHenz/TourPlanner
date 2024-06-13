package com.example.tourplanner;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.UnsupportedEncodingException;


public class PopupController {
    private TourManager manager = TourManager.getInstance();
    private static final String API_KEY = "5b3ce3597851110001cf6248bc797bfd5e6d43bc8245ff9754a1bfe3";
    private static final String GEOCODE_URL = "https://api.openrouteservice.org/geocode/search?api_key=" + API_KEY + "&text=";
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
    private Tour SelectedTour;
    @FXML
    private boolean isEdit;
    @FXML
    private Label focusLabel;
    @FXML
    private ListView<String> fromSuggestions;
    @FXML
    private ListView<String> toSuggestions;
    private String clickedFromSuggestion;
    private String clickedToSuggestion;
    @FXML
    private Label header;

    public void initData(Tour tour, boolean isEdit){
        clickedFromSuggestion=""; //initilize so we dont get an error if nothing was clicked
        clickedToSuggestion="";

        //damit der setText geht, weil sonst heißt cant open window
        focusLabel.requestFocus();
        this.SelectedTour = tour;
        this.isEdit = isEdit;
        if(this.isEdit){
            NameField.setText(SelectedTour.getName());
            TourDescriptionField.setText(SelectedTour.getDescription());
            FromField.setText(SelectedTour.getFromLocation());
            ToField.setText(SelectedTour.getToLocation());
            TransportTypeField.setValue(SelectedTour.getTransportType());
            clickedFromSuggestion = SelectedTour.getFromLocation();
            clickedToSuggestion = SelectedTour.getToLocation();
            header.setText("Edit a Tour");
        }


        FromField.addEventHandler(KeyEvent.KEY_RELEASED, event -> fetchSuggestions(FromField.getText(), fromSuggestions));
        ToField.addEventHandler(KeyEvent.KEY_RELEASED, event -> fetchSuggestions(ToField.getText(), toSuggestions));

        // Set listeners for suggestions ListView clicks
        fromSuggestions.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Platform.runLater(() -> {
                    FromField.setText(newValue);
                    fetchSuggestions(FromField.getText(), fromSuggestions);
                    clickedFromSuggestion = newValue;
                    System.out.println(clickedFromSuggestion);
                });
            }
        });
        // nochmal for to
        toSuggestions.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Platform.runLater(() -> {
                    ToField.setText(newValue);
                    fetchSuggestions(ToField.getText(), toSuggestions);
                    clickedToSuggestion = newValue;
                    System.out.println(clickedToSuggestion);
                });
            }
        });
        ObservableList<String> transportOptions = FXCollections.observableArrayList("car", "bike", "walking");
        TransportTypeField.setItems(transportOptions);
    }

    private void fetchSuggestions(String query, ListView<String> suggestionsListView) {
        try {
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
        HttpClient client = HttpClient.newHttpClient();
        System.out.println(encodedQuery);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GEOCODE_URL + encodedQuery))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(this::parseSuggestions)
                .thenAccept(suggestions -> Platform.runLater(() -> {
                    suggestionsListView.getItems().clear();
                    suggestionsListView.getItems().addAll(suggestions);
                }));
    } catch (UnsupportedEncodingException e) {
        // Handle encoding exception
        e.printStackTrace();
    }
    }

    private List<String> parseSuggestions(String responseBody) {
        List<String> suggestions = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseBody);
            JsonNode features = root.path("features");
            for (JsonNode feature : features) {
                String place = feature.path("properties").path("label").asText();
                suggestions.add(place);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return suggestions;
    }

    // Method to handle the action when the confirm button is clicked
    @FXML
    private void handleSubmit() {
        if (!isEdit) {
            // Retrieve information from text fields
            String name = NameField.getText();
            String description = TourDescriptionField.getText();
            String from = FromField.getText();
            String to = ToField.getText();
            String transportType = TransportTypeField.getValue();
            // Create a new instance of Tour with the retrieved information
            this.SelectedTour = new Tour(name, description); // Assuming Tour has a constructor that takes name and description
            System.out.println(name + "  " + description);
            this.SelectedTour.setFromLocation(from);
            this.SelectedTour.setToLocation(to);
            this.SelectedTour.setTransportType(transportType);
            if (name == null || name.isEmpty() || description == null) {
                showError("Invalid input", "name can't be empty");
                return;
            }
            if(!clickedFromSuggestion.equals(from) || !clickedToSuggestion.equals(to)){
                showError("Invalid input", "from and to must be values from the list");
                return;
            }
            manager.addTour(this.SelectedTour);
            // Close the window
            Stage stage = (Stage) NewTourSubmit.getScene().getWindow();
            stage.close();
        } else {
            // Retrieve information from text fields
            String name = NameField.getText();
            String description = TourDescriptionField.getText();
            String from = FromField.getText();
            String to = ToField.getText();
            String transportType = TransportTypeField.getValue();
            // Create a new instance of Tour with the retrieved information
            this.SelectedTour.setName(name);
            this.SelectedTour.setDescription(description);
            this.SelectedTour.setFromLocation(from);
            this.SelectedTour.setToLocation(to);
            this.SelectedTour.setTransportType(transportType);
            if (name == null || name.isEmpty() || description == null) {
                showError("Invalid input", "name can't be empty");
                return;
            }
            if(!clickedFromSuggestion.equals(from) || !clickedToSuggestion.equals(to)){
                showError("Invalid input", "from and to must be values from the list");
                return;
            }
            manager.editTour(this.SelectedTour);
            // Close the window
            Stage stage = (Stage) NewTourSubmit.getScene().getWindow();
            stage.close();
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
