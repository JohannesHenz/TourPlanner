package com.example.tourplanner;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import javafx.stage.FileChooser;
import java.time.LocalDate;

public class HelloController {
    private static final HelloController instance = new HelloController();
    //die offizielle tourliste, hier hängen (später trigger dran für adden removen etc)
    private TourManager manager = TourManager.getInstance();
    //die offizielle tourLoglist
    private TourLogManager logManager = TourLogManager.getInstance();
    //die tourlist für den view
    @FXML
    private ListView<Tour> tourList;
    @FXML
    private ObservableList<TourLog> tourLogs;
    @FXML
    private ObjectProperty<Tour> selectedTour = new SimpleObjectProperty<>();
    @FXML
    private TourLog SelectedTourLog;
    @FXML
    private TableView<TourLog> tourLogTable;
    @FXML
    private TableColumn<TourLog, LocalDate> dateColumn;
    @FXML
    private TableColumn<TourLog, Float> timeColumn;
    @FXML
    private TableColumn<TourLog, String> commentColumn;
    @FXML
    private TableColumn<TourLog, Float> difficultyColumn;
    @FXML
    private TableColumn<TourLog, Float> totalDistanceColumn;
    @FXML
    private TableColumn<TourLog, Float> totalTimeColumn;
    @FXML
    private TableColumn<TourLog, Float> ratingColumn;
    @FXML
    private TextArea tourInfo;
    @FXML
    private TextField TourSearch;
    @FXML
    private TextField LogSearch;
    @FXML
    private MenuItem OpenPDF;
    @FXML
    private MenuItem SavePDF;

    public Tour getSelectedTour(){
        return selectedTour.get();
    }
    public void setSelectedTour(Tour tour) {
        selectedTour.set(tour);
    }
    public ObjectProperty<Tour> selectedTourProperty() {
        return selectedTour;
    }

    public TourLog getSelectedTourLog(){
        return SelectedTourLog;
    }

    public void initialize() { //hier setzen wir die internen tours auf die des managers. die logs holen wir on demand später
            // Set items for tourList and tourLogList
            tourList.setItems(manager.getTours());
            /*List<TourLog> filteredTourLogs = logManager.getTourLogs().stream()
                .filter(log -> log.getTour().equals(SelectedTour))
                .collect(Collectors.toList());

            tourLogs = FXCollections.observableArrayList(filteredTourLogs);*/
            tourLogs = logManager.getTourLogs();

        if(!(dateColumn==null || timeColumn==null ||commentColumn==null ||difficultyColumn==null ||totalDistanceColumn==null ||totalTimeColumn==null ||ratingColumn==null)) {
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
            timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
            commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
            difficultyColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
            totalDistanceColumn.setCellValueFactory(new PropertyValueFactory<>("totalDistance"));
            totalTimeColumn.setCellValueFactory(new PropertyValueFactory<>("totalTime"));
            ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        }
        tourList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setSelectedTour(newValue);
                updateLogList();
            }
        });

        // Check if tourList has items and set the first item as selected
        if (!tourList.getItems().isEmpty()) {
            selectedTour.setValue(tourList.getItems().get(0));
        }

        // Update the tour info and log list for the initially selected tour
        selectedTour.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                List<TourLog> filteredTourLogs = logManager.getTourLogs().stream()
                        .filter(log -> log.getTour().equals(newValue))
                        .collect(Collectors.toList());
                tourLogs = FXCollections.observableArrayList(filteredTourLogs);
                tourLogTable.setItems(tourLogs);
                tourInfo.setText(getTourInfo());
            }
        });

        // Initialize tourInfo with the first tour's info if available
        if (selectedTour.getValue() != null) {
            tourInfo.setText(getTourInfo());
        }
        // Add a listener to the TourSearch text field to filter the tour list
        TourSearch.textProperty().addListener((observable, oldValue, newValue) -> filterTourList());
        LogSearch.textProperty().addListener((observable, oldValue, newValue) -> filterTourLogList());
    }

    public static HelloController getInstance() {
        return instance;
    }


    @FXML
    public void updateList(){
        if(tourList!=null)
            tourList.setItems(manager.getTours());
    }

    @FXML
    public void updateLogList(){
        if (selectedTour.get() != null) {
            List<TourLog> filteredTourLogs = logManager.getTourLogs().stream()
                    .filter(log -> log.getTour().equals(selectedTour.get()))
                    .collect(Collectors.toList());
            tourLogs = FXCollections.observableArrayList(filteredTourLogs);
            tourLogTable.setItems(tourLogs);
        }
    }

    @FXML
    private void addTour() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddPopup.fxml"));
            Parent root1 = fxmlLoader.load();
            PopupController controller = fxmlLoader.getController();
            controller.initData(tourList.getSelectionModel().getSelectedItem(), false);
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        }
        catch (Exception e){
            System.out.println("Cant load new window");
        }
    }

    @FXML
    private void editTour() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddPopup.fxml"));
            Parent root1 = fxmlLoader.load();
            PopupController controller = fxmlLoader.getController();
            controller.initData(tourList.getSelectionModel().getSelectedItem(), true);
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        }
        catch (Exception e){
            System.out.println("Cant load new window");
        }
    }

    @FXML
    private void deleteTour() {
        // Implement deleting a tour functionality here
        // You can get the selected item from the list view and remove it
        Tour selectedTour = tourList.getSelectionModel().getSelectedItem();
        if (selectedTour != null) {
            manager.deleteTour(selectedTour);
        }
    }


    @FXML
    protected void onAddTourLogButtonClick() {
        if(tourList.getSelectionModel().getSelectedItem()!=null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddLogPopup.fxml"));
                Parent root1 = fxmlLoader.load();
                LogPopupController controller = fxmlLoader.getController();
                controller.initData(tourLogTable.getSelectionModel().getSelectedItem(), tourList.getSelectionModel().getSelectedItem(), false);
                Stage stage = new Stage();
                stage.setScene(new Scene(root1));
                stage.show();
            } catch (Exception e) {
                System.out.println("Cant load new window");
            }
            System.out.println(selectedTour.getValue().getChildFriendliness());
        }
    }

    @FXML
    protected void onEditTourLogButtonClick() {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddLogPopup.fxml"));
                Parent root1 = fxmlLoader.load();
                LogPopupController controller = fxmlLoader.getController();
                controller.initData(tourLogTable.getSelectionModel().getSelectedItem(), tourList.getSelectionModel().getSelectedItem(), true);
                Stage stage = new Stage();
                stage.setScene(new Scene(root1));
                stage.show();
            } catch (Exception e) {
                System.out.println("Cant load new window");
            }
    }

    @FXML
    protected void onDeleteTourLogButtonClick() {
        TourLog selectedTourLog = tourLogTable.getSelectionModel().getSelectedItem();
        if (selectedTourLog != null) {
            //tourLogTable.getItems().remove(selectedTourLog);
            logManager.deleteTourLog(selectedTourLog);
        }
    }
    @FXML
    protected String getTourInfo(){
        return "Name:\t\t\t\t"+selectedTour.getValue().getName()+"\nDescription:\t\t\t"+selectedTour.getValue().getDescription()+"\nFrom:\t\t\t\t"+selectedTour.getValue().getFrom()+"\nTo:\t\t\t\t\t"+selectedTour.getValue().getTo()+"\nTransport Type:\t\t"+selectedTour.getValue().getTransportType()+"\nDistance:\t\t\t\t"+String.format("%.1f",selectedTour.getValue().getDistance())+"\nEstimated Time:\t\t"+String.format("%.0f",selectedTour.getValue().getEstimatedTime())+"\nPopularity: \t\t\t"+String.format("%.0f",selectedTour.getValue().getPopularity())+"\t\t\t\t\t(search for with ie pop>1)\nChild Friendliness: \t\t"+String.format("%.1f",selectedTour.getValue().getChildFriendliness())+"\t\t\t\t\t(search for with ie cf>1)";

    }
    @FXML
    public void filterTourList() {
        String searchText = TourSearch.getText();
        if (searchText == null || searchText.isEmpty()) {
            // If the search text is empty, display all tours
            tourList.setItems(manager.getTours());
        } else {
            // Check if the search text follows the pattern "cf<5" or similar
            if (searchText.matches("cf[<>=]\\d+(\\.\\d+)?") || searchText.matches("pop[<>=]\\d+(\\.\\d+)?")) {
                // Parse the search text to extract the operator and threshold value
                String[] tokens = searchText.split("[<=>]+");
                String operator = searchText.replaceAll("[^<=>]", "");
                float threshold = Float.parseFloat(tokens[1]);
                String attribute = tokens[0];

                // Filter the tours based on the parsed expression
                List<Tour> filteredTours = manager.getTours().stream()
                        .filter(tour -> {
                            if (attribute.equals("cf")) {
                                switch (operator) {
                                    case "<":
                                        return tour.getChildFriendliness() < threshold;
                                    case "<=":
                                        return tour.getChildFriendliness() <= threshold;
                                    case ">":
                                        return tour.getChildFriendliness() > threshold;
                                    case ">=":
                                        return tour.getChildFriendliness() >= threshold;
                                    default:
                                        return false; // Invalid operator
                                }
                            } else if (attribute.equals("pop")) {
                                switch (operator) {
                                    case "<":
                                        return tour.getPopularity() < threshold;
                                    case "<=":
                                        return tour.getPopularity() <= threshold;
                                    case ">":
                                        return tour.getPopularity() > threshold;
                                    case ">=":
                                        return tour.getPopularity() >= threshold;
                                    default:
                                        return false; // Invalid operator
                                }
                            }
                            return false; // Default return, in case neither "cf" nor "pop"
                        })
                        .collect(Collectors.toList());
                tourList.setItems(FXCollections.observableArrayList(filteredTours));
            } else {
                // If the search text doesn't follow the pattern "cf<5" or similar,
                // revert to the default filtering behavior
                List<Tour> filteredTours = manager.getTours().stream()
                        .filter(tour -> tour.getName().toLowerCase().contains(searchText.toLowerCase()))
                        .collect(Collectors.toList());
                tourList.setItems(FXCollections.observableArrayList(filteredTours));
            }
        }
    }
    @FXML
    public void filterTourLogList() {
        String searchText = LogSearch.getText();
        if (searchText == null || searchText.isEmpty()) {
            // If the search text is empty, display all tour logs for the selected tour
            List<TourLog> filteredTourLogs = logManager.getTourLogs().stream()
                    .filter(log -> log.getTour().equals(selectedTour.getValue()))
                    .collect(Collectors.toList());
            tourLogs = FXCollections.observableArrayList(filteredTourLogs);
            tourLogTable.setItems(tourLogs);
        } else {
            // Filter the tour logs based on the search text substring in comments
            List<TourLog> filteredTourLogs = logManager.getTourLogs().stream()
                    .filter(log -> log.getTour().equals(selectedTour.getValue()) &&
                            log.getComment().toLowerCase().contains(searchText.toLowerCase()))
                    .collect(Collectors.toList());
            tourLogs = FXCollections.observableArrayList(filteredTourLogs);
            tourLogTable.setItems(tourLogs);
        }
    }

    @FXML
    protected void onSavePDFButtonClick() {
        // Check if a tour is selected
        Tour selectedTour = getSelectedTour();
        if (selectedTour == null) {
            return;
        }
        // Get corresponding tour logs for the selected tour
        List<TourLog> selectedTourLogs = tourLogs.stream()
                .filter(log -> log.getTour().equals(selectedTour))
                .collect(Collectors.toList());

        // Open a file chooser to save the PDF
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );

        File file = fileChooser.showSaveDialog(tourList.getScene().getWindow());
        if (file != null) {
            // Call PDFGenerator to create the PDF
            PDFHandler pdfGenerator = new PDFHandler();
            pdfGenerator.createPDF(selectedTour, selectedTourLogs, file.getAbsolutePath());
        }
    };
    @FXML
    protected void onOpenPDFButtonClick(){

    };
}