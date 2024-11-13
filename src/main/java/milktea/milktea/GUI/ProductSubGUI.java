package milktea.milktea.GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import milktea.milktea.BUS.Category_BUS;
import milktea.milktea.BUS.Product_BUS;
import milktea.milktea.DTO.Category;
import milktea.milktea.DTO.Product;
import milktea.milktea.DTO.Recipe;
import milktea.milktea.BUS.Recipe_BUS;
import milktea.milktea.DTO.Status;
import milktea.milktea.Util.ValidationUtil;

import java.math.BigDecimal;
import java.util.ArrayList;

import static milktea.milktea.Util.UI_Util.openStage;

public class ProductSubGUI {
    public static boolean isEditable = false;
    @Getter
    private static Recipe selectedRecipe = null;
    @FXML
    private TextField txtProductID;
    @FXML
    private TextField txtProductName;
    @FXML
    private ComboBox<Category> cbCategory;
    @FXML
    private TextField txtPrice;
    @FXML
    private ImageView imgEdit;
    @FXML
    private ImageView imgDelete;
    @FXML
    private ImageView imgAdd;
    @FXML
    private TableView<Recipe> tblRecipe;
    @FXML
    private TableColumn<Recipe, String> colIngredientName;
    @FXML
    private TableColumn<Recipe, Float> colQuantity;
    @FXML
    private TableColumn<Recipe, String> colUnit;
    @FXML
    private Button btnSave;
    @FXML
    private Label lblTitle;
    @Setter
    @Getter
    private static ArrayList<Recipe> arrRecipe ;
    @Getter
    private static String productID ;
    @Setter
    @Getter
    private static Product product;
    @Setter
    @Getter
    private static boolean isEdited = false;
    @Setter
    @Getter
    private static ArrayList<Recipe> removedRecipe = new ArrayList<>();


    public void initialize() {
        arrRecipe = new ArrayList<>();
        product = new Product();
        loadCategory();
        createTable();
        if (ProductGUI.isEditable) {
            productID = ProductGUI.getSelectedProduct().getProductId();
            btnSave.setText("Cập nhật");
            lblTitle.setText("Sửa sản phẩm");
            arrRecipe = (ArrayList<Recipe>) Recipe_BUS.getRecipeByProductID(ProductGUI.getSelectedProduct().getProductId());
            txtProductID.setText(ProductGUI.getSelectedProduct().getProductId());
            txtProductName.setText(ProductGUI.getSelectedProduct().getName());
            txtPrice.setText(String.valueOf(ProductGUI.getSelectedProduct().getPrice()));
            cbCategory.setValue(Category_BUS.getCategoryById(ProductGUI.getSelectedProduct().getCategoryId()));
            ObservableList<Recipe> list = FXCollections.observableArrayList(arrRecipe);
            tblRecipe.setItems(list);

        } else {
            btnSave.setText("Thêm");
            lblTitle.setText("Thêm sản phẩm");
            productID = Product_BUS.autoId();
            txtProductID.setText(productID);
        }
        imgAdd.setOnMouseClicked(event -> openStage("Recipe_SubGUI.fxml",()->{
            if (RecipeSubGUI.isEdited()) {
                arrRecipe.add(RecipeSubGUI.getRecipe());
                ObservableList<Recipe> list = FXCollections.observableArrayList(arrRecipe);
                tblRecipe.setItems(list);
                RecipeSubGUI.setRecipe(null);
                RecipeSubGUI.setEdited(false);
            }
        }));
        imgEdit.setOnMouseClicked(event -> {
            if (tblRecipe.getSelectionModel().getSelectedItem() != null) {
                isEditable = true;
                selectedRecipe = tblRecipe.getSelectionModel().getSelectedItem();
                openStage("Recipe_SubGUI.fxml",()->{
                    if (RecipeSubGUI.isEdited()) {
                        arrRecipe.set(tblRecipe.getSelectionModel().getSelectedIndex(), RecipeSubGUI.getRecipe());
                        ObservableList<Recipe> list = FXCollections.observableArrayList(arrRecipe);
                        tblRecipe.setItems(list);
                        RecipeSubGUI.setRecipe(null);
                        RecipeSubGUI.setEdited(false);
                    }
                    isEditable = false;
                    tblRecipe.getSelectionModel().clearSelection();
                    selectedRecipe = null;
                });
            }else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng chọn một công thức để sửa");
                alert.showAndWait();
            }
        });
        imgDelete.setOnMouseClicked(event -> {
            if (tblRecipe.getSelectionModel().getSelectedItem() != null) {
                if (showAlert()) {
                    removedRecipe.add(tblRecipe.getSelectionModel().getSelectedItem());
                    arrRecipe.remove(tblRecipe.getSelectionModel().getSelectedItem());
                    ObservableList<Recipe> list = FXCollections.observableArrayList(arrRecipe);
                    tblRecipe.setItems(list);
                }
            }else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng chọn một công thức để xóa");
                alert.showAndWait();
            }
        });
        btnSave.setOnAction(event -> {
            if (ValidationUtil.isEmpty(txtProductName, txtPrice)) return;
            if (ValidationUtil.isNotPrice(txtPrice)) return;
            if (arrRecipe.isEmpty()) {
                ValidationUtil.showErrorAlert("Vui lòng thêm công thức cho sản phẩm");
                return;
            }

            if (ProductGUI.isEditable) {
                product = ProductGUI.getSelectedProduct();
                product.setName(txtProductName.getText());
                product.setCategoryId(cbCategory.getValue().getId());
                product.setPrice(BigDecimal.valueOf(Float.parseFloat(txtPrice.getText())));
                product.setStatus(Status.ACTIVE);
            } else {
                product = new Product(productID, txtProductName.getText(), cbCategory.getValue().getId(), BigDecimal.valueOf(Float.parseFloat(txtPrice.getText())), Status.ACTIVE);
            }

            isEdited = true;
            Stage stage = (Stage) btnSave.getScene().getWindow();
            stage.close();
        });
    }

    private boolean showAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Bạn có chắc chắn muốn xóa công thức này?");
        alert.showAndWait();
        return alert.getResult().getText().equals("OK");
    }

    public void createTable() {
        colIngredientName.setCellValueFactory(new PropertyValueFactory<>("ingredientName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
    }

    public void loadCategory() {
        cbCategory.getItems().addAll(Category_BUS.getAllCategory());
    }

}
