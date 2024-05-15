module com.example.tourplanner {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.FHTW.tourplanner to javafx.fxml;
    exports com.FHTW.tourplanner;
}