package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ControllerAdminDashboard {


    @FXML
    private Button btnAddEmployee;
    @FXML
    private Button btnAdminProfile;
    @FXML
    private Button btnAllUsers;
    @FXML
    private Button btnLogOutAdmin;
    @FXML
    private Button btnDashboard2;
    @FXML
    private Button btnUsersLeavesHistory;

    @FXML
    private Label totalDepartmentLabel;
    @FXML
    private Label totalEmployeeLabel;
    @FXML
    private Pane paneLeaveRequest;
    @FXML
    private Pane paneEmployee;
    @FXML
    private Pane paneDepartment;
    @FXML
    private Label totalLeavesLabel;
    @FXML
    private LineChart<String, Number> lineChart;

    @FXML
    private Label labelClock;

    @FXML
    private Button btnAbsenceToday;

    @FXML
    private Button btnSetting;


    @FXML
    public void initialize() {
        totalEmployeeLabel.setText(String.valueOf(getTotalEmployee()));
        totalDepartmentLabel.setText(String.valueOf(getTotalDepartment()));
        totalLeavesLabel.setText(String.valueOf(getTotalLeaves()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        Timeline clock = new Timeline(
                new KeyFrame(Duration.ZERO, e -> labelClock.setText(LocalTime.now().format(formatter))),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();


        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("This Week");
        series.getData().add(new XYChart.Data<>("Mon", 4000));
        series.getData().add(new XYChart.Data<>("Tue", 900));
        series.getData().add(new XYChart.Data<>("Wed", 1300));
        series.getData().add(new XYChart.Data<>("Thu", 600));
        series.getData().add(new XYChart.Data<>("Fri", 1000));
        series.getData().add(new XYChart.Data<>("Sat", 800));
        series.getData().add(new XYChart.Data<>("Sun", 7000));

        lineChart.getData().add(series);

    }

    @FXML
    private int getTotalLeaves() {
        String query = "SELECT COUNT(*) FROM leaves";
        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error in getTotalLeaves: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    @FXML
    private int getTotalDepartment() {
        String query = "SELECT COUNT(DISTINCT Department) FROM users";
        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error in getTotalDepartment: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    @FXML
    private int getTotalEmployee() {
        String query = "SELECT COUNT(*) FROM users";
        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error in getTotalEmployee: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    @FXML
    void handelSetting(ActionEvent event)throws IOException {
        loadScene(event, "/sample/sampleSetting.fxml");

    }

    @FXML
    void LoadAddEmployee(ActionEvent event) throws IOException {
        loadScene(event, "/sample/sampleAddEmployee.fxml");
    }

    @FXML
    void LoadDashboard2(ActionEvent event) throws IOException {
        loadScene(event, "/sample/sampleAdminDashboard.fxml");
    }

    @FXML
    void LoadAddEmployee(MouseEvent event) throws IOException {
        loadScene(event, "/sample/AddEmployee.fxml");
    }

    @FXML
    void LoadUsersLeavesHistory(ActionEvent event) throws IOException {
        loadScene(event, "/sample/sampleUsersLeavesHistory.fxml");
    }

    @FXML
    void LoadLogOut(ActionEvent event) throws IOException {
        loadScene(event, "/sample/sampleLoginPage.fxml");
    }

    @FXML
    void LoadAdminProfile(ActionEvent event) throws IOException {
        loadScene(event, "/sample/sampleAdminProfile.fxml");
    }

    @FXML
    void LoadAllUsers(ActionEvent event) throws IOException {
        URL resource = getClass().getResource("/sample/sampleAllUsers.fxml");
        if (resource == null) {
            System.err.println("FXML file not found: /sample/sampleAllUsers.fxml");
            return;
        }
        loadScene(event, resource);
    }
    @FXML
    void LoadAbsenceToday(ActionEvent event) throws IOException {
        loadScene(event, "/sample/sampleAbsenceToday.fxml");
    }


    private void loadScene(ActionEvent event, String fxmlPath) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void loadScene(MouseEvent event, String fxmlPath) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void loadScene(ActionEvent event, URL resource) throws IOException {
        Parent root = FXMLLoader.load(resource);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
