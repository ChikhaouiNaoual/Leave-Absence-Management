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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ControllerAdminProfile implements Initializable {

        @FXML
        private Label adminID;

        @FXML
        private Label mobileNumber;

        @FXML
        private Label firstName;

        @FXML
        private Label birthday;

        @FXML
        private Label password;

        @FXML
        private Label email;

        @FXML
        private Label city;

        @FXML
        private Label country;

        @FXML
        private Label address;

        @FXML
        private Label department;

        @FXML
        private Label gender;

        @FXML
        private Label lastName;

        @FXML
        private Button btnAdminProfile;

        @FXML
        private Button btnAllUsers;

        @FXML
        private Button btnAddEmployee;

        @FXML
        private Button btnUsersLeavesHistory;

        @FXML
        private Button btnLogOut;

        @FXML
        private Button btnDashboard2;

        @FXML
        private Button btnAbsenceToday;


        @Override
        public void initialize(URL location, ResourceBundle resources) {
                loadAdminProfile();
        }
        private void loadAdminProfile() {
                String username = Session.getUsername();
                System.out.println("Logged-in username : "+username);
                String query = "SELECT * FROM admin WHERE username = ?";

                try (Connection connection = ConnectionDB.getConnection();
                     PreparedStatement pst = connection.prepareStatement(query)) {

                        pst.setString(1, username);
                        ResultSet rs = pst.executeQuery();

                        if (rs.next()) {
                                adminID.setText(rs.getString("adminID"));
                                firstName.setText(rs.getString("firstName"));
                                lastName.setText(rs.getString("lastName"));
                                email.setText(rs.getString("email"));
                                country.setText(rs.getString("country"));
                                city.setText(rs.getString("city"));
                                address.setText(rs.getString("address"));
                                gender.setText(rs.getString("gender"));
                                department.setText(rs.getString("department"));
                                birthday.setText(rs.getString("birthday"));
                                mobileNumber.setText(rs.getString("mobileNumber"));
                                password.setText("********");
                        } else {
                                System.out.println("No user found with username: " + username);
                        }

                } catch (SQLException e) {
                        System.err.println("Error loading user profile: " + e.getMessage());
                        e.printStackTrace();
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

        @FXML void LoadAdminProfile(ActionEvent event) { loadScene(event, "/sample/sampleAdminProfile.fxml"); }
        @FXML void LoadAllUsers(ActionEvent event) { loadScene(event, "/sample/sampleAllUsers.fxml"); }
        @FXML void LoadAddEmployee(ActionEvent event) { loadScene(event, "/sample/sampleAddEmployee.fxml"); }
        @FXML void LoadUsersLeavesHistory(ActionEvent event) { loadScene(event, "/sample/sampleUsersLeavesHistory.fxml"); }
        @FXML void LoadDashboard2(ActionEvent event) { loadScene(event, "/sample/sampleAdminDashboard.fxml"); }
        @FXML void LoadLogOut(ActionEvent event) { loadScene(event, "/sample/sampleLoginPage.fxml"); }
        @FXML void LoadAbsenceToday(ActionEvent event) { loadScene(event, "/sample/sampleAbsenceToday.fxml"); }

        private void showAlert(String title, String message) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();
        }
}


