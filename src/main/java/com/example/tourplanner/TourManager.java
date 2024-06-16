package com.example.tourplanner;
import com.example.tourplanner.Tour;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class TourManager {

    private static final TourManager instance = new TourManager();

    private ObservableList<Tour> tours = FXCollections.observableArrayList();


    private Tour selectedTour;

    public TourManager() {

        tours.addListener((ListChangeListener.Change<? extends Tour> change) -> {
            BackendService backendService = BackendService.getInstance();
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Tour tour : change.getAddedSubList()) {
                            System.out.println("New Tour Added: " + tour.getName());
                            backendService.AddTourPOST(tour);
                    }
                }
            }
        }
        );
    }

    public static TourManager getInstance() {
        return instance;
    }

    public Tour getSelectedTour() {
        return selectedTour;
    }

    public void setSelectedTour(Tour selectedTour) {
         this.selectedTour = selectedTour;
    }

    public void addTour(Tour tour) {
        tours.add(tour);
    }

    public void editTour(Tour editedTour) {
        for (int i = 0; i < tours.size(); i++) {
            if (tours.get(i).getId().equals(editedTour.getId())) {
                tours.get(i).setName(editedTour.getName());
                tours.get(i).setDescription(editedTour.getDescription());
                tours.get(i).setFromLocation(editedTour.getFromLocation());
                tours.get(i).setToLocation(editedTour.getToLocation());
                tours.get(i).setTransportType(editedTour.getTransportType());
                tours.get(i).setDistance(editedTour.getDistance());
                tours.get(i).setEstimatedTime(editedTour.getEstimatedTime());
                System.out.println("Tour Edited: " + editedTour.getName());
                //backend management
                BackendService backendService = BackendService.getInstance();
                backendService.EditTourPUT(editedTour);
                return; // Exit the method once the replacement is done
            }
        }
    }

    public void deleteTour(Tour tourToDelete) {
        tours.remove(tourToDelete);
        BackendService backendService = BackendService.getInstance();
        backendService.DeleteTourDELETE(tourToDelete);
    }

    public String getNewID(){
        String id;
        boolean isUnique;
        do {
            id = UUID.randomUUID().toString();
            isUnique = true;
            for (Tour tour : tours) {
                if (tour.getId().equals(id)) {
                    isUnique = false;
                    break;
                }
            }
        } while (!isUnique);
        return id;
    }

    public ObservableList<Tour> getTours() {
        return tours;
    }

    public Tour getById(String id) {
        for (Tour tour : tours) {
            if (tour.getId().equals(id)) {
                return tour;
            }
        }
        return null; // If no tour with matching ID is found
    }
}
