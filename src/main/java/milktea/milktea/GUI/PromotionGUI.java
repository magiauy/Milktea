package milktea.milktea.GUI;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;
import milktea.milktea.BUS.Invoice_BUS;
import milktea.milktea.BUS.Promotion_BUS;
import milktea.milktea.DTO.Promotion;
import milktea.milktea.DTO.PromotionProgram;
import milktea.milktea.BUS.PromotionProgram_BUS;
import milktea.milktea.Util.ValidationUtil;

import java.time.LocalDate;
import java.util.ArrayList;

import static milktea.milktea.Util.UI_Util.openStage;

public class PromotionGUI {
    @FXML
    TableView<PromotionProgram> tblPromotionProgram;
    @FXML
    TableColumn<PromotionProgram, String> colID;
    @FXML
    TableColumn<PromotionProgram, String> colName;
    @FXML
    TableColumn<PromotionProgram, String> colStartDate;
    @FXML
    TableColumn<PromotionProgram, String> colEndDate;

    @FXML
    ImageView imgAdd;
    @FXML
    ImageView imgEdit;
    @FXML
    ImageView imgDelete;

    @FXML
    Button btnSearch;
    @FXML
    Button btnClear;

    @FXML
    TextField txtSearch;
    @FXML
    TextField txtPromotionProgramName;
    @FXML
    TextField txtPromotionProgramStartDate;
    @FXML
    TextField txtPromotionProgramEndDate;

    @FXML
    Label lblTitle;

    @FXML
    TableView<Promotion> tblPromotion;
    @FXML
    TableColumn<Promotion, String> colPromotionID;
    @FXML
    TableColumn<Promotion, String> colDiscount;
    @FXML
    TableColumn<Promotion, String> colMinimumPrice;

    @Getter
    @Setter
    private static PromotionProgram selectedPromotionProgram;
    @Getter
    @Setter
    private static boolean isEditable = false;

    @FXML
    public void initialize() {
        loadPromotionProgram();
        colPromotionID.setCellValueFactory(new PropertyValueFactory<>("promotionId"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colMinimumPrice.setCellValueFactory(new PropertyValueFactory<>("minimumPrice"));

        imgAdd.setOnMouseClicked(event -> {
            openStage("PromotionProgram_SubGUI.fxml", () -> {
                if (PromotionProgramSubGUI.isEdited()) {
                    loadPromotionProgram();
                    PromotionProgramSubGUI.setEdited(false);
                }
            });
        });

        imgEdit.setOnMouseClicked(event -> {
            if (selectedPromotionProgram == null) {
                ValidationUtil.showErrorAlert("Chọn chương trình khuyến mãi cần sửa");
                return;
            }
            if (selectedPromotionProgram.getEndDate().isAfter(LocalDate.now())){
                ValidationUtil.showErrorAlert("Không thể sửa chương trình khuyến mãi đã kết thúc");
                return;
            }
            isEditable = true;
            openStage("PromotionProgram_SubGUI.fxml", () -> {
                if (PromotionProgramSubGUI.isEdited()) {
                    loadPromotionProgram();
                    isEditable = false;
                    PromotionProgramSubGUI.setEdited(false);
                }
            });
        });

        imgDelete.setOnMouseClicked(event -> {
            if (selectedPromotionProgram == null) {
                return;
            }
            if (!ValidationUtil.showConfirmAlert("Bạn có chắc chắn muốn xóa chương trình khuyến mãi này không?")) return;

            if (Invoice_BUS.isPromotionExist(Promotion_BUS.getPromotionsByID(selectedPromotionProgram.getPromotionProgramId()))) {
                ValidationUtil.showErrorAlert("Không thể xóa chương trình khuyến mãi này vì đã có hóa đơn sử dụng");
                return;
            }
            if (!Promotion_BUS.removePromotion(Promotion_BUS.getPromotionsByID(selectedPromotionProgram.getPromotionProgramId()))
                ||!PromotionProgram_BUS.deletePromotionProgram(selectedPromotionProgram.getPromotionProgramId())) {
                return;
            }
            Promotion_BUS.removePromotionLocal(Promotion_BUS.getPromotionsByID(selectedPromotionProgram.getPromotionProgramId()));
            PromotionProgram_BUS.removePromotionProgramLocal(selectedPromotionProgram.getPromotionProgramId());
            btnClear.fire();
            loadPromotionProgram();
        });
        btnClear.setOnAction(this::btnClear);
        btnSearch.setOnAction(this::btnSearch);
    }

    private void btnSearch(ActionEvent actionEvent) {
        if (ValidationUtil.isInvalidSearch(txtSearch)) {
            return;
        }
        if (txtSearch.getText().isEmpty()) {
            loadPromotionProgram();
            return;
        }
        ArrayList<PromotionProgram> pg = PromotionProgram_BUS.searchPromotionProgram(txtSearch.getText());
        if (pg.isEmpty()) {
            ValidationUtil.showErrorAlert("Không tìm thấy chương trình khuyến mãi");
            return;
        }
        pg.removeIf(promotionProgram -> promotionProgram.getPromotionProgramId().equals("CTKM0000"));
        tblPromotionProgram.setItems(FXCollections.observableArrayList(pg));
        tblPromotionProgram.refresh();
    }

    private void btnClear(ActionEvent actionEvent) {
        txtPromotionProgramName.clear();
        txtPromotionProgramStartDate.clear();
        txtPromotionProgramEndDate.clear();
        lblTitle.setText("");
        tblPromotion.setItems(FXCollections.observableArrayList());
        tblPromotionProgram.getSelectionModel().clearSelection();
    }

    public void loadPromotionProgram() {
        colID.setCellValueFactory(new PropertyValueFactory<>("promotionProgramId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        ArrayList<PromotionProgram> promotionPrograms = new ArrayList<>(PromotionProgram_BUS.getAllPromotionProgram());
        promotionPrograms.removeIf(promotionProgram -> promotionProgram.getPromotionProgramId().equals("CTKM0000"));
        tblPromotionProgram.setItems(FXCollections.observableArrayList(promotionPrograms));
        tblPromotionProgram.setOnMouseClicked(event -> {
            if (tblPromotionProgram.getSelectionModel().getSelectedItem() != null) {
                selectedPromotionProgram = tblPromotionProgram.getSelectionModel().getSelectedItem();
                tblPromotion.setItems(FXCollections.observableArrayList(Promotion_BUS.getPromotionsByID(selectedPromotionProgram.getPromotionProgramId())));
                txtPromotionProgramName.setText(selectedPromotionProgram.getName());
                txtPromotionProgramStartDate.setText(selectedPromotionProgram.getStartDate().toString());
                txtPromotionProgramEndDate.setText(selectedPromotionProgram.getEndDate().toString());
                lblTitle.setText(selectedPromotionProgram.getPromotionProgramId());
            }
        });
        tblPromotionProgram.refresh();
    }
}
