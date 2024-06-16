package com.example.tourplanner;

import com.example.tourplanner.Tour;
import com.example.tourplanner.TourManager;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TourManagerTest {

    private TourManager tourManager;

    @BeforeEach
    public void setUp() {
        tourManager = TourManager.getInstance();
        // Clear any existing tours before each test
        tourManager.getTours().clear();
    }

    @Test
    public void testAddTour() {
        // Create a new tour
        Tour tour = new Tour();
        tour.setId("1");
        tour.setName("Test Tour");
        tour.setDescription("Description");
        tour.setFromLocation("Location A");
        tour.setToLocation("Location B");
        tour.setTransportType("Car");
        tour.setDistance(10000);
        tour.setEstimatedTime(3600);

        // Add the tour using TourManager
        tourManager.addTour(tour);

        // Check if the tour was added to the list
        ObservableList<Tour> tours = tourManager.getTours();
        assertTrue(tours.contains(tour), "Tour should be added to the tour list");

        // Optional: Check if the backend service interaction occurred (e.g., mock BackendService)
        // Add assertions here if you have a mock BackendService implementation
    }

    @Test
    public void testEditTour() {
        // Add a tour first
        Tour originalTour = new Tour();
        originalTour.setId("1");
        originalTour.setName("Test Tour");
        originalTour.setDescription("Description");
        originalTour.setFromLocation("Location A");
        originalTour.setToLocation("Location B");
        originalTour.setTransportType("Car");
        originalTour.setDistance(10000);
        originalTour.setEstimatedTime(3600);
        tourManager.addTour(originalTour);

        // Modify the tour
        originalTour.setName("Updated Test Tour");
        tourManager.editTour(originalTour);

        // Check if the tour was updated correctly
        ObservableList<Tour> tours = tourManager.getTours();
        Tour editedTour = tours.filtered(t -> t.getId().equals(originalTour.getId())).get(0);
        assertEquals("Updated Test Tour", editedTour.getName(), "Tour name should be updated");

        // Optional: Check if the backend service interaction for editing occurred
        // Add assertions here if you have a mock BackendService implementation
    }

    @Test
    public void testDeleteTour() {
        // Add a tour first
        Tour tourToDelete = new Tour();
        tourToDelete.setId("1");
        tourToDelete.setName("Test Tour");
        tourToDelete.setDescription("Description");
        tourToDelete.setFromLocation("Location A");
        tourToDelete.setToLocation("Location B");
        tourToDelete.setTransportType("Car");
        tourToDelete.setDistance(10000);
        tourToDelete.setEstimatedTime(3600);
        tourManager.addTour(tourToDelete);

        // Delete the tour
        tourManager.deleteTour(tourToDelete);

        // Check if the tour was removed from the list
        ObservableList<Tour> tours = tourManager.getTours();
        assertFalse(tours.contains(tourToDelete), "Tour should be removed from the tour list");

        // Optional: Check if the backend service interaction for deletion occurred
        // Add assertions here if you have a mock BackendService implementation
    }

    // Additional tests for edge cases, error conditions, etc., can be added as needed
}
