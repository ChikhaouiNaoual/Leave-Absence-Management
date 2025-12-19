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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;



public class ControllerUsers implements Initializable {
    Connection connection = null;
    PreparedStatement st = null;
    ResultSet rs = null;



            @FXML private Button btnProfile;
            @FXML private Button btnUsers;
            @FXML private Button btnApplyLeave;
            @FXML private Button btnMyLeavesHistory;
            @FXML private Button btnLogOut;
            @FXML private Button btnDashboard;
            @FXML private TextField txtSearch;
            @FXML private TableView<Users> usersTable;
            @FXML private TableColumn<Users, Integer> colEmployee_ID;
            @FXML private TableColumn<Users, String> colFull_Name;
            @FXML private TableColumn<Users, String> colDepartment;
            @FXML private TableColumn<Users, String> colRole;
            @FXML private TableColumn<Users, String> colStatus;





    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userList = getUsers();
        configureTable();
        setupSearchFilter();
    }

    private ObservableList<Users> userList = FXCollections.observableArrayList();
    private FilteredList<Users> filteredData;

    private void configureTable() {
        usersTable.setItems(userList);
        colEmployee_ID.setCellValueFactory(new PropertyValueFactory<>("employee_ID"));
        colFull_Name.setCellValueFactory(new PropertyValueFactory<>("full_Name"));
        colDepartment.setCellValueFactory(new PropertyValueFactory<>("department"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        colStatus.setCellFactory(column -> new TableCell<Users, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if ("Active".equalsIgnoreCase(item)) {
                        setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                    } else if ("Inactive".equalsIgnoreCase(item)) {
                        setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
                    }
                }
            }
        });
    }

    private ObservableList<Users> getUsers(){
        ObservableList<Users> list = FXCollections.observableArrayList();
        try{
            connection = ConnectionDB.getConnection();
            if (connection == null || connection.isClosed()){
                showAlert("Connection Error","Database connection is closed .");
                return list;
            }
            String query = "SELECT * FROM users";
            st = connection.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                Users u = new Users(
                        rs.getInt("Employee_ID"),
                        rs.getString("Full_Name"),
                        rs.getString("Department"),
                        rs.getString("Role"),
                        rs.getString("Status")
                );
                list.add(u);
            }
        } catch (SQLException e) {
            showAlert("Database Error","Failed to load users:" +e.getMessage());
        }
        return list;
    }


    @FXML
    private void setupSearchFilter() {

        filteredData = new FilteredList<>(userList, p -> true);
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(user -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return String.valueOf(user.getEmployee_ID()).contains(lowerCaseFilter) ||
                        user.getFull_Name().toLowerCase().contains(lowerCaseFilter) ||
                        user.getDepartment().toLowerCase().contains(lowerCaseFilter) ||
                        user.getRole().toLowerCase().contains(lowerCaseFilter) ||
                        user.getStatus().toLowerCase().contains(lowerCaseFilter);
            });
        });

        usersTable.setItems(filteredData);
    }


        private void showAlert (String title, String message){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
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
            showAlert("Navigation Error", "Failed to load page: " + e.getMessage());
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
}

