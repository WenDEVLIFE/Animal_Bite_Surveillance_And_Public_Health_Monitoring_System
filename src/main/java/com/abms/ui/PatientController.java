package com.abms.ui;

import com.animal_bite_surveillance_and_public_health_monitoring_system.animal_bite_surveillance_and_public_health_monitoring_system.App;
import com.abms.service.PatientService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;
import java.time.LocalDate;

public class PatientController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField ageField;
    @FXML private ComboBox<String> genderCombo;
    @FXML private TextArea addressArea;
    @FXML private TextField contactField;
    
    @FXML private DatePicker biteDatePicker;
    @FXML private ComboBox<String> animalTypeCombo;
    @FXML private TextField biteSiteField;
    @FXML private ComboBox<String> animalStatusCombo;
    @FXML private ComboBox<String> exposureTypeCombo;
    @FXML private TextArea remarksArea;

    private final PatientService patientService;

    public PatientController() {
        this.patientService = new PatientService();
    }

    @FXML
    public void initialize() {
        genderCombo.getItems().addAll("Male", "Female", "Other");
        animalTypeCombo.getItems().addAll("Dog", "Cat", "Other");
        animalStatusCombo.getItems().addAll("Alive", "Dead", "Unknown", "Observation");
        exposureTypeCombo.getItems().addAll("Category I", "Category II", "Category III");
        
        biteDatePicker.setValue(LocalDate.now());
    }

    @FXML
    private void handleSave() {
        try {
            int age = Integer.parseInt(ageField.getText());
            patientService.registerPatientWithIncident(
                firstNameField.getText(), lastNameField.getText(), age, genderCombo.getValue(),
                addressArea.getText(), contactField.getText(),
                biteDatePicker.getValue(), animalTypeCombo.getValue(), biteSiteField.getText(),
                animalStatusCombo.getValue(), exposureTypeCombo.getValue(), remarksArea.getText()
            );
            
            showAlert(Alert.AlertType.INFORMATION, "Success", "Patient and Incident recorded successfully. Vaccination schedule generated.");
            clearFields();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save record: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        ageField.clear();
        addressArea.clear();
        contactField.clear();
        biteSiteField.clear();
        remarksArea.clear();
    }

    @FXML
    private void goBack() throws IOException {
        App.setRoot("/com/abms/ui/dashboard.fxml");
    }
}
