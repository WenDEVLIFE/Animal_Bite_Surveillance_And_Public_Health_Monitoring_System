package com.abms.ui;

import com.animal_bite_surveillance_and_public_health_monitoring_system.animal_bite_surveillance_and_public_health_monitoring_system.App;
import com.abms.service.AuthenticationService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;
    @FXML private ImageView logoImageView;

    private final AuthenticationService authService;

    public LoginController() {
        this.authService = new AuthenticationService();
    }

    public void initialize() {
        // Load logo image
        try {
            String path = "/images/animal.png"; // matches target/classes/images/logo.jpg
            var resource = getClass().getResource(path);
            if (resource == null) {
                System.err.println("LOGO ERROR: Resource not found at " + path);
                return;
            }
            if (logoImageView == null) {
                System.err.println("LOGO ERROR: logoImageView is null (check fx:id in FXML)");
                return;
            }

            // load synchronously so width/height are known immediately
            Image logo = new Image(resource.toExternalForm(), false);
            System.out.println("LOGO size: " + logo.getWidth() + "x" + logo.getHeight());

            logoImageView.setImage(logo);
            logoImageView.setPreserveRatio(true);
            logoImageView.setSmooth(true);
            logoImageView.setCache(true);
        } catch (Exception e) {
            System.err.println("LOGO ERROR: Exception loading logo: " + e.getMessage());
            e.printStackTrace();
        }
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
