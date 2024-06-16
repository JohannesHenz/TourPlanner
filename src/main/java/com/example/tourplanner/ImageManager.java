package com.example.tourplanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ImageManager {

    private static final ImageManager instance = new ImageManager();

    private ObservableList<Image> imagelist = FXCollections.observableArrayList();

    public static ImageManager getInstance() {
        return instance;
    }

    public void addImage(Image img) {
        imagelist.add(img);
    }

    public void deleteImageByTourId(String tourId) {
        // Iterate through the imagelist to find the Image with matching tourId
        for (Image image : imagelist) {
            if (image.getTourId().equals(tourId)) {
                // Remove the image from the imagelist
                imagelist.remove(image);
                System.out.println("Image with tourId " + tourId + " deleted successfully.");
                return; // Exit the method after deleting the image
            }
        }
        // If no matching image found, print a message
        System.out.println("Image with tourId " + tourId + " not found in the list.");
    }

    public ObservableList<Image> getAllImages() {
        return imagelist;
    }

    public Image getImageByTourId(String tourId) {
        // Iterate through the imagelist to find the Image with matching tourId
        for (Image image : imagelist) {
            if (image.getTourId().equals(tourId)) {
                // Remove the image from the imagelist
                return image; // Exit the method after deleting the image
            }
        }
        return null;
    }
}
