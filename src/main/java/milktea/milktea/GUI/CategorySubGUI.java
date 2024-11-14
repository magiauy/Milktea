package milktea.milktea.GUI;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import milktea.milktea.BUS.Category_BUS;
import milktea.milktea.DTO.Category;
import milktea.milktea.Util.ValidationUtil;

public class CategorySubGUI {
    @FXML
    private TextField txtID;
    @FXML
    private TextField txtName;
    @FXML
    private Button btnSave;
    @FXML
    private Label lblTitle;

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            Stage stage = (Stage) btnSave.getScene().getWindow();
            stage.setTitle("Loại sản phẩm");
        });
        txtID.setDisable(true);
        if (CategoryGUI.isEditable()) {
            lblTitle.setText("Sửa loại sản phẩm");
            txtID.setText(CategoryGUI.getSelectedCategory().getId());
            txtName.setText(CategoryGUI.getSelectedCategory().getName());
        }else {
            lblTitle.setText("Thêm loại sản phẩm");
            txtID.setText(Category_BUS.autoID());
            txtName.setText("");
        }
        btnSave.setOnAction(e-> {
            if (ValidationUtil.isEmpty(txtName)) return;
            if (ValidationUtil.isInValidChar("Tên loại", txtName)) return;
            Category category = new Category(txtID.getText(), txtName.getText());
            if (CategoryGUI.isEditable()) {
                if (Category_BUS.editCategory(category)) {
                    Category_BUS.editCategoryLocal(category);
                    ValidationUtil.showInfoAlert("Sửa loại sản phẩm thành công");
                    Stage stage = (Stage) btnSave.getScene().getWindow();
                    stage.close();
                }else {
                    ValidationUtil.showErrorAlert("Sửa loại sản phẩm thất bại");
                }
            }else {
                if (Category_BUS.addCategory(category)) {
                    Category_BUS.addCategoryLocal(category);
                    ValidationUtil.showInfoAlert("Thêm loại sản phẩm thành công");
                    Stage stage = (Stage) btnSave.getScene().getWindow();
                    stage.close();
                }else {
                    ValidationUtil.showErrorAlert("Thêm loại sản phẩm thất bại");
                }
            }

        });
    }

}
