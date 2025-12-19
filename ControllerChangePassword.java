package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ControllerChangePassword {

    @FXML private Button btnConfirm;
    @FXML private TextField NewPassword;
    @FXML private TextField OldPassword;
    @FXML private Button btnBack;
    @FXML
    private TextField Username;

    private String currentUsername;  // Holds the username of the logged-in user
    private String username;
    // This method should be called from the Dashboard or Login Controller
    public void setUsername(String username) {
        this.currentUsername = username;
    }

    @FXML
    void OnActionConfirm(ActionEvent event) {
        if (OldPassword.getText().isEmpty() || NewPassword.getText().isEmpty()) {
            showAlert("Validation Error", "Please enter both old and new passwords.");
            return;
        }

        String updateQuery = "UPDATE login SET Password = ? WHERE UserName = ? AND Password = ?";
        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement ps = connection.prepareStatement(updateQuery)) {

            ps.setString(1, NewPassword.getText());
            ps.setString(2, currentUsername);
            ps.setString(3, OldPassword.getText());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                showAlert("Success", "Password updated successfully.");
                OldPassword.clear();
                NewPassword.clear();
            } else {
                showAlert("Failure", "Old password is incorrect.");
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to update password: " + e.getMessage());
        }
    }

    @FXML
    void onActionBack(ActionEvent event) throws IOException {
        loadPage(event, "/sample/sampleDashboard.fxml");
    }

    private void loadPage(ActionEvent event, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}