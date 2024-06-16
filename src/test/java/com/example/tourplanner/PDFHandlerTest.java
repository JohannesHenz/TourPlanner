package com.example.tourplanner;

import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PDFHandlerTest {

    private PDFHandler pdfHandler;
    private static final String DEST = "test_output2.pdf";

    @BeforeEach
    public void setUp() {
        pdfHandler = new PDFHandler();
    }

    @Test
    public void testCreatePDF() throws IOException {
        // Prepare test data
        Tour tour = new Tour("Test Tour", "Description");
        tour.setFromLocation("A");
        tour.setToLocation("B");
        tour.setTransportType("Car");
        tour.setDistance(100.0f);
        tour.setEstimatedTime(120.0f);


        List<TourLogs> tourLogs = new ArrayList<>();
        TourLogs tourLog = new TourLogs();
        tourLog.setTourId(tour.getId());
        tourLog.setLogId("your_log_id_value_here");
        tourLog.setDate(LocalDate.now());
        tourLog.setTotalDistance(0); // Example distance value, adjust as per your requirements
        tourLog.setTotalTime(0); // Example duration value, adjust as per your requirements
        tourLog.setRating(0); // Example rating value, adjust as per your requirements
        tourLog.setComment("a"); // Example average speed value, adjust as per your requirements
        tourLog.setDifficulty(0); // Example max speed value, adjust as per your requirements
        tourLogs.add(tourLog);

        // Create PDF
        pdfHandler.createPDF(tour, tourLogs, DEST);

        // Verify PDF file creation
        File file = new File(DEST);
        assertTrue(file.exists(), "PDF file should be created");

        // Clean up
        file.delete();
    }

    @Test
    public void testCreateSummarizedPDF() throws IOException {
        // Prepare test data
        Tour tour = new Tour("Test Tour", "Description");
        tour.setId("1");
        List<Tour> tourList = List.of(tour);

        TourLogs log = new TourLogs();

        log.setTourId("1");
        List<TourLogs> tourLogs = List.of(log);

        // Create summarized PDF
        pdfHandler.createSummarizedPDF(tourList, tourLogs, DEST);

        // Verify PDF file creation
        File file = new File(DEST);
        assertTrue(file.exists(), "PDF file should be created");

        // Clean up
        file.delete();
    }

    @Test
    public void testReadPDF() throws IOException {
        // Prepare test data
        Tour tour = new Tour("Test Tour", "Description");
        tour.setFromLocation("A");
        tour.setToLocation("B");
        tour.setTransportType("Car");
        tour.setDistance(100.0f);
        tour.setEstimatedTime(120.0f);

        List<TourLogs> tourLogs = new ArrayList<>();

        TourLogs tourLog = new TourLogs();
        tourLog.setTourId(tour.getId());
        tourLog.setLogId("your_log_id_value_here");
        tourLog.setDate(LocalDate.now());
        tourLog.setTotalDistance(0); // Example distance value, adjust as per your requirements
        tourLog.setTotalTime(0); // Example duration value, adjust as per your requirements
        tourLog.setRating(0); // Example rating value, adjust as per your requirements
        tourLog.setComment("a"); // Example average speed value, adjust as per your requirements
        tourLog.setDifficulty(0); // Example max speed value, adjust as per your requirements
        tourLogs.add(tourLog);

        // Define the destination path for the PDF
        String DEST = "test_output3.pdf";

        try {
            // Create PDF for reading
            pdfHandler.createPDF(tour, tourLogs, DEST);

            // Check if the PDF was successfully created
            File createdPdf = new File(DEST);
            assertTrue(createdPdf.exists(), "PDF file should be created");

            // Read PDF
            Tour readTour = PDFHandler.readPDF(DEST);

            // Verify the tour details
            assertNotNull(readTour, "Read tour should not be null");
            assertEquals(tour.getName(), readTour.getName(), "Tour name should match");
            assertEquals(tour.getDescription(), readTour.getDescription(), "Tour description should match");
        } finally {
            // Clean up
            File file = new File(DEST);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}