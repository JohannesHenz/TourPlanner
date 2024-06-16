package com.example.tourplanner;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TourViewModel {
    private TourManager manager = TourManager.getInstance();
    private StringProperty name = new SimpleStringProperty();
    private StringProperty description = new SimpleStringProperty();
    private StringProperty fromLocation = new SimpleStringProperty();
    private StringProperty toLocation = new SimpleStringProperty();
    private StringProperty transportType = new SimpleStringProperty("driving-car");
    private BooleanProperty editMode = new SimpleBooleanProperty(false);
    private ObjectProperty<Tour> selectedTour = new SimpleObjectProperty<>();
    private ListProperty<String> fromSuggestions = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ListProperty<String> toSuggestions = new SimpleListProperty<>(FXCollections.observableArrayList());

    private static final String API_KEY = "5b3ce3597851110001cf6248bc797bfd5e6d43bc8245ff9754a1bfe3";
    private static final String GEOCODE_URL = "https://api.openrouteservice.org/geocode/search?api_key=" + API_KEY + "&text=";

    // Getters for properties
    public StringProperty nameProperty() { return name; }
    public StringProperty descriptionProperty() { return description; }
    public StringProperty fromLocationProperty() { return fromLocation; }
    public StringProperty toLocationProperty() { return toLocation; }
    public StringProperty transportTypeProperty() { return transportType; }
    public BooleanProperty editModeProperty() { return editMode; }
    public ObjectProperty<Tour> selectedTourProperty() { return selectedTour; }
    public ListProperty<String> fromSuggestionsProperty() { return fromSuggestions; }
    public ListProperty<String> toSuggestionsProperty() { return toSuggestions; }

    public void initialize(Tour tour, boolean isEdit) {
        if (isEdit && tour != null) {
            selectedTour.set(tour);
            name.set(tour.getName());
            description.set(tour.getDescription());
            fromLocation.set(tour.getFromLocation());
            toLocation.set(tour.getToLocation());
            transportType.set(tour.getTransportType());
            editMode.set(true);
        } else {
            editMode.set(false);
            transportType.set("driving-car");
        }
    }

    public void saveTour() {
        if (editMode.get()) {
            // Update existing tour
            Tour tour = selectedTour.get();
            tour.setName(name.get());
            tour.setDescription(description.get());
            tour.setFromLocation(fromLocation.get());
            tour.setToLocation(toLocation.get());
            tour.setTransportType(transportType.get());
            manager.editTour(tour);
        } else {
            // Create new tour
            Tour tour = new Tour(name.get(), description.get());
            tour.setFromLocation(fromLocation.get());
            tour.setToLocation(toLocation.get());
            tour.setTransportType(transportType.get());
            manager.addTour(tour);
            System.out.println("ende von create save tour");
        }
    }

    public void validateAndSaveTour(String originalFromLocation, String originalToLocation) throws Exception {
        if (name.getValueSafe().isBlank()) {
            throw new Exception("Name can't be empty");
        }
        if (description.getValueSafe().isBlank()) {
            throw new Exception("Description can't be empty");
        }
        boolean fromModified = !fromLocation.getValueSafe().equals(originalFromLocation);
        boolean toModified = !toLocation.getValueSafe().equals(originalToLocation);
        // Skip suggestion check if editing and fields haven't changed
        if (!editMode.get()) {
            if (fromLocation.getValueSafe().isBlank() || toLocation.getValueSafe().isBlank() ||
                    !fromSuggestions.contains(fromLocation.get()) ||
                    !toSuggestions.contains(toLocation.get())) {
                throw new Exception("From and To must be valid suggestions");
            }
        }
        else {
            if (fromModified) {
                if (fromLocation.getValueSafe().isBlank() || toLocation.getValueSafe().isBlank() ||
                        !fromSuggestions.contains(fromLocation.get())) {
                    throw new Exception("From must be valid suggestions");
                }
            }
            if (toModified) {
                if (fromLocation.getValueSafe().isBlank() || toLocation.getValueSafe().isBlank() ||
                        !toSuggestions.contains(toLocation.get())) {
                    throw new Exception("To must be valid suggestions");
                }
            }
        }
        saveTour();
    }

    public void fetchSuggestions(String query, ListProperty<String> suggestions) {
        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(GEOCODE_URL + encodedQuery))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(this::parseSuggestions)
                    .thenAcceptAsync(s -> {
                        Platform.runLater(() -> suggestions.setAll(FXCollections.observableArrayList(s)));
                    });
        } catch (Exception e) {
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
}