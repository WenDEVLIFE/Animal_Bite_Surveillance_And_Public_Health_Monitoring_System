package com.abms.ui;

import com.abms.model.Patient;
import com.abms.repository.PatientRepository;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.SQLException;

public class EditPatientDialogController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField ageField;
    @FXML private ComboBox<String> genderCombo;
    @FXML private TextField contactField;
    @FXML private TextArea addressArea;

    private PatientRepository patientRepository;
    private Patient patient;
    private boolean updated = false;

    @FXML
    public void initialize() {
        genderCombo.getItems().addAll("Male", "Female", "Other");
        patientRepository = new PatientRepository();
    }

    public void setPatient(Patient p) {
        try {
            // Fetch fresh data from database as requested by user
            this.patient = patientRepository.getPatientById(p.getId());
            if (this.patient != null) {
                firstNameField.setText(this.patient.getFirstName());
                lastNameField.setText(this.patient.getLastName());
                ageField.setText(String.valueOf(this.patient.getAge()));
                genderCombo.setValue(this.patient.getGender());
                contactField.setText(this.patient.getContactNumber());
                addressArea.setText(this.patient.getAddress());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Fallback to passed object if DB fetch fails
            this.patient = p;
            firstNameField.setText(p.getFirstName());
            lastNameField.setText(p.getLastName());
            ageField.setText(String.valueOf(p.getAge()));
            genderCombo.setValue(p.getGender());
            contactField.setText(p.getContactNumber());
            addressArea.setText(p.getAddress());
        }
    }

    public boolean isUpdated() {
        return updated;
    }

    @FXML
    private void handleUpdate() {
        try {
            Patient updatedPatient = new Patient(
                patient.getId(),
                firstNameField.getText(),
                lastNameField.getText(),
                Integer.parseInt(ageField.getText()),
                genderCombo.getValue(),
                addressArea.getText(),
                contactField.getText()
            );

            patientRepository.updatePatient(updatedPatient);
            updated = true;
            closeStage();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to update patient: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleCancel() {
        closeStage();
    }

    private void closeStage() {
        ((Stage) firstNameField.getScene().getWindow()).close();
    }
}
