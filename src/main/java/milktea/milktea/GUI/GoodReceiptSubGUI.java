package milktea.milktea.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import milktea.milktea.BUS.GoodsReceipt_BUS;
import milktea.milktea.BUS.Ingredient_BUS;
import milktea.milktea.DTO.GoodsReceiptDetail;
import milktea.milktea.DTO.Unit;
import milktea.milktea.Util.ValidationUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class GoodReceiptSubGUI {
    @FXML
    Label lblIngredientName;
    @FXML
    TextField txtQuantity;
    @FXML
    TextField txtPrice;
    @FXML
    Label lblUnit;
    @FXML
    Label lblUnit1;
    @FXML
    Button btnSave;

    @Getter
    @Setter
    private static boolean isAdded = false;
    @FXML
    public void initialize() {
        if (!GoodsReceiptGUI.isEditable()) {
            lblIngredientName.setText(GoodsReceiptGUI.getIngredient().getName());
            lblUnit.setText("/"+setUnit(GoodsReceiptGUI.getIngredient().getUnit()));
            lblUnit1.setText(setUnit(GoodsReceiptGUI.getIngredient().getUnit()).name());
        }else {
            lblIngredientName.setText(Ingredient_BUS.getIngredientNameById(GoodsReceiptGUI.getGoodsReceiptDetail().getIngredientId()));
            lblUnit.setText("/"+setUnit(Ingredient_BUS.getIngredientById(GoodsReceiptGUI.getGoodsReceiptDetail().getIngredientId()).getUnit()));
            lblUnit1.setText(setUnit(Ingredient_BUS.getIngredientById(GoodsReceiptGUI.getGoodsReceiptDetail().getIngredientId()).getUnit()).name());
            txtQuantity.setText(String.valueOf(GoodsReceiptGUI.getGoodsReceiptDetail().getQuantity()));
            txtPrice.setText(String.valueOf(GoodsReceiptGUI.getGoodsReceiptDetail().getPrice()));
        }
        btnSave.setOnAction(event -> {
            if (!ValidationUtil.isEmpty(txtQuantity, txtPrice)) {
                if (!ValidationUtil.isNotPrice(txtPrice)) {
                    if (ValidationUtil.isFloat(txtQuantity)) {
                        if (!GoodsReceiptGUI.isEditable()) {
                            GoodsReceiptGUI.setGoodsReceiptDetail(new GoodsReceiptDetail());
                            GoodsReceiptGUI.getGoodsReceiptDetail().setUnit(setUnit(Ingredient_BUS.getIngredientById(GoodsReceiptGUI.getIngredient().getId()).getUnit()));
                        }else {
                            GoodsReceiptGUI.getGoodsReceiptDetail().setUnit(setUnit(Ingredient_BUS.getIngredientById(GoodsReceiptGUI.getGoodsReceiptDetail().getIngredientId()).getUnit()));
                        }
                        GoodsReceiptGUI.getGoodsReceiptDetail().setQuantity(Float.parseFloat(txtQuantity.getText()));
                        GoodsReceiptGUI.getGoodsReceiptDetail().setPrice(new BigDecimal(txtPrice.getText()).setScale( 0, RoundingMode.HALF_UP));
                            GoodsReceiptGUI.getGoodsReceiptDetail().setTotal(new BigDecimal(txtPrice.getText()).multiply(new BigDecimal(txtQuantity.getText()).setScale( 0, RoundingMode.HALF_UP)));
                            isAdded = true;
                            Stage stage = (Stage) btnSave.getScene().getWindow();
                            stage.close();
                    }
                }
            }
        });
    }

    public Unit setUnit(Unit unit) {
        if (unit.equals(Unit.GRAM)||unit.equals(Unit.KILOGRAM)) {
            return Unit.KILOGRAM;
        }else if (unit.equals(Unit.MILLILITER)||unit.equals(Unit.LITER)) {
            return Unit.LITER;
    }
        return null;
    }

}
