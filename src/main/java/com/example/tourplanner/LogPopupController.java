package com.example.tourplanner;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import java.time.LocalDate;
import javafx.scene.control.DatePicker;

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

        public void initData(TourLog log, Tour tour, boolean isEdit){
            //damit der setText geht, weil sonst regt heißt cant open window
            focusLabel.requestFocus();
            this.SelectedTourLog = log;
            this.SelectedTour = tour;
            this.isEdit = isEdit;
            if(this.isEdit){
                DateField.setValue(SelectedTourLog.getDate());
                TimeField.setText(String.valueOf(SelectedTourLog.getTime()));
                CommentField.setText(SelectedTourLog.getComment());
                difficultyField.setText(String.valueOf(SelectedTourLog.getDifficulty()));
                totalDistanceField.setText(String.valueOf(SelectedTourLog.getTotalDistance()));
                totalTimeField.setText(String.valueOf(SelectedTourLog.getTotalTime()));
                RatingField.setText(String.valueOf(SelectedTourLog.getRating()));
            }
            else{
                DateField.setValue(LocalDate.now());
                TimeField.setText("0");
                CommentField.setText("no comment");
                difficultyField.setText("3");
                totalDistanceField.setText("0");
                totalTimeField.setText("0");
                RatingField.setText("0");
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
                SelectedTourLog.setTime(Float.parseFloat(time));
                SelectedTourLog.setComment(comment);
                SelectedTourLog.setDifficulty(Float.parseFloat(difficulty));
                SelectedTourLog.setTotalDistance(Float.parseFloat(totalDistance));
                SelectedTourLog.setTotalTime(Float.parseFloat(totalTime));
                SelectedTourLog.setRating(Float.parseFloat(rating));
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
                SelectedTourLog.setTime(Float.parseFloat(time));
                SelectedTourLog.setComment(comment);
                SelectedTourLog.setDifficulty(Float.parseFloat(difficulty));
                SelectedTourLog.setTotalDistance(Float.parseFloat(totalDistance));
                SelectedTourLog.setTotalTime(Float.parseFloat(totalTime));
                SelectedTourLog.setRating(Float.parseFloat(rating));
                //hier fehlt noch handling falls was leer is
                logManager.editTourLog(this.SelectedTourLog);
                // Close the window
                Stage stage = (Stage) NewTourLogSubmit.getScene().getWindow();
                stage.close();
            }
        }
}

