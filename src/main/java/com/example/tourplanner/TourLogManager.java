package com.example.tourplanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class TourLogManager {

    private static final TourLogManager instance = new TourLogManager();

    private ObservableList<TourLogs> tourLogs = FXCollections.observableArrayList();

    private TourLogs selectedTourLog;

    public TourLogManager() {
        tourLogs.addListener((ListChangeListener.Change<? extends TourLogs> change) -> {
            BackendService backendService = new BackendService();
            while (change.next()) {
                if (change.wasAdded()) {
                    for (TourLogs log: change.getAddedSubList()) {
                        backendService.AddTourLogPOST(log);
                    }
                }
                if (change.wasRemoved()) {
                    for (TourLogs log: change.getAddedSubList()) {
                        backendService.DeleteTourLogDELETE(log);
                    }
                }
            }
        });
    }

    public static TourLogManager getInstance() {
        return instance;
    }

    public TourLogs getSelectedTourLog() {
        return selectedTourLog;
    }

    public void setSelectedTourLog(TourLogs selectedTourLog) {
        this.selectedTourLog = selectedTourLog;
    }

    public void addTourLog(TourLogs tourLog) {
        tourLogs.add(tourLog);
    }

    public void editTourLog(TourLogs editedTourLog) {
        for (int i = 0; i < tourLogs.size(); i++) {
            if (tourLogs.get(i).getId().equals(editedTourLog.getId())) {
                tourLogs.get(i).setDate(editedTourLog.getDate());
                tourLogs.get(i).setTime(editedTourLog.getTime());
                tourLogs.get(i).setComment(editedTourLog.getComment());
                tourLogs.get(i).setDifficulty(editedTourLog.getDifficulty());
                tourLogs.get(i).setTotalDistance(editedTourLog.getTotalDistance());
                tourLogs.get(i).setTotalTime(editedTourLog.getTotalTime());
                tourLogs.get(i).setRating(editedTourLog.getRating());
                System.out.println("TourLog Edited: " + editedTourLog.getId());
                BackendService backendService = new BackendService();
                backendService.EditTourLogPUT(editedTourLog);
                return; // Exit the method once the replacement is done
            }
        }
    }

    public void deleteTourLog(TourLogs tourLogToDelete) {
        tourLogs.remove(tourLogToDelete);
    }

    public String getNewLogID(){
        String id;
        boolean isUnique;
        do {
            id = UUID.randomUUID().toString();
            isUnique = true;
            for (TourLogs log : tourLogs) {
                if (log.getId().equals(id)) {
                    isUnique = false;
                    break;
                }
            }
        } while (!isUnique);
        return id;
    }

    public ObservableList<TourLogs> getTourLogs() {
        return tourLogs;
    }
}
