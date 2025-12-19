package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ControllerDashboard {

        @FXML private Button btnProfile, btnUsers, btnApplyLeave, btnMyLeavesHistory, btnLogOut, btnDashboard;
        @FXML private Text text;
        @FXML private ProgressBar StaffProgressBar, DepartmentProgressBar, leaveProgress;
        @FXML private Label staffPercentageLabel, departmentPercentageLabel, leavePercentageLabel, clockLabel;

        @FXML
        private Button btnSetting;

        private String currentUsername;


        public void setUsername(String username) {
                this.currentUsername = username;
                System.out.println("Logged  in as:" +username);

        }

        @FXML
        void LoadApplyLeavePage(ActionEvent event) throws IOException {
                loadPage(event, "/sample/sampleApplyLeaveAbsence.fxml");
        }

        @FXML
        void LoadMyLeavesHistory(ActionEvent event) throws IOException {
                loadPage(event, "/sample/sampleMyLeaveHistory.fxml");
        }

        @FXML
        void LoadProfilePage(ActionEvent event) throws IOException {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/sampleMyProfile.fxml"));
                Parent root = loader.load();
                ControllerMyProfile profileController = loader.getController();
                profileController.loadUserData(currentUsername); // Make sure this method exists
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
        }

        @FXML
        void handelSetting(ActionEvent event) throws IOException {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/sampleChangePassword.fxml"));
                Parent root = loader.load();

                // Pass username to the change password controller
                ControllerChangePassword controller = loader.getController();
                controller.setUsername(currentUsername);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
        }



        @FXML
        void LoadUsersPage(ActionEvent event) throws IOException {
                loadPage(event, "/sample/sampleUsers.fxml");
        }

        @FXML
        void LoadDashboard(ActionEvent event) throws IOException {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/sampleDashboard.fxml"));
                Parent root = loader.load();
                ControllerDashboard dashboardController = loader.getController();
                dashboardController.setUsername(currentUsername);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
        }

        @FXML
        void LoadLogOut(ActionEvent event) throws IOException {
                loadPage(event, "/sample/sampleLoginPage.fxml");
        }

        private void loadPage(ActionEvent event, String fxmlPath) throws IOException {
                Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
        }
        public void loadDashboardStats() {
                try (Connection conn = ConnectionDB.getConnection()) {

                        // Count staff
                        PreparedStatement staffStmt = conn.prepareStatement("SELECT COUNT(*) FROM employee");
                        ResultSet rsStaff = staffStmt.executeQuery();
                        rsStaff.next();
                        int staffCount = rsStaff.getInt(1);

                        // Count departments
                        PreparedStatement deptStmt = conn.prepareStatement("SELECT COUNT(DISTINCT department) FROM employee");
                        ResultSet rsDept = deptStmt.executeQuery();
                        rsDept.next();
                        int deptCount = rsDept.getInt(1);

                        // Count leave requests
                        PreparedStatement leaveStmt = conn.prepareStatement("SELECT COUNT(*) FROM leaves");
                        ResultSet rsLeave = leaveStmt.executeQuery();
                        rsLeave.next();
                        int leaveCount = rsLeave.getInt(1);

                        // Set progress and labels (dummy max values: adjust as needed)
                        staffPercentageLabel.setText(staffCount + " Employees");
                        StaffProgressBar.setProgress(Math.min(1.0, staffCount / 100.0)); // 100 = arbitrary total

                        departmentPercentageLabel.setText(deptCount + " Departments");
                        DepartmentProgressBar.setProgress(Math.min(1.0, deptCount / 10.0)); // 10 = arbitrary total

                        leavePercentageLabel.setText(leaveCount + " Requests");
                        leaveProgress.setProgress(Math.min(1.0, leaveCount / 100.0));

                } catch (SQLException e) {
                        e.printStackTrace();
                }
        }



        @FXML
        public void initialize() {
                loadDashboardStats();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                Timeline clock = new Timeline(
                        new KeyFrame(Duration.ZERO, e -> clockLabel.setText(LocalTime.now().format(formatter))),
                        new KeyFrame(Duration.seconds(1))
                );
                clock.setCycleCount(Timeline.INDEFINITE);
                clock.play();
        }
}