package milktea.milktea.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import milktea.milktea.BUS.Ingredient_BUS;
import milktea.milktea.DTO.Ingredient;
import milktea.milktea.DTO.Recipe;
import milktea.milktea.DTO.Unit;
import milktea.milktea.Util.ValidationUtil;


public class RecipeSubGUI {
    @FXML
    private Label lblTitle;
    @FXML
    private ComboBox<Ingredient> cbIngredient;
    @FXML
    private ComboBox<String> cbUnit;
    @FXML
    private TextField txtQuantity;
    @FXML
    private Button btnSave;
    @Setter
    @Getter
    private static Recipe recipe ;
    @Setter
    @Getter
    private static boolean isEdited = false;
    public void initialize() {
        recipe = new Recipe();
        loadIngredient();
        if (ProductSubGUI.isEditable) {
            btnSave.setText("Cập nhật");
            lblTitle.setText("Sửa Công thức");
            txtQuantity.setText(String.valueOf(ProductSubGUI.getSelectedRecipe().getQuantity()));
            cbIngredient.setValue(Ingredient_BUS.getIngredientById(ProductSubGUI.getSelectedRecipe().getIngredientId()));
            cbIngredient.setDisable(true);
            cbUnit.setValue(String.valueOf(ProductSubGUI.getSelectedRecipe().getUnit()));
            if (cbIngredient.getValue() != null) {
                loadUnit();
            }
        }else {
            btnSave.setText("Thêm");
            lblTitle.setText("Thêm Công thức");
        }
        btnSave.setOnAction(event -> {
            if(!ValidationUtil.isEmptyComboBox(cbIngredient,cbUnit)){
                if (!ValidationUtil.isEmpty(txtQuantity)) {
                    recipe.setProductId(ProductSubGUI.getProductID());
                    recipe.setIngredientId(cbIngredient.getValue().getId());
                    recipe.setQuantity(Float.parseFloat(txtQuantity.getText()));
                    recipe.setUnit(Unit.valueOf(cbUnit.getValue()));
                    recipe.setIngredientName(cbIngredient.getValue().getName());
                    Stage stage = (Stage) btnSave.getScene().getWindow();
                    isEdited = true;
                    stage.close();
                }
            }
        });
    }

    private void loadUnit() {
        if (cbIngredient.getValue() != null) {
            if (cbIngredient.getValue().getUnit().equals(Unit.GRAM)||cbIngredient.getValue().getUnit().equals(Unit.KILOGRAM)) {
                cbUnit.getItems().clear();
                cbUnit.getItems().addAll(Unit.GRAM.toString(), Unit.KILOGRAM.toString());
            }
                else {
                    cbUnit.getItems().clear();
                    cbUnit.getItems().addAll(Unit.MILLILITER.toString(),Unit.LITER.toString());
                }
        }
    }

    private void loadIngredient() {
      cbIngredient.getItems().clear();
        cbIngredient.getItems().addAll(Ingredient_BUS.getIngredientsExcludeIds(ProductSubGUI.getArrRecipe()));
        cbIngredient.setOnAction(event -> {
            cbUnit.getItems().clear();
                if (cbIngredient.getValue() != null) {
                    loadUnit();
                }
        });
    }

}
