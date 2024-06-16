package com.example.tourplanner;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HelloViewModel {
    private TourManager manager = TourManager.getInstance();
    private TourLogManager logManager = TourLogManager.getInstance();
    public void initialize() {

        };

    public void deleteTour(Tour todelete){
        if (todelete != null) {
            manager.deleteTour(todelete);
        }
    };

    public void deleteLog(TourLogs todelete){
        if (todelete != null) {
            logManager.deleteTourLog(todelete);
        }
    };

    public FileChooser FilePicker(String title){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );
        return fileChooser;
    };

    public void createPDF(File file, Tour selectedTour, List<TourLogs> selectedTourLogs){
        if (file != null) {
            // Call PDFGenerator to create the PDF
            PDFHandler pdfGenerator = new PDFHandler();

            pdfGenerator.createPDF(selectedTour, selectedTourLogs, file.getAbsolutePath());
        }
    }
    public void createSummarizedPDF(File file){
        List<TourLogs> LogList = new ArrayList<>(logManager.getTourLogs());
        List<Tour> TmpTourList = new ArrayList<>(manager.getTours());
        if (file != null) {
            // Call PDFGenerator to create the PDF
            PDFHandler pdfGenerator = new PDFHandler();

            pdfGenerator.createSummarizedPDF(TmpTourList, LogList, file.getAbsolutePath());
        }
    };

    public void PickFileToOpen(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf"));
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            Tour newTour = PDFHandler.readPDF(file.getAbsolutePath());
        }
    }

}
