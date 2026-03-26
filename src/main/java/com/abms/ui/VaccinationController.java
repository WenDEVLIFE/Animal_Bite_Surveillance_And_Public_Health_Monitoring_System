package com.abms.ui;

import com.animal_bite_surveillance_and_public_health_monitoring_system.animal_bite_surveillance_and_public_health_monitoring_system.App;
import com.abms.service.VaccinationRecord;
import com.abms.repository.VaccinationRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;

public class VaccinationController {

    @FXML private TableView<VaccinationRecord> vaccinationTable;
    @FXML private TableColumn<VaccinationRecord, Integer> idCol;
    @FXML private TableColumn<VaccinationRecord, String> patientCol;
    @FXML private TableColumn<VaccinationRecord, Integer> doseCol;
    @FXML private TableColumn<VaccinationRecord, String> dateCol;
    @FXML private TableColumn<VaccinationRecord, String> statusCol;
    @FXML private ComboBox<String> statusFilter;

    private final VaccinationRepository repo;

    public VaccinationController() {
        this.repo = new VaccinationRepository();
    }

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        patientCol.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        doseCol.setCellValueFactory(new PropertyValueFactory<>("doseNumber"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("scheduledDate"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        statusFilter.getItems().addAll("All", "Pending", "Completed");
        statusFilter.setValue("Pending"); // Default to pending as before
        
        loadData();
    }

    @FXML
    private void handleFilterChange() {
        loadData();
    }

    private void loadData() {
        try {
            String filter = statusFilter.getValue();
            ObservableList<VaccinationRecord> data;
            if ("All".equals(filter)) {
                data = FXCollections.observableArrayList(repo.getAllVaccinations());
            } else {
                data = FXCollections.observableArrayList(repo.getVaccinationsByStatus(filter));
            }
            vaccinationTable.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void markCompleted() {
        VaccinationRecord selected = vaccinationTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                repo.updateVaccinationStatus(selected.getId(), Date.valueOf(LocalDate.now()), "Completed", 1); // adminId=1
                loadData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void goBack() throws IOException {
        App.setRoot("/com/abms/ui/dashboard.fxml");
    }
}
