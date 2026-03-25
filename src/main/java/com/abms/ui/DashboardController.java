package com.abms.ui;

import com.animal_bite_surveillance_and_public_health_monitoring_system.animal_bite_surveillance_and_public_health_monitoring_system.App;
import javafx.fxml.FXML;
import java.io.IOException;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;

public class DashboardController {

    @FXML
    private ListView<String> activityList;

    @FXML
    public void initialize() {
        // reference activityList so IDE/compiler recognises the fx:id usage
        if (activityList != null) {
            activityList.setPlaceholder(new Label("No recent activity"));
        }
    }

    @FXML
    private void showPatientManagement() throws IOException {
        App.setRoot("/com/abms/ui/patient_management.fxml");
    }

    @FXML
    private void showVaccinationMonitoring() throws IOException {
        App.setRoot("/com/abms/ui/vaccination_monitoring.fxml");
    }

    @FXML
    private void showReportsAnalytics() throws IOException {
        App.setRoot("/com/abms/ui/reports_analytics.fxml");
    }

    @FXML
    private void handleLogout() throws IOException {
        App.setRoot("/com/abms/ui/login.fxml");
    }
}
