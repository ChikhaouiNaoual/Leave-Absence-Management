package sample;

import sample.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ControllerLoginPage {

    @FXML
    private TextField txtusername;

    @FXML
    private PasswordField txtpassword;

    @FXML
    private Button login, logout;

    @FXML
    private Hyperlink hyperlink;


    @FXML
    void OnActionLogin(ActionEvent event){
        String username = txtusername.getText().trim();
        String password = txtpassword.getText().trim();


        if (username.isEmpty() || password.isEmpty()) {
            Notification("Error", "Validation Error", "Username and password fields cannot be empty.", Alert.AlertType.INFORMATION);
            return;
        }


        if (username.equals("admin") && password.equals("admin1")) {
            Notification("Success", "Login Successful", "Welcome Admin!", Alert.AlertType.INFORMATION);
            Session.loggedInUsername = username;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/sampleAdminDashboard.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                Notification("Error", "Navigation Error", "Unable to load the Admin Dashboard.", Alert.AlertType.INFORMATION);
            }
            return;
        }


        try {
            String query = "SELECT * FROM login WHERE username = ? AND password = ?";
            try (Connection connection = ConnectionDB.getConnection();
                 PreparedStatement ps = connection.prepareStatement(query)) {

                ps.setString(1, username);
                ps.setString(2, password);
                ResultSet r = ps.executeQuery();

                if (r.next()) {
                    Session.loggedInUsername = username;
                    Session.setUsername(username);
                    Notification("Success", "Login Successful", "Welcome " + username + "!", Alert.AlertType.INFORMATION);
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/sampleDashboard.fxml"));
                        Parent root = loader.load();
                        ControllerDashboard dashboardController = loader.getController();
                        dashboardController.setUsername(username);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Notification("Error", "Navigation Error", "Unable to load the Dashboard.", Alert.AlertType.INFORMATION);
                    }
                } else {
                    Notification("Error", "Login Failed", "Invalid username or password.", Alert.AlertType.INFORMATION);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Notification("Error", "Database Error", "A database error occurred.", Alert.AlertType.INFORMATION);
        }
    }

    private void navigateToAdminDashboard(ActionEvent event)throws IOException {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/sampleAdminDashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            Notification("Error", "Navigation Error", "Unable to load the admin dashboard page.", Alert.AlertType.INFORMATION);
        }
    }


    private void navigateToDashboard(ActionEvent event) throws IOException{
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/sampleDashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Notification("Error", "Navigation Error", "Unable to load the dashboard page.", Alert.AlertType.INFORMATION);
        }
    }


    private boolean IsEmpty() {
        return txtusername.getText().trim().isEmpty() || txtpassword.getText().trim().isEmpty();
    }


    private void Notification(String title, String header, String message, Alert.AlertType information) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    void OnActionHyperlink(ActionEvent event) {
        Notification("Info", "Forgot Password",
                "Please contact your system administrator.", Alert.AlertType.INFORMATION);
    }

}