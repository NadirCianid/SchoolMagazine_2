package Controllers;

import Entities.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Objects;

public class TestApplication extends Application {
    public static User user;
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("..//fxmls//LogIn.fxml"))));
        stage.setScene(new Scene(root));
        stage.setResizable(false);

        stage.show();
    }

    public static void main(String[] args) throws SQLException {
        DataController.initializeDataBase();
        user = new User();
        launch(args);
    }
}
