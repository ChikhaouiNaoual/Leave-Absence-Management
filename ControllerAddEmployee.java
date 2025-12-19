package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ControllerAddEmployee implements Initializable {


    @FXML private TextField employeeID;
    @FXML private TextField firstName;
    @FXML private TextField lastName;
    @FXML private TextField email;
    @FXML private TextField country;
    @FXML private TextField city;
    @FXML private TextField address;
    @FXML private ComboBox<String> genderComboBox;
    @FXML private ComboBox<String> departmentComboBox;
    @FXML private TextField birthday;
    @FXML private TextField mobileNumber;
    @FXML private TextField password;
    @FXML private Button btnAdd;
    @FXML private Button btnAbsenceToday;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupComboBoxes();
    }

    private void setupComboBoxes() {
        genderComboBox.getItems().addAll("Male", "Female");
        departmentComboBox.getItems().addAll("IT", "Human Resources", "Finance", "Marketing", "Operations");
    }

    @FXML
    private void OnActionAdd(ActionEvent event) {
        if (validateInput()) {
            String query = "INSERT INTO employee (employeeID, firstName, lastName, email, country, city, " +
                    "address, gender, department, birthday, mobileNumber, password) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection connection = ConnectionDB.getConnection();
                 PreparedStatement ps = connection.prepareStatement(query)) {

                ps.setString(1, employeeID.getText());
                ps.setString(2, firstName.getText());
                ps.setString(3, lastName.getText());
                ps.setString(4, email.getText());
                ps.setString(5, country.getText());
                ps.setString(6, city.getText());
                ps.setString(7, address.getText());
                ps.setString(8, genderComboBox.getValue());
                ps.setString(9, departmentComboBox.getValue());
                ps.setString(10, birthday.getText());
                ps.setString(11, mobileNumber.getText());
                ps.setString(12, password.getText());

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

    private boolean validateInput() {
        if (employeeID.getText().isEmpty() || firstName.getText().isEmpty() ||
                lastName.getText().isEmpty() || email.getText().isEmpty() ||
                genderComboBox.getValue() == null || departmentComboBox.getValue() == null) {
            showAlert("Validation Error", "Please fill all required fields");
            return false;
        }
        return true;
    }

    private void clearFields() {
        employeeID.clear();
        firstName.clear();
        lastName.clear();
        email.clear();
        country.clear();
        city.clear();
        address.clear();
        genderComboBox.getSelectionModel().clearSelection();
        departmentComboBox.getSelectionModel().clearSelection();
        birthday.clear();
        mobileNumber.clear();
        password.clear();
}

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private void loadScene(ActionEvent event, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            Logger.getLogger(ControllerUsers.class.getName()).log(Level.SEVERE, "Error loading scene", e);
            showAlert("Navigation Error", "Failed to load page: " + e.getMessage());
        }
    }

    @FXML private void LoadDashboard2(ActionEvent event) {
        loadScene(event, "/sample/sampleAdminDashboard.fxml");
    }

    @FXML private void LoadLogOut(ActionEvent event) {
        loadScene(event, "/sample/sampleLoginPage.fxml");
    }

    @FXML void LoadAbsenceToday(ActionEvent event) { loadScene(event, "/sample/sampleAbsenceToday.fxml"); }

    @FXML private void LoadAdminProfile(ActionEvent event) {
        loadScene(event, "/sample/sampleAdminProfile.fxml");
    }

    @FXML private void LoadAllUsers(ActionEvent event) {
        loadScene(event, "/sample/sampleAllUsers.fxml");
    }

    @FXML private void LoadUsersLeavesHistory(ActionEvent event) { loadScene(event, "/sample/sampleUsersLeavesHistory.fxml"); }

    @FXML private void LoadAddEmployee(ActionEvent event) {
        loadScene(event, "/sample/sampleAddEmployee.fxml");
    }
}