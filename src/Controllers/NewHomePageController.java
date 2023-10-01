package Controllers;

import Entities.Request;
import Entities.UserRole;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

import static Controllers.Application.user;
import static Controllers.DataController.getRequests;

public class NewHomePageController {
    @FXML
    private Pane adminPane;

    @FXML
    private TableView<Request> adminTableView;

    @FXML
    private TableColumn<Request, String> requestStatusTC;
    @FXML
    private TableColumn<Request, String> fillingDateStudentTC;
    @FXML
    private TableColumn<Request, String> responseDateTC;

    @FXML
    private ComboBox<String> categoryCB;

    @FXML
    private TableColumn<Request, String> commentTC;

    @FXML
    private TableColumn<Request, String> faCategoryStudentTC;

    @FXML
    private TableColumn<Request, String> faCategoryAdminTC;

    @FXML
    private ComboBox<String> instituteCB;

    @FXML
    private TableColumn<Request, String> instituteTC;
    @FXML
    private TableColumn<Request, String> fillingDateAdminTC;

    @FXML
    private TableColumn<Request, String> paymentTC;

    @FXML
    private ComboBox<String> specialtyCB;

    @FXML
    private TableColumn<Request, String> specialtyTC;

    @FXML
    private Pane studentPane;

    @FXML
    private TableColumn<Request, String> studentTC;

    @FXML
    private TableView<Request> studentTableView;

    @FXML
    private Label userLabel;

    @FXML
    void filterByCategory(ActionEvent event) {

    }

    @FXML
    void filterByInstitute(ActionEvent event) {

    }

    @FXML
    void filterBySpecialty(ActionEvent event) {

    }

    @FXML
    void openRequest(ActionEvent event) throws SQLException, IOException {
        showRequestPage(event, user.getRole(), adminTableView.getSelectionModel().getSelectedItem());
    }

    @FXML
    void sendRequest(ActionEvent event) throws SQLException, IOException {
        showRequestPage(event, user.getRole(), null);
    }

    private void showRequestPage(ActionEvent event, UserRole userRole, Request request) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("..//fxmls//RequestPage.fxml")));

        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        RequestPageController requestPageController = fxmlLoader.getController();
        requestPageController.init(userRole, request);
    }

    public void init() throws SQLException {
        userLabel.setText(user.getName() + " (" + user.getRole() + ")");


        if (user.getRole().equals(UserRole.STUDENT)) {
            studentPane.setVisible(true);
            adminPane.setVisible(false);

            fillStudentTableView();

            return;
        }

        studentPane.setVisible(false);
        adminPane.setVisible(true);

        fillAdminTableView();
    }

    private void fillAdminTableView() throws SQLException {
        //Настройка соответсвия столбцов и полей хранимой сущности.
        //Таблица будет хранить заявки на получение материальной помощи студента (объекты Request).
        faCategoryAdminTC.setCellValueFactory(new PropertyValueFactory<>("faCategory"));
        instituteTC.setCellValueFactory(new PropertyValueFactory<>("studentInstitute"));
        specialtyTC.setCellValueFactory(new PropertyValueFactory<>("studentSpecialty"));
        studentTC.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        fillingDateAdminTC.setCellValueFactory(new PropertyValueFactory<>("fillingDate"));

        adminTableView.setItems(getRequests(user));
    }

    private void fillStudentTableView() throws SQLException {
        //Настройка соответсвия столбцов и полей хранимой сущности.
        //Таблица будет хранить заявки на получение материальной помощи студента (объекты Request).
        faCategoryStudentTC.setCellValueFactory(new PropertyValueFactory<>("faCategory"));
        requestStatusTC.setCellValueFactory(new PropertyValueFactory<>("requestStatus"));
        fillingDateStudentTC.setCellValueFactory(new PropertyValueFactory<>("fillingDate"));
        responseDateTC.setCellValueFactory(new PropertyValueFactory<>("responseDate"));
        commentTC.setCellValueFactory(new PropertyValueFactory<>("adminComment"));
        paymentTC.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));

        studentTableView.setItems(getRequests(user));
    }
}
