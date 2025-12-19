package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ControllerAbsenceToday {

    @FXML private Button btnDashboard2;
    @FXML private Button btnAdminProfile;
    @FXML private Button btnAllUsers;
    @FXML private Button btnAddEmployee;
    @FXML private Button btnUsersLeavesHistory;
    @FXML private Button btnLogOutAdmin;
    @FXML private Button btnAbsenceToday;

    @FXML private TableView<AbsenceTodayModel> tableAbsenceToday;
    @FXML private TableColumn<AbsenceTodayModel, String> colName;
    @FXML private TableColumn<AbsenceTodayModel, String> colM;
    @FXML private TableColumn<AbsenceTodayModel, String> colT;
    @FXML private TableColumn<AbsenceTodayModel, String> colW;
    @FXML private TableColumn<AbsenceTodayModel, String> colTh;
    @FXML private TableColumn<AbsenceTodayModel, String> colF;
    @FXML private TableColumn<AbsenceTodayModel, String> colS;
    @FXML private TableColumn<AbsenceTodayModel, String> colToday;

    @FXML private Button btnPreviousWeek;
    @FXML private Button btnNextWeek;
    @FXML private Label labelWeekRange;

    private LocalDate currentWeekStart;

    @FXML
    public void initialize() {
        currentWeekStart = LocalDate.now().with(DayOfWeek.MONDAY);


        applyIconCellFactory(colM);
        applyIconCellFactory(colT);
        applyIconCellFactory(colW);
        applyIconCellFactory(colTh);
        applyIconCellFactory(colF);
        applyIconCellFactory(colS);
        applyIconCellFactory(colToday);

        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colM.setCellValueFactory(new PropertyValueFactory<>("monday"));
        colT.setCellValueFactory(new PropertyValueFactory<>("tuesday"));
        colW.setCellValueFactory(new PropertyValueFactory<>("wednesday"));
        colTh.setCellValueFactory(new PropertyValueFactory<>("thursday"));
        colF.setCellValueFactory(new PropertyValueFactory<>("friday"));
        colS.setCellValueFactory(new PropertyValueFactory<>("saturday"));
        colToday.setCellValueFactory(new PropertyValueFactory<>("today"));

        loadTodayAbsences();
    }

    private void applyIconCellFactory(TableColumn<AbsenceTodayModel, String> column) {
        column.setCellFactory(col -> new TableCell<AbsenceTodayModel, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    if (item.equals("A")) {
                        setText("❌");
                        setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-alignment: center;");
                    } else {
                        setText("✅");
                        setStyle("-fx-text-fill: green; -fx-font-weight: bold; -fx-alignment: center;");
                    }
                }
            }
        });
    }

    private void loadTodayAbsences() {
        ObservableList<AbsenceTodayModel> data = FXCollections.observableArrayList();

        try (Connection connection = ConnectionDB.getConnection()) {
            LocalDate monday = currentWeekStart;
            LocalDate saturday = monday.plusDays(5);

            labelWeekRange.setText("Week: " + monday.format(DateTimeFormatter.ofPattern("MMM dd")) +
                    " - " + saturday.format(DateTimeFormatter.ofPattern("MMM dd")));

            LocalDate today = LocalDate.now();
            DayOfWeek todayDayOfWeek = today.getDayOfWeek();
            colToday.setText(todayDayOfWeek.toString().substring(0, 3)); // e.g., MON

            String query = "SELECT UserName, from_date, to_date FROM leaves " +
                    "WHERE status = 'Approved' AND " +
                    "((from_date BETWEEN ? AND ?) OR (to_date BETWEEN ? AND ?) OR (? BETWEEN from_date AND to_date))";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setDate(1, java.sql.Date.valueOf(monday));
            ps.setDate(2, java.sql.Date.valueOf(saturday));
            ps.setDate(3, java.sql.Date.valueOf(monday));
            ps.setDate(4, java.sql.Date.valueOf(saturday));
            ps.setDate(5, java.sql.Date.valueOf(today));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String name = rs.getString("UserName");
                LocalDate from = rs.getDate("from_date").toLocalDate();
                LocalDate to = rs.getDate("to_date").toLocalDate();

                String m = from.compareTo(monday) <= 0 && monday.compareTo(to) <= 0 ? "A" : "";
                String t = from.compareTo(monday.plusDays(1)) <= 0 && monday.plusDays(1).compareTo(to) <= 0 ? "A" : "";
                String w = from.compareTo(monday.plusDays(2)) <= 0 && monday.plusDays(2).compareTo(to) <= 0 ? "A" : "";
                String th = from.compareTo(monday.plusDays(3)) <= 0 && monday.plusDays(3).compareTo(to) <= 0 ? "A" : "";
                String f = from.compareTo(monday.plusDays(4)) <= 0 && monday.plusDays(4).compareTo(to) <= 0 ? "A" : "";
                String s = from.compareTo(monday.plusDays(5)) <= 0 && monday.plusDays(5).compareTo(to) <= 0 ? "A" : "";


                String todayCol = from.compareTo(today) <= 0 && today.compareTo(to) <= 0 ? "A" : "";

                AbsenceTodayModel model = new AbsenceTodayModel(name, m, t, w, th, f, s, todayCol);
                data.add(model);
            }

            tableAbsenceToday.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleNextWeek(ActionEvent event) {
        currentWeekStart = currentWeekStart.plusWeeks(1);
        loadTodayAbsences();
    }

    @FXML
    void handlePreviousWeek(ActionEvent event) {
        currentWeekStart = currentWeekStart.minusWeeks(1);
        loadTodayAbsences();
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
    @FXML void LoadAbsenceToday(ActionEvent event) { loadScene(event, "/sample/sampleAbsenceToday.fxml"); }
    @FXML void LoadUsersLeavesHistory(ActionEvent event) { loadScene(event, "/sample/sampleUsersLeavesHistory.fxml"); }
    @FXML void LoadDashboard2(ActionEvent event) { loadScene(event, "/sample/sampleAdminDashboard.fxml"); }
    @FXML void LoadLogOut(ActionEvent event) { loadScene(event, "/sample/sampleLoginPage.fxml"); }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}