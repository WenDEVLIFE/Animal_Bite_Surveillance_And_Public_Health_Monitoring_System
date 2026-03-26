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
    private final com.abms.repository.PatientRepository patientRepository;

    @FXML private TableView<com.abms.model.Patient> patientTable;
    @FXML private TableColumn<com.abms.model.Patient, Integer> idCol;
    @FXML private TableColumn<com.abms.model.Patient, String> firstNameCol;
    @FXML private TableColumn<com.abms.model.Patient, String> lastNameCol;
    @FXML private TableColumn<com.abms.model.Patient, Integer> ageCol;
    @FXML private TableColumn<com.abms.model.Patient, String> genderCol;
    @FXML private TableColumn<com.abms.model.Patient, String> contactCol;
    @FXML private TableColumn<com.abms.model.Patient, String> addressCol;

    public PatientController() {
        this.patientService = new PatientService();
        this.patientRepository = new com.abms.repository.PatientRepository();
    }

    @FXML
    public void initialize() {
        genderCombo.getItems().addAll("Male", "Female", "Other");
        animalTypeCombo.getItems().addAll("Dog", "Cat", "Other");
        animalStatusCombo.getItems().addAll("Alive", "Dead", "Unknown", "Observation");
        exposureTypeCombo.getItems().addAll("Category I", "Category II", "Category III");
        
        biteDatePicker.setValue(LocalDate.now());

        // Initialize table columns
        idCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("id"));
        firstNameCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("firstName"));
        lastNameCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("lastName"));
        ageCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("age"));
        genderCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("gender"));
        contactCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("contactNumber"));
        addressCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("address"));

        loadPatients();
    }

    private void loadPatients() {
        try {
            javafx.collections.ObservableList<com.abms.model.Patient> patients = javafx.collections.FXCollections.observableArrayList(patientRepository.getAllPatients());
            patientTable.setItems(patients);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
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
            loadPatients(); // Refresh the list after saving new patient
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
