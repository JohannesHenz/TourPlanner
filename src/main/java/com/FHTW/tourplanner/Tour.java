package com.FHTW.tourplanner;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

public class Tour {
    private StringProperty name;
    private ObjectProperty<Image> image;

    public Tour(String name, Image image) {
        this.name = new SimpleStringProperty(name);
        this.image = new SimpleObjectProperty<>(image);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public ObjectProperty<Image> imageProperty() {
        return image;
    }

    public Image getImage() {
        return image.get();
    }

    public void setImage(Image image) {
        this.image.set(image);
    }


    // getters and setters
}