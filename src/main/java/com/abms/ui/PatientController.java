package com.abms.ui;

import com.animal_bite_surveillance_and_public_health_monitoring_system.animal_bite_surveillance_and_public_health_monitoring_system.App;
import com.abms.service.PatientService;
import com.abms.utils.UserSession;
import com.abms.repository.ActivityLogRepository;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;
import java.time.LocalDate;
import java.sql.SQLException;

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

    @FXML private TableView<com.abms.model.Patient> patientTable;
    @FXML private TableColumn<com.abms.model.Patient, Integer> idCol;
    @FXML private TableColumn<com.abms.model.Patient, String> firstNameCol;
    @FXML private TableColumn<com.abms.model.Patient, String> lastNameCol;
    @FXML private TableColumn<com.abms.model.Patient, Integer> ageCol;
    @FXML private TableColumn<com.abms.model.Patient, String> genderCol;
    @FXML private TableColumn<com.abms.model.Patient, String> contactCol;
    @FXML private TableColumn<com.abms.model.Patient, String> addressCol;
    @FXML private TableColumn<com.abms.model.Patient, Void> actionCol;
    
    @FXML private TableView<com.abms.model.BiteIncident> incidentTable;
    @FXML private TableColumn<com.abms.model.BiteIncident, Integer> incIdCol;
    @FXML private TableColumn<com.abms.model.BiteIncident, String> incPatientCol;
    @FXML private TableColumn<com.abms.model.BiteIncident, LocalDate> incDateCol;
    @FXML private TableColumn<com.abms.model.BiteIncident, String> incAnimalCol;
    @FXML private TableColumn<com.abms.model.BiteIncident, String> incSiteCol;
    @FXML private TableColumn<com.abms.model.BiteIncident, String> incStatusCol;
    @FXML private TableColumn<com.abms.model.BiteIncident, String> incExposureCol;
    @FXML private TableColumn<com.abms.model.BiteIncident, String> incRemarksCol;
    @FXML private TableColumn<com.abms.model.BiteIncident, Void> incActionCol;
    @FXML private Button saveButton;

    private final PatientService patientService;
    private final com.abms.repository.PatientRepository patientRepository;
    private final com.abms.repository.BiteIncidentRepository incidentRepository;
    private final com.abms.repository.ActivityLogRepository logRepository;

    public PatientController() {
        this.patientService = new PatientService();
        this.patientRepository = new com.abms.repository.PatientRepository();
        this.incidentRepository = new com.abms.repository.BiteIncidentRepository();
        this.logRepository = new com.abms.repository.ActivityLogRepository();
    }

    @FXML
    public void initialize() {
        genderCombo.getItems().addAll("Male", "Female", "Other");
        animalTypeCombo.getItems().addAll("Dog", "Cat", "Other");
        animalStatusCombo.getItems().addAll("Alive", "Dead", "Unknown", "Observation");
        exposureTypeCombo.getItems().addAll("Category I", "Category II", "Category III");
        
        biteDatePicker.setValue(LocalDate.now());

        // Initialize patient table columns
        idCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("id"));
        firstNameCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("firstName"));
        lastNameCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("lastName"));
        ageCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("age"));
        genderCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("gender"));
        contactCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("contactNumber"));
        addressCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("address"));

        // Initialize incident table columns
        incIdCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("id"));
        incPatientCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("patientName"));
        incDateCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("biteDate"));
        incAnimalCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("animalType"));
        incSiteCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("biteSite"));
        incStatusCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("animalStatus"));
        incExposureCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("exposureType"));
        incRemarksCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("remarks"));

        setupActionColumn();
        setupIncidentActionColumn();
        loadPatients();
        loadIncidents();
    }

    private void setupActionColumn() {
        actionCol.setCellFactory(param -> new TableCell<com.abms.model.Patient, Void>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final javafx.scene.layout.HBox pane = new javafx.scene.layout.HBox(10, editBtn, deleteBtn);

            {
                editBtn.getStyleClass().add("button-edit");
                editBtn.setStyle("-fx-background-color: #F57C00; -fx-text-fill: white;");
                deleteBtn.setStyle("-fx-background-color: #D32F2F; -fx-text-fill: white;");
                
                editBtn.setOnAction(event -> {
                    com.abms.model.Patient p = getTableView().getItems().get(getIndex());
                    handleEdit(p);
                });
                deleteBtn.setOnAction(event -> {
                    com.abms.model.Patient p = getTableView().getItems().get(getIndex());
                    handleDelete(p);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    private void setupIncidentActionColumn() {
        incActionCol.setCellFactory(param -> new TableCell<com.abms.model.BiteIncident, Void>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final javafx.scene.layout.HBox pane = new javafx.scene.layout.HBox(10, editBtn, deleteBtn);

            {
                editBtn.setStyle("-fx-background-color: #F57C00; -fx-text-fill: white;");
                deleteBtn.setStyle("-fx-background-color: #D32F2F; -fx-text-fill: white;");
                
                editBtn.setOnAction(event -> {
                    com.abms.model.BiteIncident inc = getTableView().getItems().get(getIndex());
                    handleIncidentEdit(inc);
                });
                deleteBtn.setOnAction(event -> {
                    com.abms.model.BiteIncident inc = getTableView().getItems().get(getIndex());
                    handleIncidentDelete(inc);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    private void loadPatients() {
        try {
            javafx.collections.ObservableList<com.abms.model.Patient> patients = javafx.collections.FXCollections.observableArrayList(patientRepository.getAllPatients());
            patientTable.setItems(patients);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadIncidents() {
        try {
            javafx.collections.ObservableList<com.abms.model.BiteIncident> incidents = javafx.collections.FXCollections.observableArrayList(incidentRepository.getAllIncidents());
            incidentTable.setItems(incidents);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleEdit(com.abms.model.Patient p) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/com/abms/ui/edit_patient_dialog.fxml"));
            javafx.scene.Parent root = loader.load();
            
            EditPatientDialogController controller = loader.getController();
            controller.setPatient(p);
            
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Edit Patient Details");
            stage.setScene(new javafx.scene.Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
            if (controller.isUpdated()) {
                loadPatients();
                loadIncidents(); // Update incident list as well since patient name might have changed
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not open edit dialog: " + e.getMessage());
        }
    }

    private void handleIncidentEdit(com.abms.model.BiteIncident inc) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/com/abms/ui/edit_incident_dialog.fxml"));
            javafx.scene.Parent root = loader.load();
            
            EditIncidentDialogController controller = loader.getController();
            controller.setIncident(inc);
            
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Edit Incident Details");
            stage.setScene(new javafx.scene.Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
            if (controller.isUpdated()) {
                loadIncidents();
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not open edit dialog: " + e.getMessage());
        }
    }

    private void handleDelete(com.abms.model.Patient p) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete patient " + p.getFirstName() + " " + p.getLastName() + "?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    patientRepository.deletePatient(p.getId());
                    String currentUser = (UserSession.getInstance() != null) ? UserSession.getInstance().getUsername() : "Unknown";
                    logRepository.log(currentUser, "Delete Patient", "Deleted patient: " + p.getFirstName() + " " + p.getLastName() + " (ID: " + p.getId() + ")");
                    loadPatients();
                    loadIncidents();
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete: " + e.getMessage());
                }
            }
        });
    }

    private void handleIncidentDelete(com.abms.model.BiteIncident inc) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete incident #" + inc.getId() + " for " + inc.getPatientName() + "?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    incidentRepository.deleteIncident(inc.getId());
                    String currentUser = (UserSession.getInstance() != null) ? UserSession.getInstance().getUsername() : "Unknown";
                    logRepository.log(currentUser, "Delete Incident", "Deleted incident #" + inc.getId() + " for " + inc.getPatientName());
                    loadIncidents();
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void handleCancelRegistration() {
        clearFields();
    }

    @FXML
    private void handleSave() {
        try {
            int age = Integer.parseInt(ageField.getText());
            String fName = firstNameField.getText();
            String lName = lastNameField.getText();
            
            patientService.registerPatientWithIncident(
                fName, lName, age, genderCombo.getValue(),
                addressArea.getText(), contactField.getText(),
                biteDatePicker.getValue(), animalTypeCombo.getValue(), biteSiteField.getText(),
                animalStatusCombo.getValue(), exposureTypeCombo.getValue(), remarksArea.getText()
            );
            
            String currentUser = (UserSession.getInstance() != null) ? UserSession.getInstance().getUsername() : "Unknown";
            logRepository.log(currentUser, "Register Patient", "Registered new patient: " + fName + " " + lName);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Patient and Incident recorded successfully. Vaccination schedule generated.");
            clearFields();
            loadPatients(); // Refresh the list after saving new patient
            loadIncidents(); // Refresh the incident list too
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
