package Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;

public class InsertPageController {
    @FXML
    private ComboBox<String> classGradeComboBox;

    @FXML
    private ComboBox<String> classHWComboBox;

    @FXML
    private ComboBox<String> classReproofComboBox;

    @FXML
    private DatePicker deadlineDatePicker;

    @FXML
    private ComboBox<Integer> gradeComboBox;

    @FXML
    private Label gradeLabel;

    @FXML
    private Pane gradePane;

    @FXML
    private Pane homeworkPane;

    @FXML
    private TextArea homeworkTextArea;

    @FXML
    private Pane reproofPane;

    @FXML
    private TextArea reproofTextArea;

    @FXML
    private ComboBox<String> studentGradeComboBox;
    
    @FXML
    private ComboBox<String> studentReproofComboBox;

    @FXML
    private ComboBox<String> subjectGradeComboBox;

    @FXML
    private ComboBox<String> subjectHWComboBox;

    @FXML
    void selectClassForGrade(ActionEvent event) throws SQLException {
        studentGradeComboBox.setItems(DataController.getStudentsFios(classGradeComboBox.getValue()));
    }

    @FXML
    void selectClassForReproof(ActionEvent event) throws SQLException {
        studentReproofComboBox.setItems(DataController.getStudentsFios(classReproofComboBox.getValue()));
    }

    @FXML
    private void submitGrade(ActionEvent event) throws SQLException, IOException {
        String student;
        int grade;
        String subject;

        try {
            student = studentGradeComboBox.getValue();
            grade = gradeComboBox.getValue();
            subject = subjectGradeComboBox.getValue();
        } catch (NullPointerException exception) {
            JOptionPane.showMessageDialog(null, "Выбранны не все параметры \n" +
                    "Проверьте введеные данные.", " Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DataController.insertGrade(student, TestApplication.user.getName(), subject, grade);

        toHomePage(event);
    }

    @FXML
    private void submitHomework(ActionEvent event) throws SQLException, IOException {
        String className;
        String subject;
        String homeworkText;
        LocalDate deadline = deadlineDatePicker.getValue();

        try {
            className = classHWComboBox.getValue();
            subject = subjectHWComboBox.getValue();
            homeworkText = homeworkTextArea.getText();
        } catch (NullPointerException exception) {
            JOptionPane.showMessageDialog(null, "Выбранны/введены не все параметры \n" +
                    "Проверьте введеные данные.", " Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DataController.insertHomework(className, subject, TestApplication.user.getName(), deadline, homeworkText);

        toHomePage(event);
    }

    @FXML
    private void submitReproof(ActionEvent event) throws SQLException, IOException {
        String student;
        String reproofText;

        try {
            student = studentReproofComboBox.getValue();
            reproofText = reproofTextArea.getText();
        } catch (NullPointerException exception) {
            JOptionPane.showMessageDialog(null, "Выбранны/введены не все параметры \n" +
                    "Проверьте введеные данные.", " Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DataController.insertReproof(student, TestApplication.user.getName(), reproofText);

        toHomePage(event);
    }

    private void toHomePage(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("..//fxmls//HomePage.fxml")));

        LogInController.toHomePageScene(event, fxmlLoader);

        //Инициализация сцены личного кабинета
        ((HomePageController) fxmlLoader.getController()).init(TestApplication.user.getLogin());
    }

    public void init(String submitType) throws SQLException {
        switch (submitType) {
            case "grade" -> {
                clearScene(gradePane);

                ObservableList<Integer> grades = FXCollections.observableArrayList();
                for(int i = 1; i < 11; i++) {
                    grades.add(i);
                }

                classGradeComboBox.setItems(DataController.getClassesNames());

                gradeComboBox.setItems(grades);
                subjectGradeComboBox.setItems(DataController.getSubjectNames());
            }
            case "homework" -> {
                clearScene(homeworkPane);

                classHWComboBox.setItems(DataController.getClassesNames());
                subjectHWComboBox.setItems(DataController.getSubjectNames());
            }
            case "reproof" -> {
                clearScene(reproofPane);

                classReproofComboBox.setItems(DataController.getClassesNames());
            }
        }
    }

    private void clearScene(Pane currentPane) {
        gradePane.setVisible(false);
        homeworkPane.setVisible(false);
        reproofPane.setVisible(false);

        currentPane.setVisible(true);
    }
}
