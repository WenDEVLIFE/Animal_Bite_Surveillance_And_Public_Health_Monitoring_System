package com.abms.ui;

import com.animal_bite_surveillance_and_public_health_monitoring_system.animal_bite_surveillance_and_public_health_monitoring_system.App;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;

public class ReportController {

    @FXML private javafx.scene.chart.PieChart animalTypeChart;
    @FXML private javafx.scene.chart.BarChart<String, Number> monthlyChart;

    private final com.abms.repository.BiteIncidentRepository incidentRepo;

    public ReportController() {
        this.incidentRepo = new com.abms.repository.BiteIncidentRepository();
    }

    @FXML
    public void initialize() {
        loadAnimalTypeData();
        loadMonthlyTrendsData();
    }

    private void loadAnimalTypeData() {
        try {
            java.util.Map<String, Integer> counts = incidentRepo.getIncidentCountsByAnimalType();
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            
            counts.forEach((type, count) -> {
                pieChartData.add(new PieChart.Data(type + " (" + count + ")", count));
            });
            
            animalTypeChart.setData(pieChartData);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadMonthlyTrendsData() {
        try {
            java.util.Map<String, Integer> counts = incidentRepo.getMonthlyIncidentCounts();
            javafx.scene.chart.XYChart.Series<String, Number> series = new javafx.scene.chart.XYChart.Series<>();
            series.setName("Incidents");
            
            counts.forEach((month, count) -> {
                series.getData().add(new javafx.scene.chart.XYChart.Data<>(month, count));
            });
            
            monthlyChart.getData().clear();
            monthlyChart.getData().add(series);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goBack() throws IOException {
        App.setRoot("/com/abms/ui/dashboard.fxml");
    }
}
