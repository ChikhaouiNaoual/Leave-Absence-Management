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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ControllerMyLeaveHistory implements Initializable {

    Connection connection = null;
    PreparedStatement st = null;
    ResultSet rs = null;

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
    private Button btnCancel;

    @FXML
    private TableView<MyLeaveTable> leavesHistoryTable;

    @FXML
    private TableColumn<MyLeaveTable, Integer> colid;

    @FXML
    private TableColumn<MyLeaveTable, String> colUserName;

    @FXML
    private TableColumn<MyLeaveTable, String> colrequest_type;

    @FXML
    private TableColumn<MyLeaveTable, String> colleave_type;

    @FXML
    private TableColumn<MyLeaveTable, String> colfrom_date;

    @FXML
    private TableColumn<MyLeaveTable, String> colto_date;

    @FXML
    private TableColumn<MyLeaveTable, String> colreason;

    @FXML
    private TableColumn<MyLeaveTable, String> colstatus;

    private ObservableList<MyLeaveTable> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configureTable();
        loadData();

    }

    private void loadData() {
        data.clear();
        try {
            connection = ConnectionDB.getConnection();
            if (connection == null || connection.isClosed()) {
                showAlert("Connection Error", "Database connection is closed.");
                return;
            }
            String currentUsername = Session.getUsername();
            if(currentUsername == null || currentUsername.isEmpty()){
                showAlert("User Error", "No Logged-in username found .");
                return;
            }
            String query = "SELECT id, username, request_type, leave_type, from_date, to_date, reason ,status FROM leaves WHERE username = ?";
            st = connection.prepareStatement(query);
            st.setString(1,currentUsername);
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

    @FXML
    void LoadCancel(ActionEvent event) {
        MyLeaveTable selectedRequest = leavesHistoryTable.getSelectionModel().getSelectedItem();

        if (selectedRequest == null) {
            showAlert("Selection Error", "Please select a request to cancel.");
            return;
        }

        String status = selectedRequest.getStatus();
        if ("Approved".equalsIgnoreCase(status) || "Rejected".equalsIgnoreCase(status)) {
            showAlert("Cancel Error", "You can't cancel a request that has already been " + status.toLowerCase() + ".");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Cancel Confirmation");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to cancel this leave request?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    connection = ConnectionDB.getConnection();
                    String deleteQuery = "DELETE FROM leaves WHERE id = ?";
                    st = connection.prepareStatement(deleteQuery);
                    st.setInt(1, selectedRequest.getId());

                    int rowsAffected = st.executeUpdate();
                    if (rowsAffected > 0) {
                        showAlert("Success", "Leave request canceled successfully.");
                        loadData(); // Refresh table
                    } else {
                        showAlert("Error", "Failed to cancel the request.");
                    }

                } catch (SQLException e) {
                    showAlert("Database Error", "Failed to cancel request: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

    }

    @FXML
    void LoadApplyLeavePage(ActionEvent event)throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sample/sampleApplyLeaveAbsence.fxml"));
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(root));
    }

    @FXML
    void LoadMyLeavesHistory(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sample/sampleMyLeaveHistory.fxml"));
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(root));
    }

    @FXML
    void LoadProfilePage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sample/sampleMyProfile.fxml"));
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(root));
    }

    @FXML
    void LoadUsersPage(ActionEvent event)  throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/sample/sampleUsers.fxml"));
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(root));
    }

    @FXML
    void LoadDashboard(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sample/sampleDashboard.fxml"));
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(root));
    }

    @FXML
    void LoadLogOut(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sample/sampleLoginPage.fxml"));
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(root));

    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}

