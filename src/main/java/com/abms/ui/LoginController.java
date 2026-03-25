package com.abms.ui;

import com.animal_bite_surveillance_and_public_health_monitoring_system.animal_bite_surveillance_and_public_health_monitoring_system.App;
import com.abms.service.AuthenticationService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;

    private final AuthenticationService authService;

    public LoginController() {
        this.authService = new AuthenticationService();
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (authService.login(username, password)) {
            try {
                String role = authService.getUserRole(username);
                System.out.println("Login successful! Role: " + role);
                
                if (role != null) {
                    // Role-based routing
                    if ("ADMIN".equals(role)) {
                        App.setRoot("/com/abms/ui/dashboard.fxml"); 
                    } else if ("HEALTH_WORKER".equals(role)) {
                        App.setRoot("/com/abms/ui/dashboard.fxml"); 
                    } else {
                        App.setRoot("/com/abms/ui/dashboard.fxml"); 
                    }
                } else {
                    errorLabel.setText("User role not recognized.");
                }
            } catch (Exception e) {
                errorLabel.setText("Error loading dashboard.");
                e.printStackTrace();
            }
        } else {
            errorLabel.setText("Invalid username or password.");
        }
    }
}
