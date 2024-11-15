package milktea.milktea.GUI;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;
import milktea.milktea.BUS.GoodsReceiptDetail_BUS;
import milktea.milktea.BUS.GoodsReceipt_BUS;
import milktea.milktea.BUS.Ingredient_BUS;
import milktea.milktea.DTO.GoodsReceipt;
import milktea.milktea.DTO.GoodsReceiptDetail;
import milktea.milktea.DTO.Ingredient;
import milktea.milktea.DTO.Status;
import milktea.milktea.Util.ValidationUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;

import static milktea.milktea.Util.UI_Util.openStage;
public class GoodsReceiptGUI {
    @FXML
    TableView<Ingredient> tblIngredient;
    @FXML
    TableColumn<Ingredient, String> colIngredientId;
    @FXML
    TableColumn<Ingredient, String> colIngredientName;
    @FXML
    TableColumn<Ingredient, String> colUnit;
    @FXML
    TableColumn<Ingredient, String> colQuantity;

    @FXML
    TableView<GoodsReceiptDetail> tblGoodsReceiptDetail;
    @FXML
    TableColumn<GoodsReceiptDetail, String> colGoodsReceiptDetailStt;
    @FXML
    TableColumn<GoodsReceiptDetail, String> colGoodsReceiptDetailIngredientName;
    @FXML
    TableColumn<GoodsReceiptDetail, String> colGoodsReceiptDetailQuantity;
    @FXML
    TableColumn<GoodsReceiptDetail, String> getColGoodsReceiptDetailUnit;
    @FXML
    TableColumn<GoodsReceiptDetail, String> colGoodsReceiptDetailPrice;
    @FXML
    TableColumn<GoodsReceiptDetail, String> colGoodsReceiptDetailTotal;

    @FXML
    TextField txtGoodsReceiptId;
    @FXML
    TextField txtGoodsReceiptDate;
    @FXML
    TextField txtGoodsEmployeeId;
    @FXML
    TextField txtGoodsEmployeeName;
    @FXML
    TextField txtGoodsProviderId;
    @FXML
    TextField txtGoodsProviderName;
    @FXML
    TextField txtSearchIngredient;

    @FXML
    Label lblTotal;

    @FXML
    ImageView imgEdit;
    @FXML
    ImageView imgDelete;

    @FXML
    Button btnClear;
    @FXML
    Button btnAdd;
    @FXML
    Button btnProviderList;
    @FXML
    Button btnSearch;
    @FXML
    Button btnAddGoodsReceipt;
    @FXML
    Tab tabCreateGoodsReceipt;

    @Getter
    @Setter
    private static Ingredient ingredient;

    @Getter
    @Setter
    private static GoodsReceiptDetail goodsReceiptDetail;

    @Getter
    @Setter
    private static boolean isEditable = false;
    @Getter
    @Setter
    private static ArrayList<GoodsReceiptDetail> arrGoodsReceiptDetail = new ArrayList<>();
    @FXML
    public void initialize() {
        hideTabWithoutPermission();
        txtGoodsReceiptId.setText(GoodsReceipt_BUS.autoId());
        txtGoodsEmployeeId.setText(Login_Controller.getAccount().getId());
        txtGoodsEmployeeName.setText(Login_Controller.getAccount().getLastName());
        txtGoodsReceiptDate.setText(LocalDate.now().toString());
        loadIngredient();
        loadGoodsReceiptDetail();
        btnAdd.setOnAction(this::Add);
        btnProviderList.setOnAction(this::ProviderList);
        imgEdit.setOnMouseClicked(event -> {
            if (tblGoodsReceiptDetail.getSelectionModel().getSelectedItem() != null) {
                goodsReceiptDetail = tblGoodsReceiptDetail.getSelectionModel().getSelectedItem();
                isEditable = true;
                openStage("GoodReceipt_SubGUI.fxml", ()->{
                    if (GoodReceiptSubGUI.isAdded()) {
                        if (goodsReceiptDetail != null) {
                            arrGoodsReceiptDetail.set(arrGoodsReceiptDetail.indexOf(tblGoodsReceiptDetail.getSelectionModel().getSelectedItem()), goodsReceiptDetail);
                            tblGoodsReceiptDetail.setItems(FXCollections.observableArrayList(arrGoodsReceiptDetail));
                            calTotal();
                            tblGoodsReceiptDetail.getSelectionModel().clearSelection();
                            goodsReceiptDetail = null;
                            GoodReceiptSubGUI.setAdded(false);
                            isEditable = false;
                        }
                    }
                });
            }
        });
        imgDelete.setOnMouseClicked(event -> {
            if (tblGoodsReceiptDetail.getSelectionModel().getSelectedItem() != null) {
                if (ValidationUtil.showConfirmAlert("Bạn có chắc chắn muốn xóa?")) {
                    arrGoodsReceiptDetail.remove(tblGoodsReceiptDetail.getSelectionModel().getSelectedItem());
                    tblGoodsReceiptDetail.setItems(FXCollections.observableArrayList(arrGoodsReceiptDetail));
                    calTotal();
                }
            }
        });
        btnClear.setOnAction(this::Clear);
        btnAddGoodsReceipt.setOnAction(this::AddGoodsReceipt);
        tabGoodsReceipt.setOnSelectionChanged(this::tabGoodsReceiptInit);
        btnSearch.setOnAction(this::Search);
    }

    private void Search(ActionEvent actionEvent) {
        ArrayList<Ingredient> ingredients = new ArrayList<>(Ingredient_BUS.searchIngredient(txtSearchIngredient.getText()));
        ingredients.removeIf(ingredient -> ingredient.getStatus().equals(Status.INACTIVE));
        tblIngredient.setItems(FXCollections.observableArrayList(ingredients));
    }


private void AddGoodsReceipt(ActionEvent actionEvent) {
    if (ValidationUtil.isEmpty(txtGoodsProviderId, txtGoodsEmployeeId, txtGoodsReceiptId, txtGoodsReceiptDate, txtGoodsEmployeeName, txtGoodsProviderName)) return;
    if (arrGoodsReceiptDetail.isEmpty()) {
        ValidationUtil.showErrorAlert("Vui lòng thêm nguyên liệu");
        return;
    }

    GoodsReceipt goodsReceipt = GoodsReceipt.builder()
            .id(txtGoodsReceiptId.getText())
            .date(LocalDate.parse(txtGoodsReceiptDate.getText()))
            .employeeId(txtGoodsEmployeeId.getText())
            .providerId(txtGoodsProviderId.getText())
            .total(new BigDecimal(lblTotal.getText()))
            .build();

    if (GoodsReceipt_BUS.addGoodsReceipt(goodsReceipt)) {
        if (GoodsReceiptDetail_BUS.addGoodsReceiptDetail(arrGoodsReceiptDetail)) {
            GoodsReceiptDetail_BUS.addGoodsReceiptDetailLocal(arrGoodsReceiptDetail);
            GoodsReceipt_BUS.addGoodsReceiptLocal(goodsReceipt);
            ValidationUtil.showErrorAlert("Đã thêm phiếu nhập hàng thành công");
            Clear(actionEvent);
        } else {
            GoodsReceipt_BUS.deleteGoodsReceipt(goodsReceipt.getId());
            ValidationUtil.showErrorAlert("Thêm chi tiết phiếu nhập hàng thất bại");
        }
    } else {
        ValidationUtil.showErrorAlert("Thêm phiếu nhập hàng thất bại");
    }
}

    private void Clear(ActionEvent actionEvent) {
        txtGoodsProviderId.clear();
        txtGoodsProviderName.clear();
        txtSearchIngredient.clear();
        arrGoodsReceiptDetail.clear();
        tblGoodsReceiptDetail.setItems(FXCollections.observableArrayList(arrGoodsReceiptDetail));
        calTotal();
    }

    private void Add(ActionEvent actionEvent) {
        if (tblIngredient.getSelectionModel().getSelectedItem() != null) {
            ingredient = tblIngredient.getSelectionModel().getSelectedItem();
            System.out.println(ingredient);
            openStage("GoodReceipt_SubGUI.fxml", ()->{
                if (GoodReceiptSubGUI.isAdded()) {
                    if (goodsReceiptDetail != null) {
                        goodsReceiptDetail.setGoodsReceiptId(txtGoodsReceiptId.getText());
                        goodsReceiptDetail.setIngredientId(ingredient.getId());
                        arrGoodsReceiptDetail.add(goodsReceiptDetail);
                        tblGoodsReceiptDetail.setItems(FXCollections.observableArrayList(arrGoodsReceiptDetail));
                        calTotal();
                        tblIngredient.getSelectionModel().clearSelection();
                        goodsReceiptDetail = null;
                        GoodReceiptSubGUI.setAdded(false);
                    }
                }
            });
        }
    }

    private void calTotal() {
        BigDecimal total = new BigDecimal(0);
        for (GoodsReceiptDetail goodsReceiptDetail : arrGoodsReceiptDetail) {
            total = total.add(goodsReceiptDetail.getTotal()).setScale(0, RoundingMode.HALF_UP);
        }
        lblTotal.setText(String.valueOf(total));
    }

    private void ProviderList(ActionEvent actionEvent) {
        openStage("ProviderList_SubGUI.fxml", ()->{
            if (ProviderListSubGUI.getProvider() != null) {
                txtGoodsProviderId.setText(ProviderListSubGUI.getProvider().getId());
                txtGoodsProviderName.setText(ProviderListSubGUI.getProvider().getName());
            }
        });
    }
    public void loadIngredient() {
        colIngredientId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colIngredientName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        ArrayList<Ingredient> ingredients = new ArrayList<>(Ingredient_BUS.getAllIngredient());
        ingredients.removeIf(ingredient -> ingredient.getStatus().equals(Status.INACTIVE));
        tblIngredient.setItems(FXCollections.observableArrayList(ingredients));
    }
    public void loadGoodsReceiptDetail() {
        colGoodsReceiptDetailStt.setCellValueFactory(cellData -> {
            int index = arrGoodsReceiptDetail.indexOf(cellData.getValue()) + 1;
            return new SimpleStringProperty(String.valueOf(index));
        });
        GoodsReceiptTableCol(colGoodsReceiptDetailIngredientName, colGoodsReceiptDetailQuantity, colGoodsReceiptDetailPrice, colGoodsReceiptDetailTotal);
        getColGoodsReceiptDetailUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
        tblGoodsReceiptDetail.setItems(FXCollections.observableArrayList(arrGoodsReceiptDetail));
    }

    @FXML
    Tab tabGoodsReceipt;

    @FXML
    private TableView<GoodsReceipt> tblGoodsReceipt;
    @FXML
    private TableColumn<GoodsReceipt, String> colGoodsReceiptId;
    @FXML
    private TableColumn<GoodsReceipt, String> colGoodsReceiptDate;
    @FXML
    private TableColumn<GoodsReceipt, String> colGoodsReceiptEmployeeId;
    @FXML
    private TableColumn<GoodsReceipt, String> colGoodsReceiptProviderId;
    @FXML
    private TableColumn<GoodsReceipt, String> colGoodsReceiptTotal;

    @FXML
    private TextField txtSearchGoodsReceipt;
    @FXML
    private Button btnSearchGoodsReceipt;

    @FXML
    TableView<GoodsReceiptDetail> tblGoodsReceiptDetail1;
    @FXML
    TableColumn<GoodsReceiptDetail, String> colGoodsReceiptDetailIngredientId1;
    @FXML
    TableColumn<GoodsReceiptDetail, String> colGoodsReceiptDetailIngredientName1;
    @FXML
    TableColumn<GoodsReceiptDetail, String> colGoodsReceiptDetailQuantity1;
    @FXML
    TableColumn<GoodsReceiptDetail, String> colGoodsReceiptDetailPrice1;
    @FXML
    TableColumn<GoodsReceiptDetail, String> colGoodsReceiptDetailTotal1;
    @FXML
    TableColumn<GoodsReceiptDetail, String> colGoodsReceiptDetailUnit1;

    @FXML
    private Label lblGoodsReceiptId;
    @FXML
    private TextField txtGoodsReceiptDate1;
    @FXML
    private TextField txtGoodsEmployeeId1;
    @FXML
    private TextField txtGoodsProviderId1;
    @FXML
    private TextField txtGoodsReceiptTotal1;

    @FXML
    private Button advancedSearch;
    @FXML
    private Button Clear;

    @FXML
    ImageView imgRefresh;
    private void tabGoodsReceiptInit(Event event) {
        loadGoodsReceipt();
        initTableGoodsReceiptDetail();
        Clear.setOnAction(this::Clear1);
        btnSearchGoodsReceipt.setOnAction(this::SearchGoodsReceipt);
        advancedSearch.setOnAction(this::AdvancedSearch);
        imgRefresh.setOnMouseClicked(event1 -> {
            GoodsReceipt_BUS.getLocalData();
            GoodsReceiptDetail_BUS.getLocalData();
            loadGoodsReceipt();
            initTableGoodsReceiptDetail();
            Clear1(new ActionEvent());
            ValidationUtil.showInfoAlert("Làm mới dữ liệu thành công");

        });
    }

    private void AdvancedSearch(ActionEvent actionEvent) {
        openStage("AdvancedSearchGoodsReceipt_SubGUI.fxml", ()->{
            if (AdvancedSearchGoodsReceiptSubGUI.isDone()) {
                if (AdvancedSearchGoodsReceiptSubGUI.getArrGoodsReceipt() != null) {
                    tblGoodsReceipt.setItems(FXCollections.observableArrayList(AdvancedSearchGoodsReceiptSubGUI.getArrGoodsReceipt()));
                    AdvancedSearchGoodsReceiptSubGUI.setDone(false);
                    AdvancedSearchGoodsReceiptSubGUI.setArrGoodsReceipt(null);
                }
            }
        });
    }

    private void SearchGoodsReceipt(ActionEvent actionEvent) {
        tblGoodsReceipt.setItems(FXCollections.observableArrayList(GoodsReceipt_BUS.searchGoodsReceipt(txtSearchGoodsReceipt.getText())));
    }

    public void Clear1(ActionEvent actionEvent) {
        lblGoodsReceiptId.setText("");
        txtGoodsReceiptDate1.clear();
        txtGoodsEmployeeId1.clear();
        txtGoodsProviderId1.clear();
        txtGoodsReceiptTotal1.clear();
        tblGoodsReceipt.getSelectionModel().clearSelection();
        tblGoodsReceiptDetail1.setItems(FXCollections.observableArrayList());
    }
    public void loadGoodsReceipt() {
        colGoodsReceiptId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colGoodsReceiptDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colGoodsReceiptEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colGoodsReceiptProviderId.setCellValueFactory(new PropertyValueFactory<>("providerId"));
        colGoodsReceiptTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        tblGoodsReceipt.setItems(FXCollections.observableArrayList(GoodsReceipt_BUS.getAllGoodsReceipt()));
        tblGoodsReceipt.setOnMouseClicked( event -> {
            if (tblGoodsReceipt.getSelectionModel().getSelectedItem() != null) {
                GoodsReceipt goodsReceipt = tblGoodsReceipt.getSelectionModel().getSelectedItem();
                lblGoodsReceiptId.setText(goodsReceipt.getId());
                txtGoodsReceiptDate1.setText(goodsReceipt.getDate().toString());
                txtGoodsEmployeeId1.setText(goodsReceipt.getEmployeeId());
                txtGoodsProviderId1.setText(goodsReceipt.getProviderId());
                txtGoodsReceiptTotal1.setText(goodsReceipt.getTotal().toString());
                tblGoodsReceiptDetail1.setItems(FXCollections.observableArrayList(GoodsReceiptDetail_BUS.getGoodsReceiptDetailByGoodsReceiptId(goodsReceipt.getId())));
            }
        });
    }
    public void initTableGoodsReceiptDetail(){
        colGoodsReceiptDetailIngredientId1.setCellValueFactory(new PropertyValueFactory<>("ingredientId"));
        GoodsReceiptTableCol(colGoodsReceiptDetailIngredientName1, colGoodsReceiptDetailQuantity1, colGoodsReceiptDetailPrice1, colGoodsReceiptDetailTotal1);
        colGoodsReceiptDetailUnit1.setCellValueFactory(new PropertyValueFactory<>("unit"));
    }

    private void GoodsReceiptTableCol(TableColumn<GoodsReceiptDetail, String> colGoodsReceiptDetailIngredientName1, TableColumn<GoodsReceiptDetail, String> colGoodsReceiptDetailQuantity1, TableColumn<GoodsReceiptDetail, String> colGoodsReceiptDetailPrice1, TableColumn<GoodsReceiptDetail, String> colGoodsReceiptDetailTotal1) {
        colGoodsReceiptDetailIngredientName1.setCellValueFactory(cellData -> {
            Ingredient ingredient = Ingredient_BUS.getIngredientById(cellData.getValue().getIngredientId());
            return new SimpleStringProperty(ingredient.getName());
        });
        colGoodsReceiptDetailQuantity1.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colGoodsReceiptDetailPrice1.setCellValueFactory(new PropertyValueFactory<>("price"));
        colGoodsReceiptDetailTotal1.setCellValueFactory(new PropertyValueFactory<>("total"));
    }
    public void hideTabWithoutPermission(){
        int permission = Login_Controller.getAccount().getPermission();
        if (!Main.checkRolePermission(permission,4)){
            tabCreateGoodsReceipt.getTabPane().getTabs().remove(tabCreateGoodsReceipt);
            tabGoodsReceiptInit(new ActionEvent());
        }
        if (!Main.checkRolePermission(permission,8)){
            tabGoodsReceipt.getTabPane().getTabs().remove(tabGoodsReceipt);
        }
    }
}
