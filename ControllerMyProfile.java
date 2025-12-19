package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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


public class ControllerMyProfile implements Initializable {

    @FXML
    private Button btnProfile;

    @FXML
    private Button btnUsers;

    @FXML
    private Button btnApplyLeave;

    @FXML
    private Button btnMyLeavesHistory;

    @FXML
    private Button btnLogOut;

    @FXML
    private Button btnDashboard;

    @FXML
    private Label employeeID;

    @FXML
    private Label firstName;

    @FXML
    private Label lastName;

    @FXML
    private Label email;

    @FXML
    private Label country;

    @FXML
    private Label city;

    @FXML
    private Label address;

    @FXML
    private Label gender;

    @FXML
    private Label department;

    @FXML
    private Label birthday;

    @FXML
    private Label mobileNumber;

    @FXML
    private Label password;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadUserProfile();
    }


    private void loadUserProfile() {
        String username = Session.getUsername();
        System.out.println("Logged-in username : "+username);
        String query = "SELECT * FROM employee WHERE username = ?";

        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement pst = connection.prepareStatement(query)) {

            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                employeeID.setText(rs.getString("employeeID"));
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

    @FXML
    private void loadScene(ActionEvent event, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            Logger.getLogger(ControllerUsers.class.getName()).log(Level.SEVERE, "Error loading scene", e);
        }
    }

    @FXML
    void LoadApplyLeavePage(ActionEvent event) {
        loadScene(event, "/sample/sampleApplyLeaveAbsence.fxml");
    }

    @FXML
    void LoadMyLeavesHistory(ActionEvent event) {
        loadScene(event, "/sample/sampleMyLeaveHistory.fxml");
    }

    @FXML
    void LoadProfilePage(ActionEvent event) {
        loadScene(event, "/sample/sampleMyProfile.fxml");
    }

    @FXML
    void LoadUsersPage(ActionEvent event) { loadScene(event,"/sample/sampleUsers.fxml"); }

    @FXML
    void LoadDashboard(ActionEvent event) {
        loadScene(event, "/sample/sampleDashboard.fxml");
    }

    @FXML
    void LoadLogOut(ActionEvent event) {
        loadScene(event, "/sample/sampleLoginPage.fxml");
    }

    public void loadUserData(String currentUsername) {

    }
}


