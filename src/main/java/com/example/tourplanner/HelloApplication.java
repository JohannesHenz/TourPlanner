package com.example.tourplanner;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.io.InputStream;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1320, 840);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        TourManager manager = TourManager.getInstance();
        try {
            // Read JSON file and map it to a Map
            File testdata = new File("src/main/resources/testdata.json");
            Tour[] tourArray = objectMapper.readValue(testdata, Tour[].class);
            List<Tour> tourList = Arrays.asList(tourArray);

            //schmeiß die tours alle rein
            for (Tour tour : tourList) {
                    manager.addTour(tour);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            // Read JSON file and map it to a Map
            objectMapper.registerModule(new JavaTimeModule());
            File testdata = new File("src/main/resources/testlogs.json");
            TourLog[] tourArray = objectMapper.readValue(testdata, TourLog[].class);
            List<TourLog> LogList = Arrays.asList(tourArray);
            //hol die instance
            TourLogManager logManager = TourLogManager.getInstance();
            //schmeiß die tours alle rein
            for (TourLog log : LogList) {
                log.setTour(manager.getById(log.getTourId()));
                logManager.addTourLog(log);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        launch();
        }
}