package com.example.tourplanner;
import com.example.tourplanner.Tour;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class TourManager {

    private static final TourManager instance = new TourManager();

    private ObservableList<Tour> tours = FXCollections.observableArrayList();


    private Tour selectedTour;

    public TourManager() {

        tours.addListener((ListChangeListener.Change<? extends Tour> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Tour tour : change.getAddedSubList()) {
                            HelloController hello = HelloController.getInstance();
                            //add to view
                            System.out.println("New Tour Added: " + tour.getName());
                            //hello.addToList(tour);

                            //write new json for CRUD
                            ObjectMapper objectMapper = new ObjectMapper();
                            try {
                                // Write to a JSON file
                                objectMapper.writeValue(new File("src/main/resources/newtourtmp.json"), tour);
                                System.out.println("JSON file updated: newtourtmp.json");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //TO DO:
                            //some restcall back to server to add this to the database, containing the Json
                    }
                }
                if (change.wasRemoved()) {
                    //TO DO:
                    //some restcall back to server to add this to the database, containing the TourID
                    }
                }

            }
        );
    }

    public static TourManager getInstance() {
        return instance;
    }

    public Tour getSelectedTour() {
        return selectedTour;
    }

    public void setSelectedTour(Tour selectedTour) {
         this.selectedTour = selectedTour;
    }

    public void addTour(Tour tour) {
        tours.add(tour);
    }

    public void editTour(Tour editedTour) {
        for (int i = 0; i < tours.size(); i++) {
            if (tours.get(i).getId().equals(editedTour.getId())) {
                tours.get(i).setName(editedTour.getName());
                tours.get(i).setDescription(editedTour.getDescription());
                tours.get(i).setFrom(editedTour.getFrom());
                tours.get(i).setTo(editedTour.getTo());
                tours.get(i).setTransportType(editedTour.getTransportType());
                tours.get(i).setDistance(editedTour.getDistance());
                tours.get(i).setEstimatedTime(editedTour.getEstimatedTime());
                HelloController hello = HelloController.getInstance();
                System.out.println("Tour Edited: " + editedTour.getName());
                hello.updateList();
                //write new json for CRUD
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    // Write to a JSON file
                    objectMapper.writeValue(new File("src/main/resources/editedtourtmp.json"), editedTour);
                    System.out.println("JSON file updated: editedtourtmp.json");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //TO DO:
                //some restcall back to server to add this to the database, containing the Json
                return; // Exit the method once the replacement is done
            }
        }
    }

    public void deleteTour(Tour tourToDelete) {
        tours.remove(tourToDelete);
    }

    public String getNewID(){
        String id;
        boolean isUnique;
        do {
            id = UUID.randomUUID().toString();
            isUnique = true;
            for (Tour tour : tours) {
                if (tour.getId().equals(id)) {
                    isUnique = false;
                    break;
                }
            }
        } while (!isUnique);
        return id;
    }

    public ObservableList<Tour> getTours() {
        return tours;
    }
    public Tour getById(String id) {
        for (Tour tour : tours) {
            if (tour.getId().equals(id)) {
                return tour;
            }
        }
        return null; // If no tour with matching ID is found
    }
}
