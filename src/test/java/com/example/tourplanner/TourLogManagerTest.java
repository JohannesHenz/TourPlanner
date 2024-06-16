package com.example.tourplanner;

import com.example.tourplanner.TourLogManager;
import com.example.tourplanner.TourLogs;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TourLogManagerTest {

    private TourLogManager tourLogManager;

    @BeforeEach
    public void setUp() {
        tourLogManager = TourLogManager.getInstance();
        // Clear any existing tour logs before each test
        tourLogManager.getTourLogs().clear();
    }

    @Test
    public void testAddTourLog() {
        // Create a new tour log
        TourLogs tourLog = createTourLog();

        // Add the tour log using TourLogManager
        tourLogManager.addTourLog(tourLog);

        // Check if the tour log was added to the list
        ObservableList<TourLogs> tourLogs = tourLogManager.getTourLogs();
        assertTrue(tourLogs.contains(tourLog), "Tour log should be added to the tour log list");

        // Optional: Check if the backend service interaction occurred (e.g., mock BackendService)
        // Add assertions here if you have a mock BackendService implementation
    }

    @Test
    public void testEditTourLog() {
        // Add a tour log first
        TourLogs originalTourLog = createTourLog();
        tourLogManager.addTourLog(originalTourLog);

        // Modify the tour log
        originalTourLog.setComment("Updated comment");
        tourLogManager.editTourLog(originalTourLog);

        // Check if the tour log was updated correctly
        ObservableList<TourLogs> tourLogs = tourLogManager.getTourLogs();
        TourLogs editedTourLog = tourLogs.filtered(t -> t.getLogId().equals(originalTourLog.getLogId())).get(0);
        assertEquals("Updated comment", editedTourLog.getComment(), "Tour log comment should be updated");

        // Optional: Check if the backend service interaction for editing occurred
        // Add assertions here if you have a mock BackendService implementation
    }

    @Test
    public void testDeleteTourLog() {
        // Add a tour log first
        TourLogs tourLogToDelete = createTourLog();
        tourLogManager.addTourLog(tourLogToDelete);

        // Delete the tour log
        tourLogManager.deleteTourLog(tourLogToDelete);

        // Check if the tour log was removed from the list
        ObservableList<TourLogs> tourLogs = tourLogManager.getTourLogs();
        assertFalse(tourLogs.contains(tourLogToDelete), "Tour log should be removed from the tour log list");

        // Optional: Check if the backend service interaction for deletion occurred
        // Add assertions here if you have a mock BackendService implementation
    }

    @Test
    public void testGetById() {
        // Add a tour log first
        TourLogs tourLog = createTourLog();
        tourLogManager.addTourLog(tourLog);

        // Retrieve the tour log by ID
        TourLogs retrievedTourLog = tourLogManager.getById(tourLog.getLogId());

        // Check if the retrieved tour log matches the original
        assertNotNull(retrievedTourLog, "Retrieved tour log should not be null");
        assertEquals(tourLog.getLogId(), retrievedTourLog.getLogId(), "Tour log ID should match");

        // Optional: Add more assertions to check other properties if needed
    }

    @Test
    public void testGetNewLogID() {
        // Generate a new log ID
        String newLogId = tourLogManager.getNewLogID();

        // Ensure the generated ID is not null or empty
        assertNotNull(newLogId, "New log ID should not be null");
        assertFalse(newLogId.isEmpty(), "New log ID should not be empty");

        // Optional: Check if the generated ID is unique, depending on your implementation
    }

    private TourLogs createTourLog() {
        TourLogs tourLog = new TourLogs();
        tourLog.setLogId(UUID.randomUUID().toString());
        tourLog.setDate(LocalDate.now());
        tourLog.setTime(60); // Example time value
        tourLog.setComment("Test comment");
        tourLog.setDifficulty(2); // Example difficulty value
        tourLog.setTotalDistance(50); // Example distance value
        tourLog.setTotalTime(60); // Example duration value
        tourLog.setRating(4); // Example rating value

        return tourLog;
    }
}
