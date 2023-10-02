package Controllers;

import Entities.FACategory;
import Entities.Request;
import Entities.UserRole;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

import static Controllers.DataController.getFACategories;

import static Controllers.Application.user;

public class RequestPageController {
    private static Request selectedByAdminRequest;
    @FXML
    private TextArea adminCommentTextArea;

    @FXML
    private Pane adminPane;

    @FXML
    private TableColumn<FACategory, String> categoryTC;

    @FXML
    private TextField confDocsLink;

    @FXML
    private Hyperlink confirmDocsLink;

    @FXML
    private TableColumn<FACategory, String> reqDocsTC;

    @FXML
    private TableView<FACategory> faCategoriesTableView;

    @FXML
    private Text faCategoryText;

    @FXML
    private TableColumn<FACategory, String> paymentAmountTC;

    @FXML
    private TextField paymentAmountTextField;

    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private Pane studentPane;

    @FXML
    void sendConclusion(ActionEvent event) throws SQLException, IOException {
        int paymentAmount;
        try {
            paymentAmount = Integer.parseInt(paymentAmountTextField.getText());
        } catch (NumberFormatException e) {
            paymentAmountTextField.clear();
            JOptionPane.showMessageDialog(null, "Ошибка ввода \n" +
                    "Необходимо ввести натуральное значение выплаты.", " Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String adminComment = adminCommentTextArea.getText();
        String newStatus = statusComboBox.getSelectionModel().getSelectedItem();

        if(newStatus == null) {
            JOptionPane.showMessageDialog(null, "Ошибка ввода \n" +
                    "Необходимо выбрать новый статус заявки.", " Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            DataController.updateRequest(paymentAmount, adminComment, newStatus, selectedByAdminRequest.getId());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ошибка базы данных \n" +
                    "Произошла непредвиденная ошибка. \n Обратитесть к администратору приложения за подробностями.", " Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }


        toHomePage(event);
    }

    @FXML
    void sendRequest(ActionEvent event) throws SQLException, IOException {
        String link = confDocsLink.getText();

        if(link.equals("")) {
            JOptionPane.showMessageDialog(null, "Выбранны/введены не все параметры \n" +
                    "Вы не прикрепили ссылку на ваши документы.", " Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        FACategory faCategory =  faCategoriesTableView.getSelectionModel().getSelectedItem();

        if(faCategory.getTitle().equals("")) {
            JOptionPane.showMessageDialog(null, "Выбранны/введены не все параметры \n" +
                    "Необходимо выбрать строку, в которой указано \n название категории мат. помощи.", " Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DataController.insertRequest(link, faCategory, user.getLogin());

        toHomePage(event);
    }

    public void init(UserRole userRole, Request selectedRequest) throws SQLException {
        switch (userRole) {
            case STUDENT -> {
                studentPane.setVisible(true);
                adminPane.setVisible(false);

                fillFACategoriesTableView();
            }
            case ADMIN -> {
                studentPane.setVisible(false);
                adminPane.setVisible(true);

                setAdminRequestPane(selectedRequest);
            }
        }
    }

    private void setAdminRequestPane(Request selectedRequest) throws SQLException {
        confirmDocsLink.setText(selectedRequest.getConfDocLink());
        faCategoryText.setText("Категория мат. помощи:" + "\n" +
                selectedRequest.getFaCategory() + "\n\n"
                + "Документы необходимые для получения данной категории мат. помощи: \n"
                + DataController.getReqDocsInString(selectedRequest.getFaCategory())
                + "\n\n"
                + "Имя студента: \n"
                + selectedRequest.getStudentName());

        ObservableList<String> statuses = FXCollections.observableArrayList();
        statuses.add("На дополнительном рассмотрении");
        statuses.add("Мат. помощь одобрена");
        statuses.add("Мат. помощь не одобрена");

        statusComboBox.setItems(statuses);

        selectedByAdminRequest = selectedRequest;
    }

    private void fillFACategoriesTableView() throws SQLException {
        //Настройка соответсвия столбцов и полей хранимой сущности.
        //Таблица будет хранить заявки на получение материальной помощи студента (объекты Request).
        categoryTC.setCellValueFactory(new PropertyValueFactory<>("title"));
        reqDocsTC.setCellValueFactory(new PropertyValueFactory<>("reqDocs"));
        paymentAmountTC.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));

        faCategoriesTableView.setItems(getFACategories());
    }

    private void toHomePage(ActionEvent event) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("..//fxmls//HomePage.fxml")));

        LogInController.toHomePageScene(event, fxmlLoader);

        //Инициализация сцены личного кабинета
        ((NewHomePageController) fxmlLoader.getController()).init();
    }
}
