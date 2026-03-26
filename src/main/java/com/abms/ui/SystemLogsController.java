package com.abms.ui;

import com.abms.model.ActivityLog;
import com.abms.repository.ActivityLogRepository;
import com.animal_bite_surveillance_and_public_health_monitoring_system.animal_bite_surveillance_and_public_health_monitoring_system.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SystemLogsController {

    @FXML private TableView<ActivityLog> logsTable;
    @FXML private TableColumn<ActivityLog, LocalDateTime> timestampCol;
    @FXML private TableColumn<ActivityLog, String> userCol;
    @FXML private TableColumn<ActivityLog, String> actionCol;
    @FXML private TableColumn<ActivityLog, String> detailsCol;

    private final ActivityLogRepository logRepo = new ActivityLogRepository();

    @FXML
    public void initialize() {
        timestampCol.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        userCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        actionCol.setCellValueFactory(new PropertyValueFactory<>("action"));
        detailsCol.setCellValueFactory(new PropertyValueFactory<>("details"));

        // Custom cell factory for timestamp formatting
        timestampCol.setCellFactory(column -> new javafx.scene.control.TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                }
            }
        });

        loadLogs();
    }

    @FXML
    public void loadLogs() {
        try {
            ObservableList<ActivityLog> logs = FXCollections.observableArrayList(logRepo.getAllLogs());
            logsTable.setItems(logs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goBack() throws IOException {
        App.setRoot("/com/abms/ui/dashboard.fxml");
    }
}
