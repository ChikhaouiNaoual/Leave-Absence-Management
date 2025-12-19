package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ControllerUpdateUsers implements Initializable {

    @FXML
    private Button btnUpdateUsers;

    @FXML
    private ComboBox<String> Department;

    @FXML
    private ComboBox<String> Gender;

    @FXML
    private TextField ID;

    @FXML
    private TextField Name;

    @FXML
    private TextField Email;

    @FXML
    private TextField Country;

    @FXML
    private TextField User_Name;

    @FXML
    private Button btnBackToUsersTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupComboBoxes();
    }

    private void setupComboBoxes() {
        Gender.getItems().addAll("Male", "Female");
        Department.getItems().addAll("IT", "Human Resources", "Finance", "Marketing", "Operations");
    }

    public void setUserData(AllUsers selectedUser) {
        ID.setText(selectedUser.getID());
        User_Name.setText(selectedUser.getUser_Name());
        Name.setText(selectedUser.getName());
        Email.setText(selectedUser.getEmail());
        Country.setText(selectedUser.getCountry());
        Gender.setValue(selectedUser.getGender());
        Department.setValue(selectedUser.getDepartment());
    }

    @FXML
    void OnActionUpdateUsers(ActionEvent event) {
        try (Connection connection = ConnectionDB.getConnection()) {
            String sql = "UPDATE all_users SET User_Name=?, Name=?, Email=?, Country=?, Gender=?, Department=? WHERE ID=?";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, User_Name.getText());
            ps.setString(2, Name.getText());
            ps.setString(3, Email.getText());
            ps.setString(4, Country.getText());
            ps.setString(5, Gender.getValue());
            ps.setString(6, Department.getValue());
            ps.setString(7, ID.getText());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "User updated successfully.");
            } else {
                showAlert(Alert.AlertType.WARNING, "No Change", "No user was updated.");
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update user: " + e.getMessage());
        }
    }
    private void loadScene(ActionEvent event, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            Logger.getLogger(ControllerAllUsers.class.getName()).log(Level.SEVERE, "Error loading scene", e);
            showAlert("Navigation Error", "Failed to load page: " + e.getMessage());
        }
    }

    private void showAlert(String navigation_error, String s) {
    }

    @FXML
    void OnActionBackToUsersTable(ActionEvent event) {
        loadScene(event, "/sample/sampleAllUsers.fxml");

    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}