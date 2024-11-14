package milktea.milktea.GUI;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import milktea.milktea.BUS.Ingredient_BUS;
import milktea.milktea.DTO.Ingredient;
import milktea.milktea.DTO.Status;
import milktea.milktea.DTO.Unit;
import milktea.milktea.Util.ValidationUtil;

public class IngredientSubGUI {
    @FXML
    TextField txtId;
    @FXML
    TextField txtName;
    @FXML
    ComboBox<Unit> cbUnit;
    @FXML
    TextField txtQuantity;
    @FXML
    Button btnSave;

    @Getter
    @Setter
    private static boolean isEdited = false;

    @Setter
    @Getter
    private static Ingredient ingredient;
    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            Stage stage = (Stage) btnSave.getScene().getWindow();
            stage.setTitle("Thông tin nguyên liệu");
        });
        cbUnit.getItems().addAll(Unit.values());
        if (!IngredientGUI.isEditable()){
            ingredient = new Ingredient();
            System.out.println("Add Ingredient");
            txtId.setText(Ingredient_BUS.autoId());
            txtQuantity.setText("0");
            txtQuantity.setDisable(true);
        }else{
            System.out.println("Edit Ingredient");
            ingredient = new Ingredient(IngredientGUI.getSelectedIngredient());
            txtId.setText(ingredient.getId());
            txtName.setText(ingredient.getName());
            cbUnit.setValue(ingredient.getUnit());
            txtQuantity.setText(String.valueOf(ingredient.getQuantity()));
        }
        btnSave.setOnMouseClicked(event -> {
            if (ValidationUtil.isEmpty(txtName, txtQuantity) || ValidationUtil.isEmptyComboBox(cbUnit)) return;
            if (!ValidationUtil.isFloat(txtQuantity)) return;
            if (!ValidationUtil.isNegativeFloat(Float.parseFloat(txtQuantity.getText()))) return;

            ingredient.setId(txtId.getText());
            ingredient.setName(txtName.getText());
            ingredient.setUnit(cbUnit.getValue());
            ingredient.setQuantity(Float.parseFloat(txtQuantity.getText()));

            if (IngredientGUI.isEditable()) {
                if (Ingredient_BUS.editIngredient(ingredient)) {
                    Ingredient_BUS.editIngredientLocal(ingredient);
                    IngredientSubGUI.setEdited(true);
                    ValidationUtil.showInfoAlert("Nguyên liệu đã được chỉnh sửa");
                    isEdited = true;
                } else {
                    ValidationUtil.showErrorAlert("Chỉnh sửa nguyên liệu thất bại");
                }
            } else {
                ingredient.setStatus(Status.ACTIVE);
                if (Ingredient_BUS.addIngredient(ingredient)) {
                    Ingredient_BUS.addIngredientLocal(ingredient);
                    IngredientSubGUI.setEdited(true);
                    ValidationUtil.showInfoAlert("Nguyên liệu đã được thêm");
                    isEdited = true;
                }
            }
            IngredientGUI.setSelectedIngredient(null);
            Stage stage = (Stage) btnSave.getScene().getWindow();
            stage.close();
        });
    }
}
