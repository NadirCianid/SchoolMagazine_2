package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class RequestPageController {
    @FXML
    private TextArea adminCommentTextArea;

    @FXML
    private Pane adminPane;

    @FXML
    private TableColumn<?, ?> categoryTC;

    @FXML
    private TextField confDocsLink;

    @FXML
    private Hyperlink confirmDocsLink;

    @FXML
    private TableColumn<?, ?> descriptionTC;

    @FXML
    private TableView<?> faCategoriesTableView;

    @FXML
    private Text faCategoryText;

    @FXML
    private TableColumn<?, ?> paymentAmountTC;

    @FXML
    private TextField paymentAmountTextField;

    @FXML
    private ComboBox<?> statusComboBox;

    @FXML
    private Pane studentPane;

    @FXML
    void sendConclusion(ActionEvent event) {

    }

    @FXML
    void sendRequest(ActionEvent event) {

    }
}
