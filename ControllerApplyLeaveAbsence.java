
package sample;
import javafx.scene.paint.Color;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ControllerApplyLeaveAbsence {

    @FXML
    private Label fromdate;

    @FXML
    private Label todate;

    @FXML
    private DatePicker FromDate;

    @FXML
    private DatePicker ToDate;

    @FXML
    private ComboBox<String> requestTypeComboBox;

    @FXML
    private ComboBox<String> leaveTypeComboBox;

    @FXML
    private TextArea reasonTextArea;

    @FXML
    private Label messageLabel;

    @FXML
    private Label leaveTypeLabel1;

    @FXML
    private Button btnLogOut;

    @FXML
    private Button btnApply;

    @FXML
    public void initialize() {
        requestTypeComboBox.getItems().addAll("Leave", "Absence");
        leaveTypeComboBox.getItems().addAll( "Annual Leave", "Paid Leave", "Medical Leave","Study Leave","Compassionate Leave");
        leaveTypeComboBox.setDisable(true);
        leaveTypeComboBox.setVisible(false);
        leaveTypeLabel1.setVisible(false);
        requestTypeComboBox.setOnAction(event -> {
            String selected = requestTypeComboBox.getValue();
            if ("Leave".equals(selected)) {
                leaveTypeComboBox.setDisable(false);
                leaveTypeComboBox.setVisible(true);
                leaveTypeLabel1.setVisible(true);
            } else if ("Absence".equals(selected)) {
                leaveTypeComboBox.setDisable(true);
                leaveTypeComboBox.setVisible(false);
                leaveTypeLabel1.setVisible(false);
            }
        });
    }
    @FXML
    private void OnActionApplyLeavePage() {

        clearMessages();

        boolean valid = true;


        if (FromDate.getValue() == null) {
            FromDate.setStyle("-fx-border-color: red; -fx-border-width: 2;");
        } else {
            FromDate.setStyle("-fx-border-color: #0077b6; -fx-border-width: 1;");
        }

        if (ToDate.getValue() == null) {
            ToDate.setStyle("-fx-border-color: red; -fx-border-width: 2;");
        } else {
            ToDate.setStyle("-fx-border-color: #0077b6; -fx-border-width: 1;");
        }
        if(FromDate.getValue() != null && ToDate.getValue() != null){
            if(FromDate.getValue().isAfter(ToDate.getValue())){
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("From Date Can Not Be After To Date");
            return;
            }
        }


        if (reasonTextArea.getText() == null || reasonTextArea.getText().trim().isEmpty()) {
            messageLabel.setTextFill(javafx.scene.paint.Color.RED);
            messageLabel.setText("Please enter a reason for leave/absence");
            valid = false;
        } else {
            messageLabel.setTextFill(Color.web("#a6a1a1"));
            messageLabel.setText("LEAVE / ABSENCE REASON");
        }
        if (!valid) {
            return;
        }
        try {
            String sql = "INSERT INTO leaves (UserName ,request_type, leave_type, from_date, to_date, reason ,status ) VALUES (?, ?, ?, ?, ? ,? ,? )";
            PreparedStatement stmt = ConnectionDB.prepareStatement(sql);

            String username = Session.loggedInUsername;
            String requestType = requestTypeComboBox.getValue();
            String leaveType = leaveTypeComboBox.getValue(); // Can be null for Absence
            String fromDate = FromDate.getValue().toString();
            String toDate = ToDate.getValue().toString();
            String reason = reasonTextArea.getText();

            stmt.setString(1, username);
            stmt.setString(2, requestType);
            stmt.setString(3, ("Leave".equals(requestType) && leaveType != null) ? leaveType : "N/A");
            stmt.setString(4, fromDate);
            stmt.setString(5, toDate);
            stmt.setString(6, reason);
            stmt.setString(7, "Pending");

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                messageLabel.setTextFill(javafx.scene.paint.Color.GREEN);
                messageLabel.setText("Leave / Absence applied successfully!");
            }
        } catch (SQLException e) {
            messageLabel.setTextFill(javafx.scene.paint.Color.RED);
            messageLabel.setText("Database error" + e.getMessage());
            e.printStackTrace();
        }
    }


    private void   clearMessages() {
        FromDate.setStyle("-fx-border-color: red; -fx-border-width: 2;");
        FromDate.setStyle("-fx-border-color: #0077b6; -fx-border-width: 1;");

        todate.setTextFill(Color.web("#a6a1a1"));
        todate.setText("TO DATE");

        messageLabel.setTextFill(Color.web("#a6a1a1"));
        messageLabel.setText("LEAVE / ABSENCE REASON");
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
}