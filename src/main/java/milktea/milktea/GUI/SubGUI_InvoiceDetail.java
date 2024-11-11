package milktea.milktea.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import milktea.milktea.BUS.Product_BUS;
import milktea.milktea.DTO.InvoiceDetail;
import milktea.milktea.DTO.Product;
import milktea.milktea.DTO.Status;
import milktea.milktea.DTO.TempInvoiceDetail;
import milktea.milktea.Util.ValidationUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SubGUI_InvoiceDetail {
    @FXML
    private TextField txtProductName;
    @FXML
    private TextField txtSugar;
    @FXML
    private TextField txtIce;
    @FXML
    private TextArea txtNote;
    @FXML
    private ComboBox<Product> cbTopping;
    @FXML
    private ScrollPane spTopping;
    @FXML
    private Button btnAdd;

    private final HashMap<String,Integer> topping = new HashMap<>();
    public void initialize() {
        addNumericValidation(txtIce);
        addNumericValidation(txtSugar);
        if (!InvoiceGUI.isEditable()) {
            txtIce.setText("100");
            txtSugar.setText("100");
            txtProductName.setText(InvoiceGUI.getSelectedProduct().getName());
        }else {
            txtIce.setText(String.valueOf(InvoiceGUI.getSelectedInvoiceDetail().getIce()));
            txtSugar.setText(String.valueOf(InvoiceGUI.getSelectedInvoiceDetail().getSugar()));
            txtProductName.setText(Objects.requireNonNull(Product_BUS.getProductById(InvoiceGUI.getSelectedInvoiceDetail().getInvoiceDetail().getProductId())).getName());
            topping.putAll(InvoiceGUI.getSelectedInvoiceDetail().getTopping());
            spTopping.setContent(toppingPortView());
            btnAdd.setText("Cập nhật chi tiết");
        }
        setupCpTopping();
        btnAdd.setOnAction(this::btnAdd);
    }

    private void btnAdd(ActionEvent actionEvent) {
        if (!ValidationUtil.isEmpty(txtIce, txtSugar)) {
            if (!InvoiceGUI.isEditable()) {
                InvoiceGUI.getInvoiceDetail().add(new TempInvoiceDetail(new InvoiceDetail(InvoiceGUI.getInvoiceId(), InvoiceGUI.getSelectedProduct().getProductId(), 1, InvoiceGUI.getSelectedProduct().getPrice(), InvoiceGUI.getSelectedProduct().getPrice()), Integer.parseInt(txtSugar.getText()), Integer.parseInt(txtIce.getText()), txtNote.getText(), topping));

            } else {
                InvoiceGUI.getSelectedInvoiceDetail().setIce(Integer.parseInt(txtIce.getText()));
                InvoiceGUI.getSelectedInvoiceDetail().setSugar(Integer.parseInt(txtSugar.getText()));
                InvoiceGUI.getSelectedInvoiceDetail().setNote(txtNote.getText());
                InvoiceGUI.getSelectedInvoiceDetail().setTopping(topping);
            }
            Stage stage = (Stage) btnAdd.getScene().getWindow();
            stage.close();
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng nhập đầy đủ thông tin");
            alert.showAndWait();
        }

    }
    boolean ignoreAction = false;
    public void setupCpTopping() {
        if (!InvoiceGUI.isEditable()||topping.isEmpty()){
            Product_BUS.getAllProduct().forEach(product -> {
                if (product.getCategoryId().equals("C005")&&product.getStatus().equals(Status.ACTIVE)) {
                    cbTopping.getItems().add(product);
                }
            });
        }else {
            Product_BUS.getAllProduct().forEach(product -> {
                    if (product.getCategoryId().equals("C005") && !topping.containsKey(product.getProductId()) &&product.getStatus().equals(Status.ACTIVE)) {
                        cbTopping.getItems().add(product);
                }
            });
        }

        cbTopping.setOnAction(event -> {
            if (cbTopping.getValue() != null) {
                if (topping.containsKey(cbTopping.getValue().getProductId())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Topping đã được chọn");
                    alert.setContentText("Vui lòng chọn topping khác");
                    alert.showAndWait();

                } else if (!Product_BUS.checkQuantityBelowZero(Product_BUS.getAllProduct(), cbTopping.getValue().getProductId())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Hết hàng");
                    alert.setContentText("Topping đã hết hàng");
                    alert.showAndWait();
                } else {
                    if (!ignoreAction) {
                        topping.put(cbTopping.getValue().getProductId(), 1);
                }                }
                // Đặt giá trị mà không kích hoạt sự kiện

                spTopping.setContent(toppingPortView());
                ignoreAction = true;
                javafx.application.Platform.runLater(() -> {
                    cbTopping.getSelectionModel().clearSelection();
                    cbTopping.setValue(null);
                    cbTopping.setPromptText("Chọn Topping");
                    cbTopping.requestLayout();
                });
                ignoreAction = false; // Khôi phục lại sự kiện

            }
        });
    }


    public GridPane toppingPortView(){
        GridPane toppingPaneGridPane = new GridPane();
        for (Map.Entry<String, Integer> toppingArr : topping.entrySet()) {
            GridPane itemPane = new GridPane();
            itemPane.setPadding(new Insets(5));
            String toppingName = Objects.requireNonNull(Product_BUS.getProductById(toppingArr.getKey())).getName();
            Label toppingLabel = new Label(toppingName);
            itemPane.add(toppingLabel, 0, 0);

            Button deleteButton = new Button("X");
            itemPane.setHgap(10);
            itemPane.setVgap(5);
            itemPane.add(deleteButton, 2, 0);
//            itemPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());
            toppingPaneGridPane.add(itemPane, 0, toppingPaneGridPane.getChildren().size());

            deleteButton.setOnAction(e -> {
                toppingPaneGridPane.getChildren().remove(itemPane);
                topping.remove(toppingArr.getKey());
            });
        }

        return toppingPaneGridPane;
    }

    private void addNumericValidation(TextField textField) {
    textField.textProperty().addListener((observable, oldValue, newValue) -> {
        if (!newValue.matches("\\d*")) {
            textField.setText(newValue.replaceAll("\\D", ""));
        } else {
            if (!newValue.isEmpty()) {
                if (newValue.length() > 3 || Integer.parseInt(newValue) > 100) {
                    textField.setText("100");
                }
            }
        }
    });
}
}
