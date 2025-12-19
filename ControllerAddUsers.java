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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ControllerAddUsers implements Initializable {


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
        private Button btnAddUsers;

        @FXML
        private ComboBox<String> Department;
        @FXML
        private ComboBox<String> Gender;

        @FXML
        private Button btnBackToUsersTable;



    @Override
    public void initialize(URL location, ResourceBundle resources)  {
        setupComboBoxes();
    }
        private void setupComboBoxes() {
            Gender.getItems().addAll("Male", "Female");
            Department.getItems().addAll("IT", "Human Resources", "Finance", "Marketing", "Operations");
        }
    @FXML

    void OnActionAddUsers(ActionEvent event) {
        if (validateInput()) {
        String query = "INSERT INTO all_users (ID, User_Name, Name, Email, Country, Gender, Department) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, ID.getText());
            ps.setString(2, User_Name.getText());
            ps.setString(3, Name.getText());
            ps.setString(4, Email.getText());
            ps.setString(5, Country.getText());
            ps.setString(6, Gender.getValue());
            ps.setString(7, Department.getValue());

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
        if (ID.getText().isEmpty() || User_Name.getText().isEmpty() ||
                Name.getText().isEmpty() || Email.getText().isEmpty() ||
                Gender.getValue() == null || Department.getValue() == null) {
            showAlert("Validation Error", "Please fill all required fields");
            return false;
        }
        return true;
    }

    private void clearFields() {
        ID.clear();
        User_Name.clear();
        Name.clear();
        Email.clear();
        Country.clear();
        Gender.getSelectionModel().clearSelection();
        Department.getSelectionModel().clearSelection();

    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
    @FXML
    void OnActionBackToUsersTable(ActionEvent event) {loadScene(event, "/sample/sampleAllUsers.fxml");}

    }




