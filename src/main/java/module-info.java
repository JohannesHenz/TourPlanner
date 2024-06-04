module com.example.tourplanner {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.bootstrapfx.core;
    requires static lombok;

    opens com.FHTW.tourplanner.Controller to javafx.fxml;
    exports com.FHTW.tourplanner.Controller;
}
