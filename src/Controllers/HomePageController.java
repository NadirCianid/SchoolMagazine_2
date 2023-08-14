package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Pane;

import static Controllers.TestApplication.user;

public class HomePageController {

    @FXML
    private TableView<?> FinalGradesTableView;

    @FXML
    private Button finalGradesButton;

    @FXML
    private Pane finalGradesPane;

    @FXML
    private Button gradeButton;

    @FXML
    private Button gradesButton;

    @FXML
    private Button homeworkButton;

    @FXML
    private Button rebootButton;

    @FXML
    private Button reproofsButton;

    @FXML
    private ToolBar selectedItem1;

    @FXML
    private Label selectedItem2;

    @FXML
    private Button submitHomeworkButton;

    @FXML
    private Button submitReproofButton;

    @FXML
    private Label userNameLabel;

    @FXML
    private Label userStatusLabel;

    public void init(String login) {
        userStatusLabel.setText(user.getRole().name());
        userNameLabel.setText(user.getName());
    }

    @FXML
    void grade(ActionEvent event) {

    }

    @FXML
    void rebootPage(ActionEvent event) {

    }

    @FXML
    void shoowReproofs(ActionEvent event) {

    }

    @FXML
    void showFinalGrades(ActionEvent event) {

    }

    @FXML
    void showGrades(ActionEvent event) {

    }

    @FXML
    void showHomework(ActionEvent event) {

    }

    @FXML
    void submitHomework(ActionEvent event) {

    }

    @FXML
    void submitReproof(ActionEvent event) {

    }

}

