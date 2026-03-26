package com.abms.ui;

import com.abms.model.BiteIncident;
import com.abms.repository.BiteIncidentRepository;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.SQLException;

public class EditIncidentDialogController {

    @FXML private Label patientNameLabel;
    @FXML private DatePicker biteDatePicker;
    @FXML private ComboBox<String> animalTypeCombo;
    @FXML private TextField biteSiteField;
    @FXML private ComboBox<String> animalStatusCombo;
    @FXML private ComboBox<String> exposureTypeCombo;
    @FXML private TextArea remarksArea;

    private BiteIncidentRepository incidentRepository;
    private BiteIncident incident;
    private boolean updated = false;

    @FXML
    public void initialize() {
        animalTypeCombo.getItems().addAll("Dog", "Cat", "Other");
        animalStatusCombo.getItems().addAll("Alive", "Dead", "Unknown", "Observation");
        exposureTypeCombo.getItems().addAll("Category I", "Category II", "Category III");
        incidentRepository = new BiteIncidentRepository();
    }

    public void setIncident(BiteIncident inc) {
        try {
            // Fetch fresh data from database
            this.incident = incidentRepository.getIncidentById(inc.getId());
            if (this.incident != null) {
                patientNameLabel.setText(this.incident.getPatientName());
                biteDatePicker.setValue(this.incident.getBiteDate());
                animalTypeCombo.setValue(this.incident.getAnimalType());
                biteSiteField.setText(this.incident.getBiteSite());
                animalStatusCombo.setValue(this.incident.getAnimalStatus());
                exposureTypeCombo.setValue(this.incident.getExposureType());
                remarksArea.setText(this.incident.getRemarks());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Fallback
            this.incident = inc;
            patientNameLabel.setText(inc.getPatientName());
            biteDatePicker.setValue(inc.getBiteDate());
            animalTypeCombo.setValue(inc.getAnimalType());
            biteSiteField.setText(inc.getBiteSite());
            animalStatusCombo.setValue(inc.getAnimalStatus());
            exposureTypeCombo.setValue(inc.getExposureType());
            remarksArea.setText(inc.getRemarks());
        }
    }

    public boolean isUpdated() {
        return updated;
    }

    @FXML
    private void handleUpdate() {
        try {
            BiteIncident updatedInc = new BiteIncident(
                incident.getId(),
                incident.getPatientId(),
                incident.getPatientName(),
                biteDatePicker.getValue(),
                animalTypeCombo.getValue(),
                biteSiteField.getText(),
                animalStatusCombo.getValue(),
                exposureTypeCombo.getValue(),
                remarksArea.getText()
            );

            incidentRepository.updateIncident(updatedInc);
            updated = true;
            closeStage();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to update incident: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleCancel() {
        closeStage();
    }

    private void closeStage() {
        ((Stage) biteDatePicker.getScene().getWindow()).close();
    }
}
