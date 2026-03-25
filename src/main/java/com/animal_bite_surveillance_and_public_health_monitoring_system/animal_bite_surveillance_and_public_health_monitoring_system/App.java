package com.animal_bite_surveillance_and_public_health_monitoring_system.animal_bite_surveillance_and_public_health_monitoring_system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("/com/abms/ui/login.fxml"), 800, 600);
        stage.setTitle("Animal Bite Surveillance System");
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxmlPath) throws IOException {
        scene.setRoot(loadFXML(fxmlPath));
    }

    private static Parent loadFXML(String fxmlPath) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxmlPath));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}