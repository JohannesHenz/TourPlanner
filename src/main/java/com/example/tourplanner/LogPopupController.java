package com.example.tourplanner;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import java.time.LocalDate;
import javafx.scene.control.DatePicker;
import java.util.function.Consumer;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class LogPopupController {
        private TourLogManager logManager = TourLogManager.getInstance();
        private TourManager manager = TourManager.getInstance();
        @FXML
        private DatePicker DateField;
        @FXML
        private TextField TimeField;
        @FXML
        private TextField CommentField;
        @FXML
        private TextField difficultyField;
        @FXML
        private TextField totalDistanceField;
        @FXML
        private TextField totalTimeField;
        @FXML
        private TextField RatingField;
        @FXML
        private Button NewTourLogSubmit;
        @FXML
        private TourLog SelectedTourLog;
        @FXML
        private Tour SelectedTour;
        @FXML
        private boolean isEdit;
        @FXML
        private Label focusLabel;
        @FXML
        private Label header;

        public void initData(TourLog log, Tour tour, boolean isEdit){
            //damit der setText geht, weil sonst regt heißt cant open window
            focusLabel.requestFocus();
            this.SelectedTourLog = log;
            this.SelectedTour = tour;
            this.isEdit = isEdit;
            if(this.isEdit){
                DateField.setValue(SelectedTourLog.getDate());
                TimeField.setText(String.format("%.0f",SelectedTourLog.getTime()));
                CommentField.setText(SelectedTourLog.getComment());
                difficultyField.setText(String.format("%.0f",SelectedTourLog.getDifficulty()));
                totalDistanceField.setText(String.valueOf(SelectedTourLog.getTotalDistance()));
                totalTimeField.setText(String.format("%.0f",SelectedTourLog.getTotalTime()));
                RatingField.setText(String.format("%.0f",SelectedTourLog.getRating()));
                header.setText("Edit a Tour Log");
            }
            else{
                DateField.setValue(LocalDate.now());
                TimeField.setText("0");
                CommentField.setText("no comment");
                difficultyField.setText("3");
                totalDistanceField.setText("0");
                totalTimeField.setText("0");
                RatingField.setText("5");
            }
        }

        // Method to handle the action when the confirm button is clicked
        @FXML
        private void handleSubmit() {
            if(!isEdit) {
                // Retrieve information from text fields
                LocalDate date = DateField.getValue();
                String time = TimeField.getText();
                String comment = CommentField.getText();
                String difficulty = difficultyField.getText();
                String totalDistance = totalDistanceField.getText();
                String totalTime = totalTimeField.getText();
                String rating = RatingField.getText();
                // Create a new instance of Tour with the retrieved information
                Tour selectedTour = this.SelectedTour;
                this.SelectedTourLog = new TourLog(); // Assuming Tour has a constructor that takes name and description
                //hier noch handling falls was leer is
                SelectedTourLog.setDate(date);
                if (!tryParseFloat(time, SelectedTourLog::setTime)) {
                    showError("Invalid input", "Please enter a valid number for time.  The format is HHMM.");
                    return;
                }
                else{
                    if(SelectedTourLog.getTime()>2359 || SelectedTourLog.getTime()%100>59){
                        showError("Invalid input", "Please enter a valid number for time. The format is HHMM.");
                        return;
                    }
                }
                if (!comment.matches("^[^\\d]*$")) { //keine zahlen in comments, weil sonst könnt pdf lesen sehr schief gehn
                    showError("Invalid input", "Comment must not contain any numbers");
                    return; // or handle the error as needed
                } else {
                    SelectedTourLog.setComment(comment);
                }
                if (!tryParseFloat(difficulty, SelectedTourLog::setDifficulty)) {
                    showError("Invalid input", "Difficulty must be a number from 1 to 10");
                    return; // or handle the error as needed
                }
                else{
                    if(SelectedTourLog.getDifficulty()>10 || SelectedTourLog.getDifficulty()<1){
                        showError("Invalid input", "1Difficulty must be a number from 1 to 10");
                        return;
                    }
                }
                if (!tryParseFloat(totalDistance, SelectedTourLog::setTotalDistance)) {
                    showError("Invalid input", "Total Distance must be a positive number");
                    return; // or handle the error as needed
                }
                else{
                    if(SelectedTourLog.getTotalDistance()<0){
                        showError("Invalid input", "Total Distance must be a positive number");
                        return;
                    }
                }
                if (!tryParseFloat(totalTime, SelectedTourLog::setTotalTime)) {
                    showError("Invalid input", "Total Time must be a positive number");
                    return; // or handle the error as needed
                }
                else{
                    if(SelectedTourLog.getTotalTime()<0){
                        showError("Invalid input", "Total Time must be a positive number");
                        return;
                    }
                }
                if (!tryParseFloat(rating, SelectedTourLog::setRating)) {
                    showError("Invalid input", "Rating must be a number from 1 to 10");
                    return; // or handle the error as needed
                }
                else{
                    if(SelectedTourLog.getRating()>10 || SelectedTourLog.getRating()<1){
                        showError("Invalid input", "Rating must be a number from 1 to 10");
                        return;
                    }
                }
                SelectedTourLog.setLogId(TourLogManager.getInstance().getNewLogID());
                SelectedTourLog.setTour(selectedTour);
                SelectedTourLog.setTourId(selectedTour.getId());

                logManager.addTourLog(this.SelectedTourLog);
                // Close the window
                Stage stage = (Stage) NewTourLogSubmit.getScene().getWindow();
                stage.close();
            }
            else{
                // Retrieve information from text fields
                LocalDate date = DateField.getValue();
                String time = TimeField.getText();
                String comment = CommentField.getText();
                String difficulty = difficultyField.getText();
                String totalDistance = totalDistanceField.getText();
                String totalTime = totalTimeField.getText();
                String rating = RatingField.getText();
                //put it into selected tourlog
                SelectedTourLog.setDate(date);
                if (!tryParseFloat(time, SelectedTourLog::setTime)) {
                    showError("Invalid input", "Please enter a valid number for time.  The format is HHMM.");
                    return;
                }
                else{
                    if(SelectedTourLog.getTime()>2359 || SelectedTourLog.getTime()%100>59){
                        showError("Invalid input", "Please enter a valid number for time. The format is HHMM.");
                        return;
                    }
                }
                if (!comment.matches("^[^\\d]*$")) { //keine zahlen in comments, weil sonst könnt pdf lesen sehr schief gehn
                    showError("Invalid input", "Comment must not contain any numbers");
                    return; // or handle the error as needed
                } else {
                    SelectedTourLog.setComment(comment);
                }
                if (!tryParseFloat(difficulty, SelectedTourLog::setDifficulty)) {
                    showError("Invalid input", "Difficulty must be a number from 1 to 10");
                    return; // or handle the error as needed
                }
                else{
                    if(SelectedTourLog.getDifficulty()>10 || SelectedTourLog.getDifficulty()<1){
                        showError("Invalid input", "Difficulty must be a number from 1 to 10");
                        return;
                    }
                }
                if (!tryParseFloat(totalDistance, SelectedTourLog::setTotalDistance)) {
                    showError("Invalid input", "Total Distance must be a positive number");
                    return; // or handle the error as needed
                }
                else{
                    if(SelectedTourLog.getTotalDistance()<0){
                        showError("Invalid input", "Total Distance must be a positive number");
                        return;
                    }
                }
                if (!tryParseFloat(totalTime, SelectedTourLog::setTotalTime)) {
                    showError("Invalid input", "Total Time must be a positive number");
                    return; // or handle the error as needed
                }
                else{
                    if(SelectedTourLog.getTotalTime()<0){
                        showError("Invalid input", "Total Time must be a positive number");
                        return;
                    }
                }
                if (!tryParseFloat(rating, SelectedTourLog::setRating)) {
                    showError("Invalid input", "Rating must be a number from 1 to 10");
                    return; // or handle the error as needed
                }
                else{
                    if(SelectedTourLog.getRating()>10 || SelectedTourLog.getRating()<1){
                        showError("Invalid input", "Rating must be a number from 1 to 10");
                        return;
                    }
                }
                logManager.editTourLog(this.SelectedTourLog);
                // Close the window
                Stage stage = (Stage) NewTourLogSubmit.getScene().getWindow();
                stage.close();
            }
        }
    private boolean tryParseFloat(String value, Consumer<Float> consumer) {
        try {
            consumer.accept(Float.parseFloat(value));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private void showError(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

