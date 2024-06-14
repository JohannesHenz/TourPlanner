package com.example.tourplanner;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;
import java.util.Arrays;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;



public class BackendService {
    private static final BackendService instance = new BackendService();
    private static final Logger LOGGER = Logger.getLogger(BackendService.class.getName());

    private boolean startAdding = false; //damit beim initialisieren nicht gleich alle sachen wieder retour geschickt werden lel
    public BackendService() {
    }
    public static BackendService getInstance() {
        return instance;
    }

    public void getTours() {
        String url = "http://localhost:8080/api/tours";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Register JavaTimeModule
        TourManager manager = TourManager.getInstance();
        TourLogManager logManager = TourLogManager.getInstance();
        try {
            // Read JSON data into array of Tour objects
            File jsonData = new File("src/main/resources/GET.json"); //hier muss dann wsl einfach der request body
            Tour[] tours = objectMapper.readValue(jsonData, Tour[].class);

            // Iterate through each Tour object
            for (Tour tour : tours) {
                // Print tour details for verification
                System.out.println("Tour ID: " + tour.getId());
                System.out.println("Tour Name: " + tour.getName());
                System.out.println("Tour Description: " + tour.getDescription());
                List<Tour> tourList = Arrays.asList(tours);
                //schmeiß die tours alle rein
                manager.addTour(tour);


                // Iterate through tourLogs and set tour reference
                if(tour.getTourLogs() != null) {
                    for (TourLogs log : tour.getTourLogs()) {
                        log.setTour(tour); // Set the Tour reference in TourLog
                        log.setTourId(tour.getId());// Print tourLog details for verification
                        logManager.addTourLog(log);

                    }
                }
            }
        } catch (StreamReadException e) {
            throw new RuntimeException(e);
        } catch (DatabindException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("ende von addTrigger");
        startAdding = true;
    }



    public void AddTourPOST(Tour tour) {
        if (startAdding) {

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule()); // Register JavaTimeModule

            // Create a filter provider to ignore 'id' attribute
            FilterProvider filters = new SimpleFilterProvider()
                    .addFilter("tourFilter", SimpleBeanPropertyFilter.serializeAllExcept("id"));

            // Set the filter provider to the object mapper
            objectMapper.setFilterProvider(filters);

            // Add the mixin class to ObjectMapper
            objectMapper.addMixIn(Tour.class, TourMixin.class);

            try {
                // Write to a JSON file
                objectMapper.writeValue(new File("src/main/resources/newtourtmp.json"), tour);
                System.out.println("JSON file updated: newtourtmp.json");
            } catch (IOException e) {
                e.printStackTrace();
            }
            String IdForURL = tour.getId();
            // TO DO:
            // Some REST call back to server to add this to the database, containing the JSON that was just built
        }
    }

    public void AddTourLogPOST(TourLogs log) {
        if (startAdding) {
            System.out.println("stimmts?");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule()); // Register JavaTimeModule

            // Create a filter provider to ignore 'id' attribute
            FilterProvider filters = new SimpleFilterProvider()
                    .addFilter("tourFilter", SimpleBeanPropertyFilter.serializeAllExcept("id"))
                    .addFilter("tourLogsFilter", SimpleBeanPropertyFilter.serializeAllExcept("tourLogs"));
            // Set the filter provider to the object mapper
            objectMapper.setFilterProvider(filters);

            // Add the mixin class to ObjectMapper
            objectMapper.addMixIn(TourLogs.class, TourMixin.class);
            objectMapper.addMixIn(Tour.class, TourMixinLogs.class);
            try {
                // Write to a JSON file
                objectMapper.writeValue(new File("src/main/resources/newtourLogtmp.json"), log);
                System.out.println("JSON file updated: newtourLogtmp.json");
            } catch (IOException e) {
                e.printStackTrace();
            }
            String IdForURL = log.getId();
            // TO DO:
            // Some REST call back to server to add this to the database, containing the JSON that was just built
        }
    }

    public void EditTourPUT(Tour tour){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Register JavaTimeModule

        // Create a filter provider to ignore 'id' attribute
        FilterProvider filters = new SimpleFilterProvider()
                .addFilter("tourFilter", SimpleBeanPropertyFilter.serializeAllExcept("id"));

        // Set the filter provider to the object mapper
        objectMapper.setFilterProvider(filters);

        // Add the mixin class to ObjectMapper
        objectMapper.addMixIn(Tour.class, TourMixin.class);
        try {
            // Write to a JSON file
            objectMapper.writeValue(new File("src/main/resources/editedtourtmp.json"), tour);
            System.out.println("JSON file updated: editedtourtmp.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String IdForURL = tour.getId();
        //TO DO:
        //some restcall back to server to add this to the database, containing the Json
    }

    public void EditTourLogPUT(TourLogs log){
        //write new json for CRUD
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Register JavaTimeModule

        // Create a filter provider to ignore 'id' attribute
        FilterProvider filters = new SimpleFilterProvider()
                .addFilter("tourFilter", SimpleBeanPropertyFilter.serializeAllExcept("id"));

        // Set the filter provider to the object mapper
        objectMapper.setFilterProvider(filters);

        // Add the mixin class to ObjectMapper
        objectMapper.addMixIn(TourLogs.class, TourMixin.class);
        try {
            // Write to a JSON file
            objectMapper.writeValue(new File("src/main/resources/EditedTourLogTmp.json"), log);
            objectMapper.registerModule(new JavaTimeModule()); // Register JavaTimeModule
        } catch (IOException e) {
            e.printStackTrace();
        }
        String IdForURL = log.getId();
        //TO DO:
        //some restcall back to server to add this to the database, containing the Json
    }

    public void DeleteTourDELETE(Tour tour){
        String IdForURL = tour.getId();
        //TO DO:
        //some restcall back to server to delete this from the database, containing the TourID
    }

    public void DeleteTourLogDELETE(TourLogs log){
        String IdForURL = log.getId();
        //TO DO:
        //some restcall back to server to delete this from the database, containing the TourLogID
        }
}
