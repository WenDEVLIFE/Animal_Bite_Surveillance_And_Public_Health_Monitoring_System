package com.abms.ui;

import com.animal_bite_surveillance_and_public_health_monitoring_system.animal_bite_surveillance_and_public_health_monitoring_system.App;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;

public class ReportController {

    @FXML private PieChart animalTypeChart;

    @FXML
    public void initialize() {
        // Mock data for analytics visually
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                new PieChart.Data("Dogs", 65),
                new PieChart.Data("Cats", 25),
                new PieChart.Data("Others", 10));
        animalTypeChart.setData(pieChartData);
        animalTypeChart.setTitle("Bite Incidents by Animal Type");
    }

    @FXML
    private void goBack() throws IOException {
        App.setRoot("/com/abms/ui/dashboard.fxml");
    }
}
