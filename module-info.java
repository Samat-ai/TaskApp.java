module com.example.sceneapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens com.example.sceneapp to javafx.fxml;
    exports com.example.sceneapp;
}
