module com.example.restaurant_simulator {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.academyadvise_sim to javafx.fxml;
    exports com.example.academyadvise_sim;
    exports com.example.academyadvise_sim.controller;
    opens com.example.academyadvise_sim.controller to javafx.fxml;
}