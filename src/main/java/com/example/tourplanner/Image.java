package com.example.tourplanner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Image {
    private String tourId;
    private String imageName;

    public Image(String tourId, BufferedImage image) {
        this.tourId = tourId;
        try {
            // Create a file name using tourId and save the image as PNG
            String fileName = "image" +tourId + ".png";
            imageName = fileName;
            File file = new File("src/main/resources/images", fileName); // Replace with your directory path
            ImageIO.write(image, "png", file);
            System.out.println("Image saved successfully: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Getters and Setters
    public String getTourId() {
        return tourId;
    }

    public void setTourId(String tourId) {
        this.tourId = tourId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}