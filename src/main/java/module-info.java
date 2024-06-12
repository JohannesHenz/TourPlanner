module com.example.tourplanner {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires java.net.http;
    requires kernel;
    requires layout;

    opens com.example.tourplanner to javafx.fxml;
    exports com.example.tourplanner;
}