package milktea.milktea.GUI;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import milktea.milktea.BUS.Invoice_BUS;
import milktea.milktea.BUS.PromotionProgram_BUS;
import milktea.milktea.DTO.Promotion;
import milktea.milktea.DTO.PromotionProgram;
import milktea.milktea.BUS.Promotion_BUS;
import milktea.milktea.Util.ValidationUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import static milktea.milktea.Util.UI_Util.openStage;


public class PromotionProgramSubGUI {
    @FXML
    private Label lblTitle;
    @FXML
    private TextField txtID;
    @FXML
    private TextField txtName;
    @FXML
    private DatePicker dpStartDate;
    @FXML
    private DatePicker dpEndDate;

    @FXML
    private ImageView imgAdd;
    @FXML
    private ImageView imgEdit;
    @FXML
    private ImageView imgDelete;

    @FXML
    private TableView<Promotion> tblPromotion;
    @FXML
    private TableColumn<Promotion, String> colPromotionID;
    @FXML
    private TableColumn<Promotion, String> colDiscount;
    @FXML
    private TableColumn<Promotion, String> colMinimumPrice;

    @FXML
    private Button btnSave;

    @Getter
    @Setter
    private static ArrayList<Promotion> currentPromotions ;
    @Getter
    @Setter
    private static ArrayList<Promotion> removedPromotions ;
    @Getter
    @Setter
    private static ArrayList<Promotion> addedPromotions ;
    private PromotionProgram promotionProgram;

    @Getter
    @Setter
    private static Promotion selectedPromotion;
    @Getter
    @Setter
    private static boolean isEditable = false;
    @Getter
    @Setter
    private static String promotionProgramID;

    @Getter
    @Setter
    private static boolean isEdited = false;
    private final HashMap<TextField,String> textFieldInfo = new HashMap<>();
    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            Stage stage = (Stage) btnSave.getScene().getWindow();
            stage.setTitle("Thông tin khuyến mãi");
        });
        removedPromotions = new ArrayList<>();
        addedPromotions = new ArrayList<>();
        initTable();
        setupHashMap();
        if  (PromotionGUI.isEditable()){
            promotionProgram = PromotionGUI.getSelectedPromotionProgram();
            lblTitle.setText("Sửa khuyến mãi");
            promotionProgramID = promotionProgram.getPromotionProgramId();
            txtID.setText(promotionProgramID);
            txtName.setText(promotionProgram.getName());
            dpStartDate.setValue(promotionProgram.getStartDate());
            dpEndDate.setValue(promotionProgram.getEndDate());
            currentPromotions = new ArrayList<>(Promotion_BUS.getPromotionsByID(promotionProgram.getPromotionProgramId()));
            tblPromotion.setItems(FXCollections.observableList(currentPromotions));
        } else {
            lblTitle.setText("Thêm khuyến mãi");
            promotionProgram = new PromotionProgram();
            promotionProgramID = PromotionProgram_BUS.autoID();
            txtID.setText(promotionProgramID);
            currentPromotions = new ArrayList<>();

        }

        btnSave.setOnAction(this::btnSave);
        imgAdd.setOnMouseClicked(this::imgAdd);
        imgEdit.setOnMouseClicked(this::imgEdit);
        imgDelete.setOnMouseClicked(this::imgDelete);
    }

    private void imgDelete(MouseEvent event) {
        if (tblPromotion.getSelectionModel().getSelectedItem() == null){
            ValidationUtil.showErrorAlert("Chọn khuyến mãi cần xóa");
            return;
        }
        Promotion promotion = tblPromotion.getSelectionModel().getSelectedItem();
        if (!addedPromotions.contains(promotion)){
            removedPromotions.add(promotion);
            currentPromotions.remove(promotion);
        }else {
            addedPromotions.remove(promotion);
        }
        tblPromotion.setItems(FXCollections.observableList(currentPromotions));
    }

    private void imgEdit(MouseEvent event) {
        if (tblPromotion.getSelectionModel().getSelectedItem() == null){
            ValidationUtil.showErrorAlert("Chọn khuyến mãi cần sửa");
            return;
        }
        selectedPromotion = tblPromotion.getSelectionModel().getSelectedItem();
        isEditable = true;
        openStage("Promotion_SubGUI.fxml",()->{
            if (PromotionSubGUI.isEdited()){
                Promotion promotion = PromotionSubGUI.getPromotion();
                if (!addedPromotions.contains(promotion)){
                    currentPromotions.set(currentPromotions.indexOf(selectedPromotion),promotion);
                }else {
                    addedPromotions.set(addedPromotions.indexOf(promotion),promotion);
                }
                refreshTable();
                PromotionSubGUI.setPromotion(null);
                PromotionSubGUI.setEdited(false);
                selectedPromotion = null;
                isEditable = false;
            }
        });
    }

    private void imgAdd(MouseEvent event) {
        openStage("Promotion_SubGUI.fxml",()->{
            if (PromotionSubGUI.isEdited()){
                Promotion promotion = PromotionSubGUI.getPromotion();
                addedPromotions.add(promotion);
                refreshTable();
                PromotionSubGUI.setPromotion(null);
                PromotionSubGUI.setEdited(false);
            }
        });
    }

    public void refreshTable(){
        ArrayList<Promotion> promotions = new ArrayList<>(currentPromotions);
        promotions.addAll(addedPromotions);
        promotions.removeAll(removedPromotions);
        tblPromotion.setItems(FXCollections.observableList(promotions));
        tblPromotion.refresh();
    }
    public void setupHashMap(){
        textFieldInfo.put(txtID,"ID");
        textFieldInfo.put(txtName,"Tên CTKM");
    }

    private void btnSave(ActionEvent actionEvent) {
        if (ValidationUtil.isEmpty(txtName)) return;
        if (ValidationUtil.isEmptyDp(dpStartDate,"Ngày bắt đầu")) return;
        if (ValidationUtil.isEmptyDp(dpEndDate,"Ngày kết thúc")) return;
        if (dpStartDate.getValue().isAfter(dpEndDate.getValue())){
            ValidationUtil.showErrorAlert("Ngày bắt đầu không thể sau ngày kết thúc");
            return;
        }
        if (dpStartDate.getValue().isBefore(LocalDate.now())){
            ValidationUtil.showErrorAlert("Ngày bắt đầu không thể trước ngày hiện tại");
            return;
        }
        if (ValidationUtil.isInValidChar(textFieldInfo,txtName)) return;
        ArrayList<Promotion> promotions = new ArrayList<>(currentPromotions);
        promotions.addAll(addedPromotions);
        promotions.removeAll(removedPromotions);
        if (promotions.isEmpty()){
            ValidationUtil.showErrorAlert("Không thể lưu khi chưa có khuyến mãi");
            return;
        }
        promotionProgram.setPromotionProgramId(txtID.getText());
        promotionProgram.setName(txtName.getText());
        promotionProgram.setStartDate(dpStartDate.getValue());
        promotionProgram.setEndDate(dpEndDate.getValue());
        if (PromotionGUI.isEditable()){
            if (!PromotionProgram_BUS.editPromotionProgram(promotionProgram)||
                    !Promotion_BUS.removePromotion(removedPromotions)||
                    !Promotion_BUS.editPromotion(currentPromotions)||
                    !Promotion_BUS.addPromotion(addedPromotions)){
                return;
            }else {
                PromotionProgram_BUS.editPromotionProgramLocal(promotionProgram);
                Promotion_BUS.removePromotionLocal(removedPromotions);
                Promotion_BUS.editPromotionLocal(currentPromotions);
                Promotion_BUS.addPromotionLocal(addedPromotions);
            }
        } else {
            if (!PromotionProgram_BUS.addPromotionProgram(promotionProgram)
            || !Promotion_BUS.addPromotion(addedPromotions)){
                return;
            }else {
                PromotionProgram_BUS.addPromotionProgramLocal(promotionProgram);
                Promotion_BUS.addPromotionLocal(addedPromotions);
            }
        }
        isEdited = true;
        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();

    }

    public void initTable(){
        colPromotionID.setCellValueFactory(new PropertyValueFactory<>("promotionId"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colMinimumPrice.setCellValueFactory(new PropertyValueFactory<>("minimumPrice"));
    }
}
