package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;

public class NewHomePageController {
    @FXML
    private Pane adminPane;

    @FXML
    private TableView<?> adminTableView;

    @FXML
    private TableColumn<?, ?> applicationStatusTC;

    @FXML
    private ComboBox<String> categoryCB;

    @FXML
    private TableColumn<?, ?> commentTC;

    @FXML
    private TableColumn<?, ?> faCategoryStudentTC;

    @FXML
    private TableColumn<?, ?> faCategoryTC;

    @FXML
    private ComboBox<?> instituteCB;

    @FXML
    private TableColumn<?, ?> instituteTC;

    @FXML
    private TableColumn<?, ?> paymentTC;

    @FXML
    private ComboBox<?> specialtyCB;

    @FXML
    private TableColumn<?, ?> specialtyTC;

    @FXML
    private Pane studentPane;

    @FXML
    private TableColumn<?, ?> studentTC;

    @FXML
    private TableView<?> studentTableView;

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
    void openApplication(ActionEvent event) {

    }

    @FXML
    void sendApplication(ActionEvent event) {

    }
}
