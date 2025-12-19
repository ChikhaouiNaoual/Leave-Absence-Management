package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class ControllerUsersLeavesHistory implements Initializable {

    Connection connection = null;
    PreparedStatement st = null;
    ResultSet rs = null;

    @FXML private TableView<MyLeaveTable> leavesHistoryTable;
    @FXML private TableColumn<MyLeaveTable, Integer> colid;
    @FXML private TableColumn<MyLeaveTable, String> colUserName;
    @FXML private TableColumn<MyLeaveTable, String> colrequest_type;
    @FXML private TableColumn<MyLeaveTable, String> colleave_type;
    @FXML private TableColumn<MyLeaveTable, String> colfrom_date;
    @FXML private TableColumn<MyLeaveTable, String> colto_date;
    @FXML private TableColumn<MyLeaveTable, String> colreason;
    @FXML private TableColumn<MyLeaveTable, String> colstatus;

    @FXML
    private Button btnApproved;

    @FXML
    private Button btnRejected;
    @FXML
    private Button btnAbsenceToday;

    private ObservableList<MyLeaveTable> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configureTable();
        loadData();
    }

    private void configureTable() {
        colid.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colUserName.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUserName()));
        colrequest_type.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getRequest_type()));
        colleave_type.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getLeave_type()));
        colfrom_date.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFrom_date()));
        colto_date.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTo_date()));
        colreason.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getReason()));
        colstatus.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus()));

        leavesHistoryTable.setItems(data);
        colstatus.setCellFactory(column -> new TableCell<MyLeaveTable, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if ("Approved".equalsIgnoreCase(item)) {
                        setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                    } else if ("Rejected".equalsIgnoreCase(item)) {
                        setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
                    }
                }
            }
        });
    }

    private void loadData() {
        data.clear();
        try {
            connection = ConnectionDB.getConnection();
            if (connection == null || connection.isClosed()) {
                showAlert("Connection Error", "Database connection is closed.");
                return;
            }

            String query = "SELECT id, username, request_type, leave_type, from_date, to_date, reason ,status FROM leaves";
            st = connection.prepareStatement(query);
            rs = st.executeQuery();

            while (rs.next()) {
                data.add(new MyLeaveTable(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("request_type"),
                        rs.getString("leave_type"),
                        rs.getString("from_date"),
                        rs.getString("to_date"),
                        rs.getString("reason"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load data: " + e.getMessage());
        }
    }

    @FXML
    void LoadApproved(ActionEvent event) {
        updateStatus("Approved");
    }

    @FXML
    void LoadRejected(ActionEvent event) {
        updateStatus("Rejected");
    }

    private void updateStatus(String newStatus) {
        MyLeaveTable selected = leavesHistoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a leave request.");
            return;
        }

        try {
            connection = ConnectionDB.getConnection();
            String updateQuery = "UPDATE leaves SET status = ? WHERE id = ?";
            st = connection.prepareStatement(updateQuery);
            st.setString(1, newStatus);
            st.setInt(2, selected.getId());
            int rows = st.executeUpdate();

            if (rows > 0) {
                selected.setStatus(newStatus);
                leavesHistoryTable.refresh();
                showAlert("Success", "Status updated to: " + newStatus);
            }

        } catch (SQLException e) {
            showAlert("Error", "Failed to update status: " + e.getMessage());
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