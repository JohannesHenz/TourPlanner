package com.example.tourplanner;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Level;
import java.util.logging.LogManager;
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

import javax.imageio.ImageIO;


public class BackendService {
    private static final BackendService instance = new BackendService();
    private static final Logger LOGGER = Logger.getLogger(BackendService.class.getName());

    private boolean startAdding = false; //damit beim initialisieren nicht gleich alle sachen wieder retour geschickt werden lel
    public BackendService() {
    }
    public static BackendService getInstance() {
        return instance;
    }

    private TourManager manager = TourManager.getInstance();
    private TourLogManager logManager = TourLogManager.getInstance();

    public void getTours() {
        String url = "http://localhost:8080/api/tours";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Register JavaTimeModule
        try {
            //for testing with static data
            /*File jsonData = new File("src/main/resources/GET.json"); //hier muss dann wsl einfach der request body
            Tour[] tours = objectMapper.readValue(jsonData, Tour[].class);
            */
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check if the response status code is 200 (OK)
            if (response.statusCode() == 200) {
                // Read JSON data into array of Tour objects from response body
                String responseBody = response.body();
                Tour[] tours = objectMapper.readValue(responseBody, Tour[].class);
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
                    if (tour.getTourLogs() != null) {
                        for (TourLogs log : tour.getTourLogs()) {
                            log.setTour(tour); // Set the Tour reference in TourLog
                            log.setTourId(tour.getId());// Print tourLog details for verification
                            logManager.addTourLog(log);

                        }
                    }
                }
                //hol alle images
                for(Tour tour : manager.getTours()){
                    ImageGET(tour.getId());
                }
            } else {
                System.out.println("Failed to fetch tours. HTTP error code: " + response.statusCode());
            }

            } catch (StreamReadException e) {
                throw new RuntimeException(e);
            } catch (DatabindException e) {
                throw new RuntimeException(e);
            } catch (IOException | InterruptedException e) {
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

                // Convert the Tour object to JSON string
                String tourJson = objectMapper.writeValueAsString(tour);
                // Create the HttpClient
                HttpClient client = HttpClient.newHttpClient();
                // Create the HttpRequest
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/api/tours"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(tourJson))
                        .build();

                // Send the request and get the response
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                // Check if the response status code is 200 (OK) or 201 (Created)
                if (response.statusCode() == 200 || response.statusCode() == 201) {
                    // Parse the response body (JSON) here
                    String responseBody = response.body();
                    System.out.println("Response JSON: " + responseBody);
                    // TO DO: Handle the parsed JSON response
                    //speihern die Tour zwischen
                    ObjectMapper objectMapper2 = new ObjectMapper();
                    objectMapper.registerModule(new JavaTimeModule()); // Register JavaTimeModule
                    Tour tmptour = objectMapper2.readValue(responseBody, Tour.class);
                    System.out.println("tmptour ID" + tmptour.getId());
                    //die Tour die wir grad angelegt haben, kriegt jetzt das from, to und vor allem die neue id aus dem Backend
                    manager.getById(tour.getId()).setDistance(tmptour.getDistance());
                    manager.getById(tour.getId()).setEstimatedTime(tmptour.getEstimatedTime());
                    manager.getById(tour.getId()).setId(tmptour.getId());
                    ImageGET(tmptour.getId());
                } else {
                    System.out.println("Failed to post tour. HTTP error code: " + response.statusCode());
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void AddTourLogPOST(TourLogs log) {
        if (startAdding) {
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

                // Convert the Tour object to JSON string
                String logJson = objectMapper.writeValueAsString(log);
                // Create the HttpClient
                HttpClient client = HttpClient.newHttpClient();
                // Create the HttpRequest
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/api/tours/"+log.getTourId() +"/logs"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(logJson))
                        .build();
                System.out.println("HTTPREQUEST:"+ request);
                // Send the request and get the response
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                // Check if the response status code is 200 (OK) or 201 (Created)
                if (response.statusCode() == 200 || response.statusCode() == 201) {
                    // Parse the response body (JSON) here
                    String responseBody = response.body();
                    System.out.println("Response JSON: " + responseBody);
                    //speihern den log zwischen
                    ObjectMapper objectMapper2 = new ObjectMapper();
                    objectMapper2.registerModule(new JavaTimeModule());
                    TourLogs tmplog = objectMapper2.readValue(responseBody, TourLogs.class);
                    //das log das wir grad angelegt haben, kriegt jetzt die neue id aus dem Backend
                    logManager.getById(log.getLogId()).setLogId(tmplog.getLogId());
                } else {
                    System.out.println("Failed to post log. HTTP error code: " + response.statusCode());
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            String IdForURL = log.getLogId();
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
            // Write to a JSON file (for debugging)
            objectMapper.writeValue(new File("src/main/resources/editedtourtmp.json"), tour);
            System.out.println("JSON file updated: editedtourtmp.json");

            // Convert the Tour object to JSON string
            String tourJson = objectMapper.writeValueAsString(tour);

            // Create the HttpClient
            HttpClient client = HttpClient.newHttpClient();

            // Construct the URL for the PUT request
            String url = "http://localhost:8080/api/tours/" + tour.getId();

            // Create the HttpRequest
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(tourJson))
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check if the response status code is 200 (OK)
            if (response.statusCode() == 200 || response.statusCode() == 204) {
                // Successfully updated the tour
                String responseBody = response.body();
                System.out.println("Response JSON: " + responseBody);
                //speihern die Tour zwischen
                ObjectMapper objectMapper2 = new ObjectMapper();
                Tour tmptour = objectMapper2.readValue(responseBody, Tour.class);
                //die Tour die wir grad editiert haben
                manager.getById(tour.getId()).setDistance(tmptour.getDistance());
                manager.getById(tour.getId()).setEstimatedTime(tmptour.getEstimatedTime());
                ImageGET(tmptour.getId());
            } else {
                System.out.println("Failed to update tour. HTTP error code: " + response.statusCode());
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
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
            // Write to a JSON file (for debugging)
            objectMapper.writeValue(new File("src/main/resources/EditedTourLogTmp.json"), log);
            objectMapper.registerModule(new JavaTimeModule()); // Register JavaTimeModule
            // Convert the log object to JSON string
            String logJson = objectMapper.writeValueAsString(log);
            // Create the HttpClient
            HttpClient client = HttpClient.newHttpClient();
            // Construct the URL for the PUT request
            String url = "http://localhost:8080/api/tours/" + log.getTourId()+ "/" + log.getLogId();

            // Create the HttpRequest
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(logJson))
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check if the response status code is 200 (OK)
            if (response.statusCode() == 200 || response.statusCode() == 204) {
                // Successfully updated the tour
                System.out.println("Log updated successfully.");
            } else {
                System.out.println("Failed to update log. HTTP error code: " + response.statusCode());
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void DeleteTourDELETE(Tour tour) {
        // Get the ID for the URL
        String IdForURL = tour.getId();

        // Construct the URL for the DELETE request
        String url = "http://localhost:8080/api/tours/" + IdForURL;

        try {
            // Create the HttpClient
            HttpClient client = HttpClient.newHttpClient();

            // Create the HttpRequest
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .DELETE()
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check if the response status code is 200 (OK) or 204 (No Content)
            if (response.statusCode() == 200 || response.statusCode() == 204) {
                // Successfully deleted the tour
                System.out.println("Tour deleted successfully.");
            } else {
                System.out.println("Failed to delete tour. HTTP error code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void DeleteTourLogDELETE(TourLogs log){
        // Get the ID for the URL
        String TourIdForURL = log.getTourId();
        String IdForURL = log.getLogId();

        // Construct the URL for the DELETE request
        String url = "http://localhost:8080/api/tours/" + TourIdForURL + "/" + IdForURL;

        try {
            // Create the HttpClient
            HttpClient client = HttpClient.newHttpClient();

            // Create the HttpRequest
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .DELETE()
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check if the response status code is 200 (OK) or 204 (No Content)
            if (response.statusCode() == 200 || response.statusCode() == 204) {
                // Successfully deleted the tour
                System.out.println("Tour deleted successfully.");
            } else {
                System.out.println("Failed to delete tour. HTTP error code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void ImageGET(String tourId){
        try {
            String url = "http://localhost:8080/api/tours/" + tourId + "/image";
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            // Send the request and handle the response
            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
            if (response.statusCode() == 200) {
                // Read the image bytes from the response body
                byte[] imageBytes = response.body();

                // Convert byte array to BufferedImage
                BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
                Image newImg = new Image(tourId, image);
                ImageManager imgManager = ImageManager.getInstance();
                imgManager.addImage(newImg);
                System.out.println("Image fetched and saved successfully.");
            } else {
                System.out.println("Failed to fetch image. HTTP error code: " + response.statusCode());
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
