package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class LogInController {
    private static Stage stage;
    private static Scene scene;
    private static Parent root;

    @FXML
    private Button logInButton;

    @FXML
    private TextField logInTextField;

    @FXML
    private PasswordField pswdPasswordField;

    @FXML
    private void logIn(ActionEvent event) throws IOException, SQLException {
        String login = logInTextField.getText();
        String pswd = pswdPasswordField.getText();

        boolean dataIsValid = DataController.isDataValid(login, pswd);

        if(dataIsValid) {
            Application.user.setName(DataController.getName(login));
            Application.user.setLogin(login);

            //Получение FXMLLoader объекта  для перехода к новой сцене и доступа к котроллеру этой сцены
            FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("..//fxmls//HomePage.fxml")));

            toHomePageScene(event, fxmlLoader);

            //Инициализация сцены личного кабинета
            ((NewHomePageController) fxmlLoader.getController()).init();
        } else {
            JOptionPane.showMessageDialog(null, "Нет такого пользователя! \n" +
                    "Проверьте введеные данные.", " Ошибка", JOptionPane.ERROR_MESSAGE);
        }
        cleanTF();
    }

    private void cleanTF() {
        logInTextField.clear();
        pswdPasswordField.clear();
    }

    public static void toHomePageScene(ActionEvent event, FXMLLoader fxmlLoader) throws IOException {
        root = fxmlLoader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}
