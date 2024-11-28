package milktea.milktea.GUI;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import milktea.milktea.BUS.Category_BUS;
import milktea.milktea.DTO.Category;
import milktea.milktea.Util.ValidationUtil;
import milktea.milktea.BUS.Product_BUS;
import javafx.application.Platform;


import static milktea.milktea.Util.UI_Util.openStage;


public class CategoryGUI {
    @FXML
    private TableView<Category> tblCategory;
    @FXML
    private TableColumn<Category, String> colID;
    @FXML
    private TableColumn<Category, String> colName;

    @FXML
    private ImageView imgAdd;
    @FXML
    private ImageView imgEdit;
    @FXML
    private ImageView imgDelete;

    @FXML
    private Button btnCancel;

    @Getter
    @Setter
    private static boolean isEditable = false;
    @Getter
    @Setter
    private static Category selectedCategory;
    @FXML
    public void initialize() {
        selectedCategory = null;
        Platform.runLater(() -> {
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.setTitle("Quản lý loại sản phẩm");
        });
        loadCategory();
        imgAdd.setOnMouseClicked(event -> {
            openStage("Category_SubGUI.fxml", this::loadCategory);
        });

        imgEdit.setOnMouseClicked(e->{
            if (tblCategory.getSelectionModel().getSelectedItem() == null) {
                ValidationUtil.showErrorAlert("Vui lòng chọn loại sản phẩm cần sửa");
                return;
            }
            selectedCategory = tblCategory.getSelectionModel().getSelectedItem();
            isEditable = true;
            openStage("Category_SubGUI.fxml",()->{
                isEditable = false;
                selectedCategory = null;
                tblCategory.getSelectionModel().clearSelection();
                loadCategory();
            });
        });

        imgDelete.setOnMouseClicked(e->{
            if (tblCategory.getSelectionModel().getSelectedItem() == null) {
                ValidationUtil.showErrorAlert("Vui lòng chọn loại sản phẩm cần xóa");
                return;
            }
            if (!ValidationUtil.showConfirmAlert("Bạn có chắc chắn muốn xóa loại sản phẩm này không?")) {
                return;
            }
            if (Product_BUS.isCategoryExist(tblCategory.getSelectionModel().getSelectedItem().getId())) {
                ValidationUtil.showErrorAlert("Không thể xóa loại sản phẩm này vì đã có sản phẩm thuộc loại sản phẩm này");
                return;
            }
            if (Category_BUS.deleteCategory(tblCategory.getSelectionModel().getSelectedItem().getId())) {
                Category_BUS.deleteCategoryLocal(tblCategory.getSelectionModel().getSelectedItem().getId());
                loadCategory();
                ValidationUtil.showInfoAlert("Xóa loại sản phẩm thành công");
            } else {
                ValidationUtil.showErrorAlert("Xóa loại sản phẩm thất bại");
            }
        });
        btnCancel.setOnAction(e-> {
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        });
    }

    public void loadCategory() {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tblCategory.setItems(FXCollections.observableArrayList(Category_BUS.getAllCategory()));
        tblCategory.refresh();
    }
}
