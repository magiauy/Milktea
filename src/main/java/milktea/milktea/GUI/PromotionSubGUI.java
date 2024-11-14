package milktea.milktea.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import milktea.milktea.BUS.Invoice_BUS;
import milktea.milktea.BUS.Promotion_BUS;
import milktea.milktea.DTO.Promotion;
import milktea.milktea.Util.ValidationUtil;

import java.math.BigDecimal;
import java.util.ArrayList;

public class PromotionSubGUI {
    @FXML
    private Label lblTitle;
    @FXML
    private TextField txtID;
    @FXML
    private TextField txtDiscount;
    @FXML
    private TextField txtMinimumPrice;
    @FXML
    private Button btnSave;

    @Getter
    @Setter
    private static boolean isEdited = false;
    @Getter
    @Setter
    private static Promotion promotion;

    @FXML
    public void initialize() {
        promotion = new Promotion();
        if (PromotionProgramSubGUI.isEditable()) {
            lblTitle.setText("Sửa Mã Khuyến Mãi");
            txtID.setText(PromotionProgramSubGUI.getSelectedPromotion().getPromotionId());
            txtDiscount.setText(String.valueOf(PromotionProgramSubGUI.getSelectedPromotion().getDiscount()));
            txtMinimumPrice.setText(String.valueOf(PromotionProgramSubGUI.getSelectedPromotion().getMinimumPrice()));
            txtID.setDisable(Invoice_BUS.isPromotionExist(PromotionProgramSubGUI.getSelectedPromotion().getPromotionId()));
        } else {
            lblTitle.setText("Thêm Mã Khuyến Mãi");
        }
        btnSave.setOnAction(event -> {
            if (ValidationUtil.isEmpty(txtID, txtDiscount, txtMinimumPrice)) return;
            if (ValidationUtil.isNotPrice(txtDiscount, txtMinimumPrice)) return;
            if (ValidationUtil.isFirstCharNotSpace("Mã Khuyến mãi",txtID)) return;
            if (ValidationUtil.isInvalidSearch("Mã Khuyến mãi",txtID)) return;
            ArrayList<Promotion> promotions = new ArrayList<>(PromotionProgramSubGUI.getCurrentPromotions());
            promotions.addAll(PromotionProgramSubGUI.getAddedPromotions());
            promotions.removeAll(PromotionProgramSubGUI.getRemovedPromotions());
            promotions.addAll(new ArrayList<>(Promotion_BUS.getAllPromotion()));


            if (PromotionProgramSubGUI.isEditable()) {
                int currentIndex = promotions.indexOf(promotion);
                if (promotions.stream().anyMatch(p -> promotions.indexOf(p) != currentIndex && p.getPromotionId().equals(txtID.getText()))) {
                    ValidationUtil.showErrorAlert("Mã Khuyến Mãi đã tồn tại");
                    return;
                }

            } else {
                if (promotions.stream().anyMatch(p -> p.getPromotionId().equals(txtID.getText()))) {
                    ValidationUtil.showErrorAlert("Mã Khuyến Mãi đã tồn tại");
                    return;
                }
            }
            promotion.setPromotionProgramId(PromotionProgramSubGUI.getPromotionProgramID());
            promotion.setPromotionId(txtID.getText());
            promotion.setDiscount(BigDecimal.valueOf(Float.parseFloat(txtDiscount.getText())));
            promotion.setMinimumPrice(BigDecimal.valueOf(Float.parseFloat(txtMinimumPrice.getText())));

            isEdited = true;
            PromotionProgramSubGUI.setEditable(false);
            Stage stage = (Stage) btnSave.getScene().getWindow();
            stage.close();
        });
    }
}
