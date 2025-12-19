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

public class ControllerSetting {

    @FXML private Button btnAdd;
    @FXML private Button btnConfirm;
    @FXML private Button btnBack;

    @FXML private TextField UserId;
    @FXML private TextField UserName;
    @FXML private TextField Password;

    @FXML private TextField OldPassword;
    @FXML private TextField NewPassword;

    @FXML
    void OnActionAdd(ActionEvent event) {
        if (validateInput()) {
            String query = "INSERT INTO login (UserId, UserName, Password) VALUES (?, ?, ?)";

            try (Connection connection = ConnectionDB.getConnection();
                 PreparedStatement ps = connection.prepareStatement(query)) {

                ps.setString(1, UserId.getText());
                ps.setString(2, UserName.getText());
                ps.setString(3, Password.getText());

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    showAlert("Success", "Employee added successfully");
                    clearFields();
                }
            } catch (SQLException e) {
                showAlert("Database Error", "Failed to add employee: " + e.getMessage());
            }
        }
    }

    @FXML
    void OnActionConfirm(ActionEvent event) {

    }

    @FXML
    void onActionBack(ActionEvent event) throws IOException {
        loadScene(event, "/sample/sampleAdminDashboard.fxml");
    }

    private boolean validateInput() {
        if (UserId.getText().isEmpty() || UserName.getText().isEmpty() || Password.getText().isEmpty()) {
            showAlert("Validation Error", "Please fill all required fields");
            return false;
        }
        return true;
    }

    private void clearFields() {
        UserId.clear();
        UserName.clear();
        Password.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadScene(ActionEvent event, String fxmlPath) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}