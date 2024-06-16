package com.example.tourplanner;
import javafx.beans.property.*;
import javafx.scene.control.Alert;
import java.time.LocalDate;
import java.util.function.Consumer;


public class LogViewModel {
    private final TourLogManager logManager = TourLogManager.getInstance();
    private final ObjectProperty<TourLogs> selectedTourLog = new SimpleObjectProperty<>();
    private final ObjectProperty<Tour> selectedTour = new SimpleObjectProperty<>();
    private final BooleanProperty isEdit = new SimpleBooleanProperty();

    // Properties bound to UI elements
    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private final StringProperty time = new SimpleStringProperty();
    private final StringProperty comment = new SimpleStringProperty();
    private final StringProperty difficulty = new SimpleStringProperty();
    private final StringProperty totalDistance = new SimpleStringProperty();
    private final StringProperty totalTime = new SimpleStringProperty();
    private final StringProperty rating = new SimpleStringProperty();

    // Constructor
    public LogViewModel() {
        date.set(LocalDate.now());
        time.set("0");
        comment.set("no comment");
        difficulty.set("3");
        totalDistance.set("0");
        totalTime.set("0");
        rating.set("5");
    }

    // Initialization method
    public void initData(TourLogs log, Tour tour, boolean editMode) {
        selectedTourLog.set(log);
        selectedTour.set(tour);
        isEdit.set(editMode);

        if (editMode && log != null) {
            date.set(log.getDate());
            time.set(String.format("%.0f", log.getTime()));
            comment.set(log.getComment());
            difficulty.set(String.format("%.0f", log.getDifficulty()));
            totalDistance.set(String.valueOf(log.getTotalDistance()));
            totalTime.set(String.format("%.0f", log.getTotalTime()));
            rating.set(String.format("%.0f", log.getRating()));
        }
    }

    // Method to handle form submission
    public void validateAndSaveLog() throws Exception{
        TourLogs log = selectedTourLog.get();
        if (log == null) {
            log = new TourLogs();
            log.setTour(selectedTour.get());
            log.setTourId(selectedTour.get().getId());
        }

        log.setDate(date.get());
        if (!tryParseFloat(time.get(), log::setTime) || log.getTime() > 2359 || log.getTime() % 100 > 59) {
            throw new Exception("Please enter a valid number for time. The format is HHMM.");
        }

        if (!comment.get().matches("^[^\\d]*$")) {
            throw new Exception("Comment must not contain any numbers");
        } else {
            log.setComment(comment.get());
        }

        if (!tryParseFloat(difficulty.get(), log::setDifficulty) || log.getDifficulty() > 10 || log.getDifficulty() < 1) {
            throw new Exception("Difficulty must be a number from 1 to 10");
        }

        if (!tryParseFloat(totalDistance.get(), log::setTotalDistance) || log.getTotalDistance() < 0) {
            throw new Exception("Total Distance must be a positive number");
        }

        if (!tryParseFloat(totalTime.get(), log::setTotalTime) || log.getTotalTime() < 0) {
            throw new Exception("Total Time must be a positive number");
        }

        if (!tryParseFloat(rating.get(), log::setRating) || log.getRating() > 10 || log.getRating() < 1) {
            throw new Exception("Rating must be a number from 1 to 10");
        }

        if (isEdit.get()) {
            logManager.editTourLog(log);
        } else {
            log.setLogId(logManager.getNewLogID());
            logManager.addTourLog(log);
        }

        // Close the window (handled by the View)
    }

    private boolean tryParseFloat(String value, Consumer<Float> consumer) {
        try {
            consumer.accept(Float.parseFloat(value));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Getters for properties (used by the View)
    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public StringProperty timeProperty() {
        return time;
    }

    public StringProperty commentProperty() {
        return comment;
    }

    public StringProperty difficultyProperty() {
        return difficulty;
    }

    public StringProperty totalDistanceProperty() {
        return totalDistance;
    }

    public StringProperty totalTimeProperty() {
        return totalTime;
    }

    public StringProperty ratingProperty() {
        return rating;
    }

    public ObjectProperty<TourLogs> selectedTourLogProperty() {
        return selectedTourLog;
    }

    public BooleanProperty isEditProperty() {
        return isEdit;
    }
}
