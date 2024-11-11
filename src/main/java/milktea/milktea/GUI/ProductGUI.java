package milktea.milktea.GUI;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import lombok.Getter;
import milktea.milktea.BUS.Ingredient_BUS;
import milktea.milktea.BUS.InvoiceDetail_BUS;
import milktea.milktea.BUS.Product_BUS;
import milktea.milktea.BUS.Recipe_BUS;
import milktea.milktea.DTO.Product;
import milktea.milktea.DTO.Recipe;
import milktea.milktea.DTO.Status;

import static milktea.milktea.Util.UI_Util.openStage;
import java.util.ArrayList;

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
    Button btnSearch;
    @FXML
    Button btnClear;

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
            if (tblProduct.getSelectionModel().getSelectedItem() != null) {
                isEditable = true;
                selectedProduct = tblProduct.getSelectionModel().getSelectedItem();
                openStage("Product_SubGUI.fxml",()->{
                    if (ProductSubGUI.isEdited()) {
                        if (Product_BUS.editProduct(ProductSubGUI.getProduct())&&Recipe_BUS.deleteRecipe(ProductSubGUI.getRemovedRecipe())&&Recipe_BUS.editRecipe(ProductSubGUI.getArrRecipe())) {
                            if (Product_BUS.editProductLocal(ProductSubGUI.getProduct())&&Recipe_BUS.deleteLocalRecipe(ProductSubGUI.getRemovedRecipe())&& Recipe_BUS.editRecipesLocal(ProductSubGUI.getArrRecipe())) {
                                ObservableList<Product> data = FXCollections.observableArrayList(Product_BUS.getAllProduct());
                                tblProduct.setItems(data);
                                tblProduct.refresh();
                                ObservableList<Recipe> data1 = FXCollections.observableArrayList(ProductSubGUI.getArrRecipe());
                                tblRecipe.setItems(data1);
                                tblRecipe.refresh();
                                ProductSubGUI.setProduct(null);
                                ProductSubGUI.setArrRecipe(null);
                                ProductSubGUI.setEdited(false);
                            }
                    }

                    }
                });
            }else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText("Vui lòng chọn sản phẩm cần sửa");
                alert.showAndWait();
            }
        });
        imgAdd.setOnMouseClicked(event -> openStage("Product_SubGUI.fxml",()->{
            if (ProductSubGUI.isEdited()) {
                if (Product_BUS.addProduct(ProductSubGUI.getProduct())&&Recipe_BUS.addRecipe(ProductSubGUI.getArrRecipe())) {
                    if (Product_BUS.addProductLocal(ProductSubGUI.getProduct())&&Recipe_BUS.addRecipesLocal(ProductSubGUI.getArrRecipe())) {
                        ObservableList<Product> data = FXCollections.observableArrayList(Product_BUS.getAllProduct());
                        tblProduct.setItems(data);
                        tblProduct.refresh();
                        ProductSubGUI.setProduct(null);
                        ProductSubGUI.setArrRecipe(null);
                        ProductSubGUI.setEdited(false);
                    }
                }
            }
        }));
        imgDelete.setOnMouseClicked(event -> {
            if (tblProduct.getSelectionModel().getSelectedItem() != null) {
                if (showAlert()) {
                    if (!InvoiceDetail_BUS.checkProductExist(tblProduct.getSelectionModel().getSelectedItem().getProductId())) {
                        if (Recipe_BUS.deleteRecipeByProductID(tblProduct.getSelectionModel().getSelectedItem().getProductId())) {
                            if (Product_BUS.deleteProduct(tblProduct.getSelectionModel().getSelectedItem().getProductId())) {
                                if (Product_BUS.deleteProductLocal(tblProduct.getSelectionModel().getSelectedItem().getProductId()) && Recipe_BUS.deleteLocalRecipeByProductID(tblProduct.getSelectionModel().getSelectedItem().getProductId())) {
                                    ObservableList<Product> data = FXCollections.observableArrayList(Product_BUS.getAllProduct());
                                    tblProduct.setItems(data);
                                    tblProduct.refresh();
                                }
                            } else {
                                failedAlert();
                            }
                        } else {
                            failedAlert();
                        }
                    }else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Thông báo");
                        alert.setHeaderText("Không thể xóa sản phẩm này vì đã có hóa đơn sử dụng sản phẩm này");
                        alert.showAndWait();
                    }
                }
            }else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText("Vui lòng chọn sản phẩm cần xóa");
                alert.showAndWait();
            }
        });
        imgLock.setOnMouseClicked(event -> {
            if (tblProduct.getSelectionModel().getSelectedItem() != null) {
                if (tblProduct.getSelectionModel().getSelectedItem().getStatus().equals(Status.ACTIVE)) {
                    if (Product_BUS.changeStatusProduct(tblProduct.getSelectionModel().getSelectedItem().getProductId(), Status.INACTIVE)) {
                        if (Product_BUS.changeStatusProductLocal(tblProduct.getSelectionModel().getSelectedItem().getProductId(), Status.INACTIVE)) {
                            tblProduct.getSelectionModel().getSelectedItem().setStatus(Status.INACTIVE);
                            imgLock.setImage(new ImageView("img/Lock.png").getImage());
                            ObservableList<Product> data = FXCollections.observableArrayList(Product_BUS.getAllProduct());
                            tblProduct.setItems(data);
                            tblProduct.refresh();
                        }else {
                            failedAlert();
                        }
                    }else {
                        failedAlert();
                    }
                }else {
                    if (Product_BUS.changeStatusProduct(tblProduct.getSelectionModel().getSelectedItem().getProductId(), Status.ACTIVE)) {
                        if (Product_BUS.changeStatusProductLocal(tblProduct.getSelectionModel().getSelectedItem().getProductId(), Status.ACTIVE)) {
                            tblProduct.getSelectionModel().getSelectedItem().setStatus(Status.ACTIVE);
                            imgLock.setImage(new ImageView("img/Padlock.png").getImage());
                            ObservableList<Product> data = FXCollections.observableArrayList(Product_BUS.getAllProduct());
                            tblProduct.setItems(data);
                            tblProduct.refresh();
                        }else {
                            failedAlert();
                        }
                    }else {
                        failedAlert();
                    }
                }
            }else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText("Vui lòng chọn sản phẩm cần khóa");
                alert.showAndWait();
            }
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
            imgLock.setImage(product.getStatus().equals(Status.ACTIVE)?new ImageView("img/Padlock.png").getImage():new ImageView("img/Lock.png").getImage());
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
        int permission = Login_Controller.account.getPermission();
        if (!Main.checkRolePermission(permission,9)){
            imgAdd.setVisible(false);
            imgDelete.setVisible(false);
            imgEdit.setVisible(false);
            imgLock.setVisible(false);
        }
    }
}
