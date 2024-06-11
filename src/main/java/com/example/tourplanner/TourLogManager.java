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

    private ObservableList<TourLog> tourLogs = FXCollections.observableArrayList();

    private TourLog selectedTourLog;

    public TourLogManager() {
        tourLogs.addListener((ListChangeListener.Change<? extends TourLog> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (TourLog log: change.getAddedSubList()) {
                        HelloController hello = HelloController.getInstance();
                        //add to view
                        //hello.addToLogList(log);

                        //write new json for CRUD
                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.registerModule(new JavaTimeModule());
                        try {
                            // Write to a JSON file
                            objectMapper.writeValue(new File("src/main/resources/newTourLogTmp.json"), log);
                            System.out.println("JSON file updated: newTourLogTmp.json");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //TO DO:
                        //some restcall back to server to add this to the database, containing the Json
                    }
                }
                if (change.wasRemoved()) {
                    HelloController hello = HelloController.getInstance();
                    //hello.updateLogList();
                    //TO DO:
                    //some restcall back to server to add this to the database, containing the TourID
                }
            }
        });
    }

    public static TourLogManager getInstance() {
        return instance;
    }

    public TourLog getSelectedTourLog() {
        return selectedTourLog;
    }

    public void setSelectedTourLog(TourLog selectedTourLog) {
        this.selectedTourLog = selectedTourLog;
    }

    public void addTourLog(TourLog tourLog) {
        tourLogs.add(tourLog);
    }

    public void editTourLog(TourLog editedTourLog) {
        for (int i = 0; i < tourLogs.size(); i++) {
            if (tourLogs.get(i).getLogId().equals(editedTourLog.getLogId())) {
                tourLogs.get(i).setDate(editedTourLog.getDate());
                tourLogs.get(i).setTime(editedTourLog.getTime());
                tourLogs.get(i).setComment(editedTourLog.getComment());
                tourLogs.get(i).setDifficulty(editedTourLog.getDifficulty());
                tourLogs.get(i).setTotalDistance(editedTourLog.getTotalDistance());
                tourLogs.get(i).setTotalTime(editedTourLog.getTotalTime());
                tourLogs.get(i).setRating(editedTourLog.getRating());
                HelloController hello = HelloController.getInstance();
                System.out.println("TourLog Edited: " + editedTourLog.getLogId());
                //hello.updateLogList();
                //write new json for CRUD
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                try {
                    // Write to a JSON file
                    objectMapper.writeValue(new File("src/main/resources/EditedTourLogTmp.json"), editedTourLog);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //TO DO:
                //some restcall back to server to add this to the database, containing the Json
                return; // Exit the method once the replacement is done
            }
        }
    }

    public void deleteTourLog(TourLog tourLogToDelete) {
        tourLogs.remove(tourLogToDelete);
    }

    public String getNewLogID(){
        String id;
        boolean isUnique;
        do {
            id = UUID.randomUUID().toString();
            isUnique = true;
            for (TourLog log : tourLogs) {
                if (log.getLogId().equals(id)) {
                    isUnique = false;
                    break;
                }
            }
        } while (!isUnique);
        return id;
    }

    public ObservableList<TourLog> getTourLogs() {
        return tourLogs;
    }
}
