package com.example.tourplanner;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;
import java.util.stream.Collectors;

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
    private TextField tourImageField;
    @FXML
    private TextField tourLogDescriptionField;
    @FXML
    private TextField tourLogDateField;
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
        for (Tour tour : tourList.getItems()) {
            System.out.println(tour.getName());
        }

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
        selectedTour.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                List<TourLog> filteredTourLogs = logManager.getTourLogs().stream()
                        .filter(log -> log.getTour().equals(newValue))
                        .collect(Collectors.toList());
                tourLogs = FXCollections.observableArrayList(filteredTourLogs);
                tourLogTable.setItems(tourLogs);
            }
        });
    }

    public static HelloController getInstance() {
        return instance;
    }

    @FXML
    public void addToList(Tour tour){
        if(tourList!=null)
            tourList.getItems().add(tour);
    }

    @FXML
    public void updateList(){
        if(tourList!=null)
            tourList.setItems(manager.getTours());
    }

    @FXML
    public void addToLogList(TourLog log){
        if(tourLogs!=null) {
            tourLogs.add(log);
            tourLogTable.setItems(tourLogs);
        }
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
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddLogPopup.fxml"));
            Parent root1 = fxmlLoader.load();
            LogPopupController controller = fxmlLoader.getController();
            controller.initData(tourLogTable.getSelectionModel().getSelectedItem(), tourList.getSelectionModel().getSelectedItem(), false);
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        }
        catch (Exception e){
            System.out.println("Cant load new window");
        }
        System.out.println(selectedTour.getValue().getChildFriendliness());
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
        }
        catch (Exception e){
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
}