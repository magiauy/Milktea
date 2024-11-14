package milktea.milktea.GUI;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;
import milktea.milktea.BUS.*;
import milktea.milktea.DTO.Product;
import milktea.milktea.DTO.Recipe;
import milktea.milktea.DTO.Status;
import milktea.milktea.Util.ValidationUtil;

import static milktea.milktea.Util.UI_Util.openStage;
import java.util.ArrayList;
import java.util.Objects;

public class ProductGUI {
    @FXML
    private TableView<Product> tblProduct;
    @FXML
    private TableColumn<Product, String> colID;
    @FXML
    private TableColumn<Product, String> colName;
    @FXML
    private TableColumn<Product, String> colCategoryID;
    @FXML
    private TableColumn<Product, Integer> colPrice;
    @FXML
    private TableColumn<Product, Integer> colStatus;
    @FXML
    TableView<Recipe> tblRecipe;
    @FXML
    TableColumn<Recipe, String> colIngredientName;
    @FXML
    TableColumn<Recipe, Float> colQuantity;
    @FXML
    TableColumn<Recipe, String> colUnit;

    @FXML
    TextField txtSearch;
    @FXML
    TextField txtProductName;
    @FXML
    TextField txtCategoryID;
    @FXML
    TextField txtPrice;
    @FXML
    TextField txtStatus;
    @FXML
    Label lblProductID;

    @FXML
    ImageView imgEdit;
    @FXML
    ImageView imgDelete;
    @FXML
    ImageView imgAdd;
    @FXML
    ImageView imgLock;
    @FXML
    ImageView imgRefresh;

    @FXML
    Button btnSearch;
    @FXML
    Button btnClear;

    @FXML
    Button btnCategory;
    public static boolean isEditable = false;

    @Getter
    private static Product selectedProduct = null;

    public void initialize() {
        createProductTable();
        createRecipeTable();
        btnSearch.setOnAction(event -> {
            String search = txtSearch.getText();
            ObservableList<Product> data = FXCollections.observableArrayList(Product_BUS.searchProduct(search));
            tblProduct.setItems(data);
        });
        btnClear.setOnAction(this::onClear);
        hideButtonWithoutPermission();
        imgEdit.setOnMouseClicked(event -> {
            selectedProduct = tblProduct.getSelectionModel().getSelectedItem();
            if (selectedProduct == null) {
                ValidationUtil.showErrorAlert("Vui lòng chọn sản phẩm cần sửa");
                return;
            }
            isEditable = true;
            openStage("Product_SubGUI.fxml", () -> {
                isEditable = false;
                if (!ProductSubGUI.isEdited()) return;
                if (!Product_BUS.editProduct(ProductSubGUI.getProduct()) ||
                    !Recipe_BUS.deleteRecipe(ProductSubGUI.getRemovedRecipe()) ||
                    !Recipe_BUS.editRecipe(ProductSubGUI.getArrCurrentRecipe()) ||
                    !Recipe_BUS.addRecipe(ProductSubGUI.getAddRecipe())) {
                    failedAlert();
                    return;
                }
                if(!Product_BUS.editProductLocal(ProductSubGUI.getProduct()) ||
                    !Recipe_BUS.deleteLocalRecipe(ProductSubGUI.getRemovedRecipe()) ||
                    !Recipe_BUS.editRecipesLocal(ProductSubGUI.getArrCurrentRecipe())||
                    !Recipe_BUS.addRecipesLocal(ProductSubGUI.getAddRecipe())){
                    failedAlert();
                    return;
                }
                ObservableList<Product> data = FXCollections.observableArrayList(Product_BUS.getAllProduct());
                tblProduct.setItems(data);
                tblProduct.refresh();
                ObservableList<Recipe> data1 = FXCollections.observableArrayList(ProductSubGUI.getArrCurrentRecipe());
                tblRecipe.setItems(data1);
                tblRecipe.refresh();
                ProductSubGUI.setProduct(null);
                ProductSubGUI.setArrCurrentRecipe(null);
                ProductSubGUI.setEdited(false);
            });
        });
        imgAdd.setOnMouseClicked(event -> openStage("Product_SubGUI.fxml",()->{
            if (ProductSubGUI.isEdited()) {
                if (Product_BUS.addProduct(ProductSubGUI.getProduct())&&Recipe_BUS.addRecipe(ProductSubGUI.getAddRecipe())) {
                    if (Product_BUS.addProductLocal(ProductSubGUI.getProduct())&&Recipe_BUS.addRecipesLocal(ProductSubGUI.getAddRecipe())) {
                        ObservableList<Product> data = FXCollections.observableArrayList(Product_BUS.getAllProduct());
                        tblProduct.setItems(data);
                        tblProduct.refresh();
                        ProductSubGUI.setProduct(null);
                        ProductSubGUI.setArrCurrentRecipe(null);
                        ProductSubGUI.setEdited(false);
                    }
                }
            }
        }));
        imgDelete.setOnMouseClicked(event -> {
            Product selectedProduct = tblProduct.getSelectionModel().getSelectedItem();
            if (selectedProduct == null) {
                ValidationUtil.showErrorAlert("Vui lòng chọn sản phẩm cần xóa");
                return;
            }
            if (!showAlert()) return;
            String productId = selectedProduct.getProductId();
            if (InvoiceDetail_BUS.checkProductExist(productId)) {
                ValidationUtil.showErrorAlert("Không thể xóa sản phẩm này vì đã có hóa đơn sử dụng sản phẩm này");
                return;
            }
            if (!Recipe_BUS.deleteRecipeByProductID(productId) || !Product_BUS.deleteProduct(productId) ||
                !Product_BUS.deleteProductLocal(productId) || !Recipe_BUS.deleteLocalRecipeByProductID(productId)) {
                failedAlert();
                return;
            }
            ObservableList<Product> data = FXCollections.observableArrayList(Product_BUS.getAllProduct());
            tblProduct.setItems(data);
            tblProduct.refresh();
        });
        imgLock.setOnMouseClicked(event -> {
            Product selectedProduct = tblProduct.getSelectionModel().getSelectedItem();
            if (selectedProduct == null) {
                ValidationUtil.showErrorAlert("Vui lòng chọn sản phẩm cần khóa");
                return;
            }

            Status newStatus = selectedProduct.getStatus().equals(Status.ACTIVE) ? Status.INACTIVE : Status.ACTIVE;
            String confirmMessage = newStatus.equals(Status.INACTIVE) ? "Bạn có chắc chắn muốn khóa sản phẩm này không?" : "Bạn có chắc chắn muốn mở khóa sản phẩm này không?";
            String lockImage = newStatus.equals(Status.INACTIVE) ?
                Objects.requireNonNull(getClass().getClassLoader().getResource("img/Lock.png")).toString() :
                Objects.requireNonNull(getClass().getClassLoader().getResource("img/Padlock.png")).toString();

            if (!ValidationUtil.showConfirmAlert(confirmMessage)) return;
            if (!Product_BUS.changeStatusProduct(selectedProduct.getProductId(), newStatus)) {
                failedAlert();
                return;
            }
            if (!Product_BUS.changeStatusProductLocal(selectedProduct.getProductId(), newStatus)) {
                failedAlert();
                return;
            }

            selectedProduct.setStatus(newStatus);
            imgLock.setImage(new ImageView(lockImage).getImage());
            ObservableList<Product> data = FXCollections.observableArrayList(Product_BUS.getAllProduct());
            tblProduct.setItems(data);
            tblProduct.refresh();
        });
        btnCategory.setOnAction(this::btnCategory);
        ImageView imgCategory = new ImageView(Objects.requireNonNull(getClass().getClassLoader().getResource("img/Settings.png")).toString());
        imgCategory.setFitHeight(25);
        imgCategory.setFitWidth(25);
        btnCategory.setGraphic(imgCategory);
        btnCategory.setStyle(" -fx-padding: 0");
        imgRefresh.setOnMouseClicked(event -> {
            Product_BUS.getLocalData();
            Recipe_BUS.getLocalData();
            Category_BUS.getLocalData();
            ObservableList<Product> data = FXCollections.observableArrayList(Product_BUS.getAllProduct());
            tblProduct.setItems(data);
            tblProduct.refresh();
        });
    }

    private void btnCategory(ActionEvent actionEvent) {
        //TODO: Open CategoryGUI and manage category
        openStage("Category_GUI.fxml", () -> {
            ObservableList<Product> data = FXCollections.observableArrayList(Product_BUS.getAllProduct());
            tblProduct.setItems(data);
            tblProduct.refresh();
        });
    }

    private void failedAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText("Đã có lỗi xảy ra");
        alert.showAndWait();
    }
    private boolean showAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận");
        alert.setHeaderText("Bạn có chắc chắn muốn xóa sản phẩm này không?");
        alert.showAndWait();
        return alert.getResult().getText().equals("OK");
    }

    private void onClear(ActionEvent actionEvent) {
        txtPrice.clear();
        txtProductName.clear();
        txtCategoryID.clear();
        txtStatus.clear();
        txtSearch.clear();
        lblProductID.setText("");
        tblProduct.getSelectionModel().clearSelection();
        tblRecipe.getItems().clear();

    }

    public void createProductTable() {
        colID.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCategoryID.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        ObservableList<Product> data = FXCollections.observableArrayList(Product_BUS.getAllProduct());
        tblProduct.setItems(data);
        tblProduct.getSelectionModel().selectedItemProperty().addListener(this::onProductSelected);
    }

    private void onProductSelected(Observable observable) {
        Product product = tblProduct.getSelectionModel().getSelectedItem();
        if (product != null) {
            lblProductID.setText(product.getProductId());
            txtProductName.setText(product.getName());
            txtCategoryID.setText(product.getCategoryId());
            txtPrice.setText(String.valueOf(product.getPrice()));
            txtStatus.setText(String.valueOf(product.getStatus()));
            imgLock.setImage(product.getStatus().equals(Status.ACTIVE)?
                    new ImageView(Objects.requireNonNull(getClass().getClassLoader().getResource("img/Lock.png")).toString()).getImage()
                    :new ImageView(Objects.requireNonNull(getClass().getClassLoader().getResource("img/Padlock.png")).toString()).getImage());
            ArrayList<Recipe> arrRecipes = (ArrayList<Recipe>) Recipe_BUS.getRecipeByProductID(product.getProductId());
            for (Recipe recipe : arrRecipes) {
                recipe.setIngredientName(Ingredient_BUS.getIngredientNameById(recipe.getIngredientId()));
            }
            ObservableList<Recipe> data = FXCollections.observableArrayList(arrRecipes);
            tblRecipe.setItems(data);
        }
    }
    public void createRecipeTable() {
        colIngredientName.setCellValueFactory(new PropertyValueFactory<>("ingredientName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
    }
    public void hideButtonWithoutPermission(){
        int permission = Login_Controller.getAccount().getPermission();
        if (!Main.checkRolePermission(permission,9)){
            imgAdd.setVisible(false);
            imgDelete.setVisible(false);
            imgEdit.setVisible(false);
            imgLock.setVisible(false);
            btnCategory.setVisible(false);
        }
    }
}
