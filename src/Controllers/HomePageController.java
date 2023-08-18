package Controllers;

import Entities.HomeworkOrReproof;
import Entities.SubjectFinalGrade;
import Entities.SubjectGrade;
import Staff.TableType;
import Staff.UserRole;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

import static Controllers.DataController.*;
import static Controllers.TestApplication.user;
import static Staff.Assistant.*;
import static Staff.TableType.*;


public class HomePageController {
    private String[] selectedItems;
    private static TableType currentTableType;

    @FXML
    private TableView<SubjectFinalGrade> finalGradesTableView;

    @FXML
    private TableColumn<SubjectFinalGrade, Integer> number;
    @FXML
    private TableColumn<SubjectFinalGrade, String> subjectName;
    @FXML
    private TableColumn<SubjectFinalGrade, Integer> firstQuarter;
    @FXML
    private TableColumn<SubjectFinalGrade, Integer> secondQuarter;
    @FXML
    private TableColumn<SubjectFinalGrade, Integer> thirdQuarter;
    @FXML
    private TableColumn<SubjectFinalGrade, Integer> fourthQuarter;
    @FXML
    private TableColumn<SubjectFinalGrade, Integer> finalGrade;


    @FXML
    private TableView<SubjectGrade> gradesTableView;

    @FXML
    private TableColumn<SubjectGrade, String> gradeTeacher;
    @FXML
    private TableColumn<SubjectGrade, String> date;
    @FXML
    private TableColumn<SubjectGrade, Integer> gradeMark;
    @FXML
    private TableColumn<SubjectGrade, Integer> gradeNumber;
    @FXML
    private TableColumn<SubjectGrade, String> student;


    @FXML
    private TableView<HomeworkOrReproof> homeworkTableView;

    @FXML
    private TableColumn<HomeworkOrReproof, Integer> homeworkNumber;
    @FXML
    private TableColumn<HomeworkOrReproof, String> homeworkDeadline;
    @FXML
    private TableColumn<HomeworkOrReproof, String> homeworkTeacher;
    @FXML
    private TableColumn<HomeworkOrReproof, String> homeworkBody;


    @FXML
    private TableView<HomeworkOrReproof> reproofsTableView;

    @FXML
    private TableColumn<HomeworkOrReproof, Integer> reproofNumber;
    @FXML
    private TableColumn<HomeworkOrReproof, String> reproofTeacher;
    @FXML
    private TableColumn<HomeworkOrReproof, String> reproofBody;


    @FXML
    private Button finalGradesButton;

    @FXML
    private Pane finalGradesPane;

    @FXML
    private Pane gradesPane;

    @FXML
    private Pane homeworkPane;

    @FXML
    private Pane reproofsPane;

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
    private Label selectedItem1;

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

        if(user.getRole().equals(UserRole.STUDENT)) {
            submitHomeworkButton.setDisable(true);
            submitReproofButton.setDisable(true);
            gradeButton.setDisable(true);
        }
    }

    @FXML
    void grade(ActionEvent event) {

    }

    @FXML
    void rebootActiveTable(ActionEvent event) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("..//fxmls//HomePage.fxml")));
        // TODO: выяснить, какая таблица активна в данный момент и обновить ее с помощью метода rebootTable()
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        HomePageController newPageController = fxmlLoader.getController();
        newPageController.selectedItems = selectedItems;
        newPageController.init(user.getName());
        newPageController.setSelectedItems();

        newPageController.rebootTable(currentTableType);

    }

    @FXML
    void showFinalGrades(ActionEvent event) throws SQLException {
        selectedItems = getClassAndStudent();
        setSelectedItems();

        rebootTable(FINAL_GRADES);
    }

    private void setSelectedItems() {
        selectedItem1.setText(selectedItems[0]);
        selectedItem2.setText(selectedItems[1]);
    }

    private void rebootTable(TableType tableType) throws SQLException {
        cleanTableSpace();
        currentTableType = tableType;

        switch (tableType) {
            case FINAL_GRADES -> {
                fillFinalGradesTable(selectedItems);

                finalGradesPane.setVisible(true);
            }
            case GRADES -> {
                fillGradesTable(selectedItems);

                gradesPane.setVisible(true);
            }
            case HOMEWORK -> {
                fillHomeworkTable(selectedItems);

                homeworkPane.setVisible(true);
            }
            case REPROOFS -> {
                fillReproofsTable(selectedItems);

                reproofsPane.setVisible(true);
            }
        }
    }

    private void cleanTableSpace() {
        finalGradesPane.setVisible(false);
        gradesPane.setVisible(false);
        homeworkPane.setVisible(false);
        reproofsPane.setVisible(false);
    }

    @FXML
    void showGrades(ActionEvent event) throws SQLException {
        selectedItems = getClassAndSubject();
        setSelectedItems();

        rebootTable(GRADES);
    }

    @FXML
    void showHomeworkAndReproofs(ActionEvent event) throws SQLException {
        //В зависимости от названия нажатой кнопки будет выводится таблица с дз или с замечаниями
        switch (((Button) event.getSource()).getText()) {
            case "Домашнее задание" -> {
                selectedItems = getClassAndSubject();
                rebootTable(HOMEWORK);
            }
            case "Замечания" -> {
                selectedItems = getClassAndStudent();
                rebootTable(REPROOFS);
            }
        }

        setSelectedItems();
    }

    @FXML
    void submitHomework(ActionEvent event) {

    }

    @FXML
    void submitReproof(ActionEvent event) {

    }

    private void fillReproofsTable(String[] selectedItems) throws SQLException {
        //Настройка соответсвия столбцов и полей хранимой сущности.
        //Таблица будет хранить домашние задания по предмету (объекты HomeworkOrReproof).
        reproofNumber.setCellValueFactory(new PropertyValueFactory<>("homeworkOrReproofNumber"));
        reproofTeacher.setCellValueFactory(new PropertyValueFactory<>("homeworkOrReproofTeacher"));
        reproofBody.setCellValueFactory(new PropertyValueFactory<>("homeworkOrReproofBody"));

        reproofsTableView.setItems(getReproofs(selectedItems[1]));

        /*После заполнения таблицы необходимо обнулить счетчик в классе SubjectFinalGrade,
        чтобы при следующем заполнении он был равен нулю. */
        HomeworkOrReproof.resetCounter();
    }

    private void fillHomeworkTable(String[] selectedItems) throws SQLException {
        //Настройка соответсвия столбцов и полей хранимой сущности.
        //Таблица будет хранить домашние задания по предмету (объекты HomeworkOrReproof).
        homeworkNumber.setCellValueFactory(new PropertyValueFactory<>("homeworkOrReproofNumber"));
        homeworkDeadline.setCellValueFactory(new PropertyValueFactory<>("homeworkOrReproofDeadline"));
        homeworkTeacher.setCellValueFactory(new PropertyValueFactory<>("homeworkOrReproofTeacher"));
        homeworkBody.setCellValueFactory(new PropertyValueFactory<>("homeworkOrReproofBody"));

        homeworkTableView.setItems(getHomework(selectedItems));

        /*После заполнения таблицы необходимо обнулить счетчик в классе SubjectFinalGrade,
        чтобы при следующем заполнении он был равен нулю. */
        HomeworkOrReproof.resetCounter();
    }

    private void fillFinalGradesTable(String[] selectedItems) throws SQLException {
        //Настройка соответсвия столбцов и полей хранимой сущности.
        //Таблица будет хранить итоговые оценки по предмету (объекты SubjectFinalGrade).
        number.setCellValueFactory(new PropertyValueFactory<>("number"));
        subjectName.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        firstQuarter.setCellValueFactory(new PropertyValueFactory<>("firstQuarter"));
        secondQuarter.setCellValueFactory(new PropertyValueFactory<>("secondQuarter"));
        thirdQuarter.setCellValueFactory(new PropertyValueFactory<>("thirdQuarter"));
        fourthQuarter.setCellValueFactory(new PropertyValueFactory<>("fourthQuarter"));
        finalGrade.setCellValueFactory(new PropertyValueFactory<>("finalGrade"));

        finalGradesTableView.setItems(getSubjectFinalGrades(selectedItems[1]));

        /*После заполнения таблицы необходимо обнулить счетчик в классе SubjectFinalGrade,
        чтобы при следующем заполнении он был равен нулю. */
        SubjectFinalGrade.resetCounter();
    }

    public void fillGradesTable(String[] selectedItems) throws SQLException {
        //Настройка соответсвия столбцов и полей хранимой сущности.
        //Таблица будет хранить  оценки по предмету (объекты SubjectGrade).
        gradeNumber.setCellValueFactory(new PropertyValueFactory<>("gradeNumber"));
        student.setCellValueFactory(new PropertyValueFactory<>("student"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        gradeTeacher.setCellValueFactory(new PropertyValueFactory<>("gradeTeacher"));
        gradeMark.setCellValueFactory(new PropertyValueFactory<>("gradeMark"));


        gradesTableView.setItems(getSubjectGrades(selectedItems));

        /*После заполнения таблицы необходимо обнулить счетчик в классе SubjectFinalGrade,
        чтобы при следующем заполнении он был равен нулю. */
        SubjectGrade.resetCounter();
    }
}

