package milktea.milktea.GUI;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import milktea.milktea.BUS.Provider_BUS;
import milktea.milktea.DTO.Provider;

public class ProviderListSubGUI {
    @FXML
    private TableView<Provider> tableMain;
    @FXML
    private TableColumn<Provider, String> colID;
    @FXML
    private TableColumn<Provider, String> colName;
    @FXML
    private TableColumn<Provider, String> colPhone;
    @FXML
    private TableColumn<Provider, String> colEmail;

    @FXML
    private TextField txtSearch;

    @FXML
    private Button btnSearch;
    @FXML
    private Button btnChoice;

    @Getter
    @Setter
    private static Provider provider;

    @FXML
    public void initialize() {
        createProviderTable();
    }

    public void createProviderTable() {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableMain.setItems(FXCollections.observableArrayList(Provider_BUS.getAllProvider()));
        btnSearch.setOnAction(e -> {
            tableMain.setItems(FXCollections.observableArrayList(Provider_BUS.searchProvider(txtSearch.getText())));
        });
        btnChoice.setOnAction(e -> {
            if (tableMain.getSelectionModel().getSelectedItem() != null) {
                provider = tableMain.getSelectionModel().getSelectedItem();
                if (provider != null) {
                    ((Stage) btnChoice.getScene().getWindow()).close();
                }
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setContentText("Vui lòng chọn nhà cung cấp");
                alert.show();
            }
        });
    }
}
