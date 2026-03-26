package com.abms.ui;

import com.animal_bite_surveillance_and_public_health_monitoring_system.animal_bite_surveillance_and_public_health_monitoring_system.App;
import javafx.fxml.FXML;
import java.io.IOException;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;

public class DashboardController {

    @FXML private ListView<String> activityList;
    @FXML private Label todayCasesLabel;
    @FXML private Label pendingVaccLabel;
    @FXML private Label completedDosesLabel;

    private final com.abms.repository.BiteIncidentRepository incidentRepo;
    private final com.abms.repository.VaccinationRepository vaccinationRepo;
    private javafx.animation.Timeline refreshTimeline;

    public DashboardController() {
        this.incidentRepo = new com.abms.repository.BiteIncidentRepository();
        this.vaccinationRepo = new com.abms.repository.VaccinationRepository();
    }

    @FXML
    public void initialize() {
        if (activityList != null) {
            activityList.setPlaceholder(new Label("No recent activity"));
        }
        
        refreshStatistics();
        startRefreshTimeline();
    }

    private void startRefreshTimeline() {
        refreshTimeline = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(javafx.util.Duration.seconds(10), event -> refreshStatistics())
        );
        refreshTimeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
        refreshTimeline.play();
    }

    private void refreshStatistics() {
        try {
            int todayCases = incidentRepo.getTodayIncidentCount();
            int pendingVacc = vaccinationRepo.getPendingVaccinationCount();
            int completedDoses = vaccinationRepo.getCompletedDoseCount();

            javafx.application.Platform.runLater(() -> {
                todayCasesLabel.setText(String.valueOf(todayCases));
                pendingVaccLabel.setText(String.valueOf(pendingVacc));
                completedDosesLabel.setText(String.valueOf(completedDoses));
                
                updateActivityList(todayCases, pendingVacc, completedDoses);
            });
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateActivityList(int today, int pending, int completed) {
        javafx.collections.ObservableList<String> activities = javafx.collections.FXCollections.observableArrayList();
        activities.add("System Status: Operational");
        activities.add("Total Bite Incidents recorded today: " + today);
        activities.add("Upcoming vaccinations to be administered: " + pending);
        activities.add("Total doses successfully completed: " + completed);
        activities.add("Last updated: " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
        activityList.setItems(activities);
    }

    @FXML
    private void showPatientManagement() throws IOException {
        stopTimeline();
        App.setRoot("/com/abms/ui/patient_management.fxml");
    }

    @FXML
    private void showVaccinationMonitoring() throws IOException {
        stopTimeline();
        App.setRoot("/com/abms/ui/vaccination_monitoring.fxml");
    }

    @FXML
    private void showReportsAnalytics() throws IOException {
        stopTimeline();
        App.setRoot("/com/abms/ui/reports_analytics.fxml");
    }

    @FXML
    private void handleLogout() throws IOException {
        stopTimeline();
        App.setRoot("/com/abms/ui/login.fxml");
    }

    private void stopTimeline() {
        if (refreshTimeline != null) {
            refreshTimeline.stop();
        }
    }
}
