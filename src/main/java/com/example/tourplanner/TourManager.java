package com.example.tourplanner;
import com.example.tourplanner.Tour;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TourManager {
    private ObservableList<Tour> tours = FXCollections.observableArrayList();

    private Tour selectedTour;

    public TourManager(ObservableList<Tour> tours) {
        // Initialize the tours list
        this.tours = tours;
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

    public void editTour(int index, Tour editedTour) {
        tours.set(index, editedTour);
    }

    public void deleteTour(Tour tour) {
        tours.remove(tour);
    }

    public ObservableList<Tour> getTours() {
        return tours;
    }
}
