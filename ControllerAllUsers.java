package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ControllerAllUsers implements Initializable {

    Connection connection = null;
    PreparedStatement st = null;
    ResultSet rs = null;

    @FXML private TableView<AllUsers> allUsersTable;
    @FXML private TableColumn<AllUsers, String> colID;
    @FXML private TableColumn<AllUsers, String> colUser_Name;
    @FXML private TableColumn<AllUsers, String> colName;
    @FXML private TableColumn<AllUsers, String> colEmail;
    @FXML private TableColumn<AllUsers, String> colCountry;
    @FXML private TableColumn<AllUsers, String> colGender;
    @FXML private TableColumn<AllUsers, String> colDepartment;

    @FXML private Button btnDeleteUsers;
    @FXML private Button btnUpdateUsers;
    @FXML private Button btnAddUsers;
    @FXML private Button btnAdminProfile;
    @FXML private Button btnAllUsers;
    @FXML private Button btnAddEmployee;
    @FXML private Button btnUsersLeavesHistory;
    @FXML private Button btnLogOut;
    @FXML private Button btnDashboard2;
    @FXML private Button btnAbsenceToday;

    @FXML
    private TextField txtSearch2;

    private ObservableList<AllUsers> userList = FXCollections.observableArrayList();
    private FilteredList<AllUsers> filteredData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userList = getAllUsers();
        configureTable();
        setupSearchFilter2();
    }

    private void configureTable() {
        allUsersTable.setItems(userList);

        colID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colUser_Name.setCellValueFactory(new PropertyValueFactory<>("User_Name"));
        colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
        colCountry.setCellValueFactory(new PropertyValueFactory<>("Country"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("Gender"));
        colDepartment.setCellValueFactory(new PropertyValueFactory<>("Department"));
    }

    private ObservableList<AllUsers> getAllUsers() {
        ObservableList<AllUsers> list = FXCollections.observableArrayList();
        try {
            connection = ConnectionDB.getConnection();
            if (connection == null || connection.isClosed()) {
                showAlert("Connection Error", "Database connection is closed.");
                return list;
            }

            String query = "SELECT * FROM all_users";
            st = connection.prepareStatement(query);
            rs = st.executeQuery();

            while (rs.next()) {
                AllUsers user = new AllUsers(
                        rs.getString("ID"),
                        rs.getString("User_Name"),
                        rs.getString("Name"),
                        rs.getString("Email"),
                        rs.getString("Country"),
                        rs.getString("Gender"),
                        rs.getString("Department")
                );
                list.add(user);
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load users: " + e.getMessage());
        }
        return list;
    }


    @FXML
    private void setupSearchFilter2() {
        filteredData = new FilteredList<>(userList, p -> true);

        txtSearch2.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(user -> {
                if (newValue == null || newValue.isEmpty()) return true;

                String filter = newValue.toLowerCase();

                return user.getID().toLowerCase().contains(filter) ||
                        user.getUser_Name().toLowerCase().contains(filter) ||
                        user.getName().toLowerCase().contains(filter) ||
                        user.getEmail().toLowerCase().contains(filter) ||
                        user.getCountry().toLowerCase().contains(filter) ||
                        user.getGender().toLowerCase().contains(filter) ||
                        user.getDepartment().toLowerCase().contains(filter);
            });
        });

        allUsersTable.setItems(filteredData);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
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
    void LoadAddUsers(ActionEvent event) {
        loadScene(event, "/sample/sampleAddUsers.fxml");
    }

    @FXML
    void LoadDeleteUsers(ActionEvent event) {
        AllUsers selectedUser = allUsersTable.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            showAlert("No Selection", "Please select a user to delete.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Are you sure you want to delete user: " + selectedUser.getUser_Name() + "?");

        if (confirmation.showAndWait().get() == ButtonType.OK) {
            String sql = "DELETE FROM all_users WHERE ID = ?";

            try (Connection connection = ConnectionDB.getConnection();
                 PreparedStatement pst = connection.prepareStatement(sql)) {

                pst.setString(1, selectedUser.getID());
                int rowsAffected = pst.executeUpdate();

                if (rowsAffected > 0) {
                    userList.remove(selectedUser);
                    showAlert("Deleted", "User deleted successfully.");
                } else {
                    showAlert("Error", "Failed to delete the user.");
                }

            } catch (SQLException e) {
                showAlert("Database Error", e.getMessage());
            }
        }
    }

    @FXML
    void LoadUpdateUsers(ActionEvent event) {
        AllUsers selectedUser = allUsersTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert("Selection Error", "Please select a user to update.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/sampleUpdateUsers.fxml"));
            Parent root = loader.load();

            ControllerUpdateUsers updateController = loader.getController();
            updateController.setUserData(selectedUser);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Failed to load update page.");
        }
    }


    @FXML void LoadAdminProfile(ActionEvent event) {
        loadScene(event, "/sample/sampleAdminProfile.fxml");
    }

    @FXML void LoadAllUsers(ActionEvent event) {
        loadScene(event, "/sample/sampleAllUsers.fxml");
    }

    @FXML void LoadAddEmployee(ActionEvent event) {
        loadScene(event, "/sample/sampleAddEmployee.fxml");
    }

    @FXML void LoadUsersLeavesHistory(ActionEvent event) {
        loadScene(event, "/sample/sampleUsersLeavesHistory.fxml");
    }

    @FXML void LoadDashboard2(ActionEvent event) {
        loadScene(event, "/sample/sampleAdminDashboard.fxml");
    }

    @FXML void LoadAbsenceToday(ActionEvent event) { loadScene(event, "/sample/sampleAbsenceToday.fxml"); }

    @FXML void LoadLogOut(ActionEvent event) {
        loadScene(event, "/sample/sampleLoginPage.fxml");
    }
}