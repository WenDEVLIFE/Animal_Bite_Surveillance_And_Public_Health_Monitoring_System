package com.abms.ui;

import com.abms.model.User;
import com.abms.repository.UserRepository;
import com.animal_bite_surveillance_and_public_health_monitoring_system.animal_bite_surveillance_and_public_health_monitoring_system.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.abms.utils.UserSession;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import java.io.IOException;
import java.sql.SQLException;

public class UserManagementController {

    @FXML private TextField fullNameField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private Button saveButton;

    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Integer> idCol;
    @FXML private TableColumn<User, String> fullNameCol;
    @FXML private TableColumn<User, String> usernameCol;
    @FXML private TableColumn<User, String> roleCol;
    @FXML private TableColumn<User, Void> actionsCol;

    private final UserRepository userRepo = new UserRepository();
    private final com.abms.repository.ActivityLogRepository logRepo = new com.abms.repository.ActivityLogRepository();
    private User selectedUser = null;

    public void initialize() {
        roleComboBox.setItems(FXCollections.observableArrayList("ADMIN", "HEALTH_WORKER"));
        
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        fullNameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        
        setupActionsColumn();
        loadUsers();
    }

    private void loadUsers() {
        try {
            ObservableList<User> users = FXCollections.observableArrayList(userRepo.getAllUsers());
            userTable.setItems(users);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSave() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleComboBox.getValue();

        if (username.isEmpty() || role == null) {
            showAlert("Error", "Please fill in all required fields.");
            return;
        }

        try {
            String adminUser = (UserSession.getInstance() != null) ? UserSession.getInstance().getUsername() : "System";
            String fullName = fullNameField.getText();
            
            if (selectedUser == null) {
                userRepo.addUser(username, password, role, fullName);
                logRepo.log(adminUser, "Add User", "Created new user: " + username + " (" + fullName + ") with role: " + role);
                showAlert("Success", "User added successfully.");
            } else {
                String oldUsername = selectedUser.getUsername();
                selectedUser.setUsername(username);
                selectedUser.setFullName(fullName);
                if (!password.isEmpty()) {
                    selectedUser.setPassword(password);
                }
                selectedUser.setRole(role);
                userRepo.updateUser(selectedUser);
                logRepo.log(adminUser, "Update User", "Updated user: " + oldUsername + " (Role: " + role + ")");
                showAlert("Success", "User updated successfully.");
            }
            handleClear();
            loadUsers();
        } catch (SQLException e) {
            showAlert("Database Error", e.getMessage());
        }
    }

    @FXML
    private void handleClear() {
        fullNameField.clear();
        usernameField.clear();
        passwordField.clear();
        roleComboBox.setValue(null);
        selectedUser = null;
        saveButton.setText("Add User");
    }

    private void setupActionsColumn() {
        Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactory = param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final HBox pane = new HBox(5, editBtn, deleteBtn);

            {
                editBtn.getStyleClass().add("button-edit");
                deleteBtn.getStyleClass().add("button-delete");
                
                editBtn.setOnAction(event -> {
                    selectedUser = getTableView().getItems().get(getIndex());
                    fullNameField.setText(selectedUser.getFullName());
                    usernameField.setText(selectedUser.getUsername());
                    passwordField.setText(""); // Don't show password
                    roleComboBox.setValue(selectedUser.getRole());
                    saveButton.setText("Update User");
                });

                deleteBtn.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    if (confirmDelete(user.getUsername())) {
                        try {
                            userRepo.deleteUser(user.getId());
                            String adminUser = (UserSession.getInstance() != null) ? UserSession.getInstance().getUsername() : "System";
                            logRepo.log(adminUser, "Delete User", "Deleted user: " + user.getUsername() + " (ID: " + user.getId() + ")");
                            loadUsers();
                        } catch (SQLException e) {
                            showAlert("Error", "Could not delete user.");
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        };
        actionsCol.setCellFactory(cellFactory);
    }

    private boolean confirmDelete(String username) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete User: " + username);
        alert.setContentText("Are you sure you want to delete this user?");
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void goBack() throws IOException {
        App.setRoot("/com/abms/ui/dashboard.fxml");
    }
}
