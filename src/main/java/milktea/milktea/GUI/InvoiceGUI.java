package milktea.milktea.GUI;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import milktea.milktea.BUS.*;
import milktea.milktea.DTO.*;
import milktea.milktea.Util.ValidationUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

import static milktea.milktea.Util.UI_Util.configureButton;
import static milktea.milktea.Util.UI_Util.openStage;
import static milktea.milktea.Util.ValidationUtil.isEmpty;
@Slf4j
public class InvoiceGUI {
    @FXML
    private ScrollPane productPane;
    @FXML
    private TextField txtProductSearch;
    @FXML
    private Button btnProductSearch;
    @FXML
    private Button btnAddProduct;
    @FXML
    private ComboBox<String> cbxCategory;
    @FXML
    private ScrollPane invoiceDetailPane;
    @FXML
    private TextField txtInvoiceId;
    @FXML
    private TextField txtCurrentDate;
    @FXML
    private TextField txtCustomerName;
    @FXML
    private TextField txtCustomerId;
    @FXML
    private TextField txtEmployeeId;
    @FXML
    private TextField txtEmployeeName;
    @FXML
    private Button btnCustomerPicker;
    @FXML
    private Button btnCustomerAdd;

    @FXML
    private Button btnAddInvoice;
    @FXML
    private TextField txtCurrentPoint;
    @FXML
    private TextField txtPromotion;
    @FXML
    private TextField txtDiscount;
    @FXML
    private Button btnPromotionPicker;
    @FXML
    private Label lblTotal;
    @FXML
    private Button clear;
    @FXML
    private CheckBox chbPoint;

    @FXML
    private ImageView btnEdit;
    @FXML
    private ImageView btnDelete;

    private final ArrayList<Product> arrProducts = new ArrayList<>();

    @Getter
    @Setter
    private static String invoiceId = null;
    @Getter
    @Setter
    private static Product selectedProduct = null;
    @Getter
    @Setter
    private static TempInvoiceDetail selectedInvoiceDetail = null;
    @Getter
    @Setter
    private static Object selectedObject = null;
    @Getter
    @Setter
    private static ArrayList<TempInvoiceDetail> invoiceDetail = new ArrayList<>();
    @Getter
    @Setter
    private static boolean isEditable = false;
    @Getter
    @Setter
    private static int globalProductIndex = 0 ;

    @Getter
    @Setter
    private static String globalListFlag = null;
    public void initialize() {
        invoiceId = Invoice_BUS.autoId();
        txtInvoiceId.setText(invoiceId);

        txtEmployeeId.setText(Login_Controller.getAccount().getId());
        txtEmployeeName.setText(Login_Controller.getAccount().getLastName());

        txtCustomerId.setText(Customer_BUS.getAllCustomer().getFirst().getId());
        txtCustomerName.setText(Customer_BUS.getAllCustomer().getFirst().getLastName());

        LocalDate currentDate = LocalDate.now();
        txtCurrentDate.setText(currentDate.toString());

        txtPromotion.setText("NoPromotion");
        checkPointCustomer();
        invoiceDetail.clear();
        arrProducts.addAll(Recipe_BUS.updateProductQuantity(Product_BUS.getAllProduct(), invoiceDetail, false));
        setupButton();
        productPane.setContent(createListProduct());
        productPane.setPadding(new Insets(10, 10, 10, 10)); // top, right, bottom, left
        tabInvoice.setOnSelectionChanged( event -> tabInvoiceInit());
    }

    public void setupButton(){
        configureButton(btnProductSearch, this::btnProductSearch);
        configureButton(btnAddProduct, this::btnAddProduct);
        configureButton(btnCustomerPicker, this::btnCustomerPicker);
        configureButton(btnCustomerAdd, this::btnCustomerAdd);
        configureButton(btnPromotionPicker, this::btnPromotionPicker);
        configureButton(btnAddInvoice, this::btnAddInvoice);
        configureButton(clear, this::clear);
        btnEdit.setOnMouseClicked(this::edit);
        btnDelete.setOnMouseClicked(this::delete);
        chbPoint.setOnAction(event -> usePointToDiscount());
        txtProductSearch.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                btnProductSearch(new ActionEvent());
            }
        });
        cbxCategory.getItems().clear();
        cbxCategory.getItems().add("Tất cả");
        cbxCategory.getItems().addAll(Category_BUS.getAllCategoryName());

        cbxCategory.addEventHandler(ActionEvent.ACTION, event -> {
            if (cbxCategory.getValue().equals("Tất cả")) {
                arrProducts.clear();
                arrProducts.addAll(Product_BUS.getAllProduct());
                productPane.setContent(createListProduct());
            } else {
                arrProducts.clear();
                arrProducts.addAll(Product_BUS.searchProductByCategory(Category_BUS.getCategoryIdByName(cbxCategory.getValue())));
                productPane.setContent(createListProduct());
            }
        });
    }

    private void clear(ActionEvent actionEvent) {
        txtProductSearch.clear();
        txtPromotion.clear();
        txtDiscount.clear();
        txtCurrentPoint.clear();
        txtCustomerId.clear();
        txtCustomerName.clear();
        txtEmployeeId.clear();
        txtEmployeeName.clear();
        txtInvoiceId.clear();
        invoiceDetail.clear();
        invoiceDetailPane.setContent(createListInvoiceDetail());
        arrProducts.clear();
        arrProducts.addAll(Recipe_BUS.updateProductQuantity(Product_BUS.getAllProduct(), invoiceDetail, false));
        productPane.setContent(createListProduct());
        initialize();

    }

    private void btnPromotionPicker(ActionEvent actionEvent) {
        globalListFlag = "Khuyến Mãi";
        openStage("SubGUI_List_InvoiceDetail.fxml",() -> {
            if (selectedObject != null) {
                if (selectedObject instanceof Promotion) {
                    txtPromotion.setText(((Promotion) selectedObject).getPromotionId());
                    callBackDiscount(1,0, String.valueOf(((Promotion) selectedObject).getDiscount()));
                    calTotal();
                }
            }
        });
    }

    private void btnProductSearch(ActionEvent actionEvent) {
        if (!ValidationUtil.isInvalidSearch(txtProductSearch)) {
            if (!txtProductSearch.getText().isEmpty()) {
                arrProducts.clear();
                arrProducts.addAll(Product_BUS.searchProduct(txtProductSearch.getText()));
                productPane.setContent(createListProduct());
            } else {
                arrProducts.clear();
                arrProducts.addAll(Product_BUS.getAllProduct());
                productPane.setContent(createListProduct());
            }
        }
    }

    private void btnCustomerPicker(ActionEvent actionEvent) {
        globalListFlag = "Khách Hàng";
        openStage("SubGUI_List_InvoiceDetail.fxml",() -> {
            if (selectedObject != null) {
                 if (selectedObject instanceof Customer) {
                    txtCustomerId.setText(((Customer) selectedObject).getId());
                    txtCustomerName.setText(((Customer) selectedObject).getLastName());
                    checkPointCustomer();
                }
            }
        });
    }

    private void btnCustomerAdd(ActionEvent actionEvent) {
        openStage("CustomerSubGUI.fxml",() -> {
            if (CustomerSubGUI.isAdded()){
                txtCustomerId.setText(Customer_BUS.getAllCustomer().getLast().getId());
                txtCustomerName.setText(Customer_BUS.getAllCustomer().getLast().getLastName());
                checkPointCustomer();
                CustomerSubGUI.setAdded(false);
            }
        });
    }

    private void btnAddProduct(ActionEvent actionEvent) {
        if (selectedProduct != null) {
            if (Product_BUS.checkQuantityBelowZero(arrProducts, selectedProduct.getProductId())) {
                openStage("SubGUI_InvoiceDetail.fxml", () -> {
                    arrProducts.clear();
                    arrProducts.addAll(Recipe_BUS.updateProductQuantity(Product_BUS.getAllProduct(), invoiceDetail, false));
                    productPane.setContent(createListProduct());
                    invoiceDetailPane.setContent(createListInvoiceDetail());
                });
            }else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Số lượng sản phẩm đã hết");
                alert.showAndWait();
            }
        }
    }

    private void btnAddInvoice(ActionEvent actionEvent) {
        Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        alertConfirm.setTitle("Xác nhận");
        alertConfirm.setHeaderText(null);
        alertConfirm.setContentText("Bạn có chắc chắn muốn thêm hóa đơn này");
        Optional<ButtonType> action = alertConfirm.showAndWait();

        if (action.isPresent() && action.get() == ButtonType.OK) {
            if (!isEmpty(txtCurrentDate, txtCustomerId,txtEmployeeId, txtCustomerName,  txtEmployeeName, txtInvoiceId,txtPromotion)) {
                {
                    HashMap<String, Integer> stackedTopping = new HashMap<>();
                    HashMap<String, Integer> stackedProduct = new HashMap<>();
                    for (TempInvoiceDetail entry : invoiceDetail) {
                        if (entry.getTopping() != null) {
                            for (Map.Entry<String, Integer> topping : entry.getTopping().entrySet()) {
                                if (stackedTopping.containsKey(topping.getKey())) {
                                    stackedTopping.put(topping.getKey(), stackedTopping.get(topping.getKey()) + topping.getValue());
                                } else {
                                    stackedTopping.put(topping.getKey(), topping.getValue());
                                }
                            }
                        }
                        if (stackedProduct.containsKey(entry.getInvoiceDetail().getProductId())) {
                            stackedProduct.put(entry.getInvoiceDetail().getProductId(), stackedProduct.get(entry.getInvoiceDetail().getProductId()) + entry.getInvoiceDetail().getQuantity());
                        } else {
                            stackedProduct.put(entry.getInvoiceDetail().getProductId(), entry.getInvoiceDetail().getQuantity());
                        }
                    }
//                    System.out.println(stackedProduct);
//                    System.out.println(stackedTopping);
                    ArrayList<InvoiceDetail> arrInvoiceDetail = new ArrayList<>();
                    createDetail(stackedProduct, arrInvoiceDetail);
                    if (!stackedTopping.isEmpty()) {
                        createDetail(stackedTopping, arrInvoiceDetail);
                    }
                    Invoice invoice = Invoice.builder()
                            .invoiceId(invoiceId)
                            .issueDate(LocalDate.now())
                            .customerId(txtCustomerId.getText())
                            .employeeId(txtEmployeeId.getText())
                            .promotionId(txtPromotion.getText())
                            .total(total)
                            .build();
                    if (discount.compareTo(total) > 0) {
                        invoice.setDiscount(total);
                    } else {
                        invoice.setDiscount(discount);
                    }

                    if (Invoice_BUS.addInvoice(invoice) && InvoiceDetail_BUS.addInvoiceDetail(arrInvoiceDetail) && Ingredient_BUS.updateIngredient(arrInvoiceDetail)) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Thông báo");
                            alert.setHeaderText(null);
                            alert.setContentText("Thêm hóa đơn thành công");
                            alert.showAndWait();
                            Invoice_BUS.addInvoiceLocal(invoice);
                            InvoiceDetail_BUS.addInvoiceDetailLocal(arrInvoiceDetail);
                            earnPoint(Customer_BUS.getCustomerById(txtCustomerId.getText()));
                        clear(new ActionEvent());


                    } else {
                        //Remove if add invoice fail
                        Invoice_BUS.removeInvoice(invoiceId);
                        Invoice_BUS.removeInvoiceDetail(invoiceId);

                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Thông báo");
                        alert.setHeaderText(null);
                        alert.setContentText("Thêm hóa đơn thất bại");
                        alert.showAndWait();
                    }
                }
            }
        }
    }

    private void createDetail(@NonNull HashMap<String, Integer> stackedArr, ArrayList<InvoiceDetail> arrInvoiceDetail) {
        for (Map.Entry<String, Integer> entry : stackedArr.entrySet()){
            InvoiceDetail invoiceDetail = InvoiceDetail.builder()
                    .invoiceId(invoiceId)
                    .productId(entry.getKey())
                    .quantity(entry.getValue())
                    .unitPrice(Objects.requireNonNull(Product_BUS.getProductById(entry.getKey())).getPrice())
                    .totalPrice(Objects.requireNonNull(Product_BUS.getProductById(entry.getKey())).getPrice().multiply(BigDecimal.valueOf(entry.getValue())))
                    .build();
            arrInvoiceDetail.add(invoiceDetail);
        }
    }


    private GridPane currentlySelectedItem = null;
    public GridPane createListProduct(){
        GridPane container = new GridPane();
        container.setHgap(5);
        container.setVgap(5);
        ArrayList<GridPane> items = new ArrayList<>();
        for (Product product : arrProducts) {
            if (product.getStatus().equals(Status.ACTIVE)){
                if (product.getCategoryId().equals("C005")) {
                    continue;
                }
                // Create Label for product code and name
                Label productCodeAndNameLabel = new Label(product.getProductId() + " : " + product.getName());

                // Create Label for product price
//            Label priceLabel = new Label(product.getPrice().setScale(0, RoundingMode.HALF_UP) + " VND"+"                        "+"SL: "+product.getQuantity());
                String formattedPriceLabel = String.format("%-7s VND                        SL: %-5d",
                        product.getPrice().setScale(0, RoundingMode.HALF_UP),
                        product.getQuantity());
                Label priceLabel = new Label(formattedPriceLabel);
                VBox leftPanel = new VBox();
                leftPanel.getChildren().addAll(productCodeAndNameLabel, new Text("-------------------------------------"), priceLabel);
                GridPane item = new GridPane();
                item.add(leftPanel, 0, 0);
                item.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());
                item.getStyleClass().add("item-border");
                BooleanProperty selected = new SimpleBooleanProperty(false);
                item.setUserData(selected);

                item.setOnMouseClicked(e -> {
                    if (currentlySelectedItem != null) {
                        currentlySelectedItem.setStyle("-fx-border-radius: 5px; -fx-border-color: black; -fx-border-width: 1px;");
                    }
                    selected.set(true);
                    if (selected.get()) {
                        item.setStyle("-fx-border-radius: 5px; -fx-border-color: red; -fx-border-width: 3px;");
                        currentlySelectedItem = item;
                        selectedProduct = product;
                    }
                });
                items.add(item);
            }
        }


        for (int i = 0; i < items.size(); i++) {
            container.add(items.get(i), i % 3, i / 3);
        }
        return container;
    }
    boolean productSpinnerChanged = false;
    boolean toppingSpinnerChanged = false;

    public GridPane createListInvoiceDetail(){
        GridPane container = new GridPane();
        container.setVgap(5);
        GridPane[] items = new GridPane[invoiceDetail.size()];
        int productIndex = 0;

    if (!invoiceDetail.isEmpty()) {
        for (TempInvoiceDetail entry : invoiceDetail) {
            HBox hBoxProduct = new HBox();
            GridPane panel = new GridPane();
            Label lblSTT = new Label("   "+(productIndex+1));
            lblSTT.setPrefWidth(35.0);

            Label lblProductName = new Label(Objects.requireNonNull(Product_BUS.getProductById(entry.getInvoiceDetail().getProductId())).getName());
            lblProductName.setPrefWidth(138.0);
            lblProductName.setStyle("-fx-font-weight: bold");

            Label lblPrice = new Label(""+entry.getInvoiceDetail().getUnitPrice().setScale(0, RoundingMode.HALF_UP));
            lblPrice.setPrefWidth(53.0);
            lblPrice.setAlignment(Pos.CENTER_RIGHT);

            Label lblTotalPrice = new Label("   "+ entry.getInvoiceDetail().getTotalPrice().setScale(0, RoundingMode.HALF_UP));
            lblTotalPrice.setPrefWidth(70.0);
            lblTotalPrice.setAlignment(Pos.CENTER_RIGHT);

            Spinner<Integer> snProductQuantity = new Spinner<>(1, 100, entry.getInvoiceDetail().getQuantity());
            snProductQuantity.setPrefWidth(53);
            snProductQuantity.valueProperty().addListener((obs, oldValue, newValue) -> {
                if (!productSpinnerChanged) {
                    if (newValue>oldValue){
                        if (Product_BUS.checkQuantityBelowZero(arrProducts, entry.getInvoiceDetail().getProductId())) {
                            updateQuantity(entry, lblTotalPrice, newValue);
                        } else {
                            productSpinnerChanged = true;
                            snProductQuantity.getValueFactory().setValue(oldValue);
                        }
                    }else {
                        updateQuantity(entry, lblTotalPrice, newValue);
                    }
                    productSpinnerChanged = false;
                }
            });
            GridPane.setMargin(snProductQuantity, new Insets(0 ,10 , 0 ,0));
            GridPane.setMargin(lblProductName, new Insets(0 ,0 , 0 ,5));

            panel.add(lblSTT, 0, 0);
            panel.add(lblProductName, 1, 0);
            panel.add(snProductQuantity, 2, 0);
            panel.add(lblPrice, 3, 0);
            panel.add(lblTotalPrice, 4, 0);

            hBoxProduct.getChildren().add(panel);
            VBox vboxToppings = new VBox();
            HashMap<String, Integer> toppings = entry.getTopping();
            if (toppings != null) {
                for (Map.Entry<String, Integer> topping : Objects.requireNonNull(toppings).entrySet()) {

                    Label space = new Label("    ");
                    space.setPrefWidth(35.0);

                    Label lblToppingName = new Label("+ " + Objects.requireNonNull(Product_BUS.getProductById(topping.getKey())).getName());
                    lblToppingName.setPrefWidth(138.0);

                    Label lblToppingPrice = new Label("" + Objects.requireNonNull(Objects.requireNonNull(Product_BUS.getProductById(topping.getKey())).getPrice()).setScale(0, RoundingMode.HALF_UP));
                    lblToppingPrice.setPrefWidth(53);
                    lblToppingPrice.setAlignment(Pos.CENTER_RIGHT);

                    Label lblToppingTotalPrice = new Label("   " + Objects.requireNonNull(Objects.requireNonNull(Product_BUS.getProductById(topping.getKey())).getPrice().multiply(BigDecimal.valueOf(topping.getValue().doubleValue()))).setScale(0, RoundingMode.HALF_UP));
                    lblToppingTotalPrice.setPrefWidth(70.0);
                    lblToppingTotalPrice.setAlignment(Pos.CENTER_RIGHT);

                    Spinner<Integer> snToppingQuantity = new Spinner<>(1, 100, topping.getValue());
                    snToppingQuantity.setPrefWidth(53);
                    snToppingQuantity.valueProperty().addListener((obs, oldValue, newValue) -> {
                        if (!toppingSpinnerChanged) {
                            if (newValue > oldValue) {
                                if (Product_BUS.checkQuantityBelowZero(arrProducts, topping.getKey())) {
                                    updateToppingQuantity(entry, toppings, topping, lblToppingTotalPrice, newValue);
                                }else {
                                    toppingSpinnerChanged = true;
                                    snToppingQuantity.getValueFactory().setValue(oldValue);
                                }
                            }else {
                                updateToppingQuantity(entry, toppings, topping, lblToppingTotalPrice, newValue);
                            }

                        }
                        toppingSpinnerChanged = false;

                    });

                    GridPane.setMargin(snToppingQuantity, new Insets(0, 10, 0, 0));
                    GridPane.setMargin(lblToppingName, new Insets(0, 0, 0, 5));

                    GridPane panelTopping = new GridPane();
                    panelTopping.add(space, 0, 0);
                    panelTopping.add(lblToppingName, 1, 0);
                    panelTopping.add(snToppingQuantity, 2, 0);
                    panelTopping.add(lblToppingPrice, 3, 0);
                    panelTopping.add(lblToppingTotalPrice, 4, 0);

                    HBox hBoxTopping = new HBox();
                    hBoxTopping.getChildren().add(panelTopping);
                    vboxToppings.getChildren().add(hBoxTopping);


                }
            }
            VBox vbox = new VBox();
            Label space = new Label("             ");
            Label lblSugar = new Label("Đường: "+entry.getSugar()+"    ");
            Label lblIce = new Label("Đá: "+entry.getIce());

            HBox hBoxSugarIce = new HBox();
            hBoxSugarIce.getChildren().addAll(space,lblSugar, lblIce);


            vbox.getChildren().addAll(hBoxProduct,hBoxSugarIce, vboxToppings);
            items[productIndex] = new GridPane();
            items[productIndex].add(vbox, 0, productIndex);
            int finalProductIndex = productIndex;
            items[productIndex].addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> {
                if (currentlySelectedItem != null) {
                    currentlySelectedItem.setStyle("-fx-background-color: white;");
                }
                items[finalProductIndex].setStyle("-fx-background-color: lightblue;");
                currentlySelectedItem = items[finalProductIndex];
                selectedInvoiceDetail = entry;
                globalProductIndex = finalProductIndex;
            });
            container.add(items[productIndex], 0, productIndex);
            productIndex++;
        }

    }
        calTotal();
        return container;
    }

    private void updateToppingQuantity(TempInvoiceDetail entry, HashMap<String, Integer> toppings, Map.Entry<String, Integer> topping, Label lblToppingTotalPrice, Integer newValue) {
        toppingSpinnerChanged = true;
        toppings.put(topping.getKey(), newValue);
        entry.setTopping(toppings);
        lblToppingTotalPrice.setText(Objects.requireNonNull(Objects.requireNonNull(Product_BUS.getProductById(topping.getKey())).getPrice().multiply(BigDecimal.valueOf(newValue.doubleValue()))).setScale(0, RoundingMode.HALF_UP).toString());
        arrProducts.clear();
        arrProducts.addAll(Recipe_BUS.updateProductQuantity(Product_BUS.getAllProduct(), invoiceDetail, false));
        productPane.setContent(createListProduct());
        calTotal();
    }

    private void updateQuantity(TempInvoiceDetail entry, Label lblTotalPrice, Integer newValue) {
        productSpinnerChanged = true;
        entry.getInvoiceDetail().setQuantity(newValue);
        entry.getInvoiceDetail().setTotalPrice(entry.getInvoiceDetail().getUnitPrice().multiply(BigDecimal.valueOf(newValue)));
        lblTotalPrice.setText(entry.getInvoiceDetail().getTotalPrice().setScale(0, RoundingMode.HALF_UP).toString());
        arrProducts.clear();
        arrProducts.addAll(Recipe_BUS.updateProductQuantity(Product_BUS.getAllProduct(), invoiceDetail, false));
        productPane.setContent(createListProduct());
        calTotal();
    }

    public void edit(MouseEvent event){
        if (selectedInvoiceDetail != null) {
            isEditable = true;
            openStage("SubGUI_InvoiceDetail.fxml", () -> {
                isEditable = false;
                invoiceDetail.set(globalProductIndex, selectedInvoiceDetail);
                invoiceDetailPane.setContent(createListInvoiceDetail());
                selectedInvoiceDetail = null;
            });
        }else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn chi tiết cần sửa");
            alert.showAndWait();
        }
    }
    public void delete(MouseEvent event){
        if (selectedInvoiceDetail != null) {
            if (confirmAlert()) {
                invoiceDetail.remove(globalProductIndex);
                arrProducts.clear();
                arrProducts.addAll(Recipe_BUS.updateProductQuantity(Product_BUS.getAllProduct(), invoiceDetail, false));
                productPane.setContent(createListProduct());
                invoiceDetailPane.setContent(createListInvoiceDetail());
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn chi tiết cần xoá");
            alert.showAndWait();
        }
    }

    boolean confirmAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận");
        alert.setHeaderText(null);
        alert.setContentText("Bạn có chắc chắn xoá sản phẩm này");
        Optional<ButtonType> action = alert.showAndWait();
        return action.map(buttonType -> buttonType == ButtonType.OK).orElse(false);
    }

    public void checkPointCustomer(){
        if (txtCustomerId.getText().equals(Customer_BUS.getAllCustomer().getFirst().getId())){
            txtCurrentPoint.setText("0");
            txtCurrentPoint.setDisable(true);
            chbPoint.setDisable(true);
        }else {
            txtCurrentPoint.setDisable(false);
            chbPoint.setDisable(false);
            txtCurrentPoint.setText(Customer_BUS.getPointByID(txtCustomerId.getText()));
        }
    }
    public BigDecimal discountByPoint = BigDecimal.valueOf(0);
    public BigDecimal discountByPromotion = BigDecimal.valueOf(0);
    public BigDecimal discount = BigDecimal.valueOf(0);
    public BigDecimal total ;

    public void calTotal() {
        total = BigDecimal.valueOf(0);
        for (TempInvoiceDetail entry : invoiceDetail) {
            total = total.add(entry.getInvoiceDetail().getTotalPrice());
            if (entry.getTopping() != null) {
                for (Map.Entry<String, Integer> topping : entry.getTopping().entrySet()) {
                    total = total.add(Objects.requireNonNull(Objects.requireNonNull(Product_BUS.getProductById(topping.getKey())).getPrice().multiply(BigDecimal.valueOf(topping.getValue()))));
                }
            }
        }
        total = total.subtract(discount);
        if (total.compareTo(BigDecimal.valueOf(0)) < 0) {
            total = BigDecimal.valueOf(0);
            lblTotal.setText(total.setScale(0, RoundingMode.HALF_UP).toString());
        } else {
            lblTotal.setText(total.setScale(0, RoundingMode.HALF_UP).toString());
        }
    }
    public void usePointToDiscount(){
        if (chbPoint.isSelected()) {
            if (Integer.parseInt(txtCurrentPoint.getText()) >= 1000) {
                callBackDiscount(1,1, String.valueOf(((Integer.parseInt(txtCurrentPoint.getText()) / 1000) * 1000)));
                calTotal();
            }else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Điểm của bạn không đủ để sử dụng");
                alert.showAndWait();
                chbPoint.setSelected(false);
            }
        }else {
            callBackDiscount(0,0, String.valueOf(((Integer.parseInt(txtCurrentPoint.getText()) / 1000) * 1000)));
            calTotal();
        }
    }

   public void callBackDiscount(int flagIncreaseDecrease, int flagDiscountType, String value) {
    BigDecimal valueBigDecimal = BigDecimal.valueOf(Integer.parseInt(value));

    if (flagIncreaseDecrease == 1) {
        if (flagDiscountType == 1) {
            discountByPoint = discountByPoint.add(valueBigDecimal);
        } else {
            discountByPromotion = discountByPromotion.add(valueBigDecimal);
        }
    } else {
        if (flagDiscountType == 1) {
            discountByPoint = discountByPoint.subtract(valueBigDecimal);
        } else {
            discountByPromotion = discountByPromotion.subtract(valueBigDecimal);
        }
    }

    discount = discountByPoint.add(discountByPromotion);
    txtDiscount.setText(discount.setScale(0, RoundingMode.HALF_UP).toString());
}
    public void earnPoint(Customer customer) {
        if (chbPoint.isSelected()) {
            System.out.println(discount.compareTo(total) > 0);
            if (discount.compareTo(total) > 0) {
                BigDecimal remainingPoint = discount.subtract(total).subtract(discountByPromotion);
                BigDecimal newPoint = total.multiply(BigDecimal.valueOf(0.05)).setScale(0, RoundingMode.HALF_UP);
                customer.setPoint(newPoint.add(remainingPoint));
            } else {
                BigDecimal newPoint = total.multiply(BigDecimal.valueOf(0.05)).setScale(0, RoundingMode.HALF_UP);
                customer.setPoint(newPoint);
            }
        }else {
            BigDecimal newPoint = total.multiply(BigDecimal.valueOf(0.05)).setScale(0, RoundingMode.HALF_UP);
            customer.setPoint(customer.getPoint().add(newPoint));
        }
        Customer_BUS.editCustomer(customer);
        Customer_BUS.editCustomerLocal(customer);
    }

    @FXML
    TableView<Invoice> tblInvoice;
    @FXML
    TableColumn<Invoice, String> colInvoiceId;
    @FXML
    TableColumn<Invoice, String> colCustomerId;
    @FXML
    TableColumn<Invoice, String> colEmployeeId;
    @FXML
    TableColumn<Invoice, String> colPromotionId;
    @FXML
    TableColumn<Invoice, String> colIssueDate;
    @FXML
    TableColumn<Invoice, BigDecimal> colDiscount;
    @FXML
    TableColumn<Invoice, BigDecimal> colTotal;

    @FXML
    TableView<InvoiceDetail> tblInvoiceDetail;
    @FXML
    TableColumn<InvoiceDetail, String> colProductId;
    @FXML
    TableColumn<InvoiceDetail, Integer> colQuantity;
    @FXML
    TableColumn<InvoiceDetail, BigDecimal> colUnitPrice;
    @FXML
    TableColumn<InvoiceDetail, BigDecimal> colTotalPrice;

    @FXML
    TextField txtSearchInvoice;
    @FXML
    Button btnSearchInvoice;
    @FXML
    Button btnAdvanceSearchInvoice;
    @FXML
    Button btnClearInvoice;
    @FXML
    TextField txtInvoiceEmployeeId;
    @FXML
    TextField txtInvoiceCustomerId;
    @FXML
    TextField txtInvoicePromotionId;
    @FXML
    TextField txtInvoiceIssueDate;
    @FXML
    TextField txtInvoiceDiscount;
    @FXML
    TextField txtInvoiceTotal;
    @FXML
    Label lblInvoiceId;

    @FXML
    Tab tabInvoice;

    @FXML
    ImageView imgRefresh;

    public void tabInvoiceInit(){
        loadTableInvoice();
        btnClearInvoice.setOnAction(event -> {
            lblInvoiceId.setText("");
            txtInvoiceEmployeeId.setText("");
            txtInvoiceCustomerId.setText("");
            txtInvoicePromotionId.setText("");
            txtInvoiceIssueDate.setText("");
            txtInvoiceDiscount.setText("");
            txtInvoiceTotal.setText("");
            tblInvoiceDetail.getItems().clear();
            tblInvoiceDetail.refresh();
            tblInvoice.getSelectionModel().clearSelection();
        });
        btnSearchInvoice.setOnAction(event -> {
            if (!ValidationUtil.isInvalidSearch(txtSearchInvoice)) {
                if (!txtSearchInvoice.getText().isEmpty()) {
                    ObservableList<Invoice> invoices = FXCollections.observableList(Invoice_BUS.searchInvoice(txtSearchInvoice.getText()));
                    tblInvoice.setItems(invoices);
                    tblInvoice.refresh();
                } else {
                    loadTableInvoice();
                }
            }
        });
        btnAdvanceSearchInvoice.setOnAction(event -> {
            openStage("AdvancedSearchInvoice_SubGUI.fxml",() -> {
                if (AdvancedSearchInvoiceSubGUI.getArrInvoice() != null) {
                    if (AdvancedSearchInvoiceSubGUI.isDone()) {
                        ObservableList<Invoice> invoices = FXCollections.observableList(AdvancedSearchInvoiceSubGUI.getArrInvoice());
                        tblInvoice.setItems(invoices);
                        tblInvoice.refresh();
                        AdvancedSearchInvoiceSubGUI.setDone(false);
                        AdvancedSearchInvoiceSubGUI.setArrInvoice(null);
                    }
                }
            });
        });
        imgRefresh.setOnMouseClicked(event -> {
            loadTableInvoice();
            btnClearInvoice.fire();
        });
    }
    public void loadTableInvoice(){
        colInvoiceId.setCellValueFactory(new PropertyValueFactory<>("invoiceId"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colPromotionId.setCellValueFactory(new PropertyValueFactory<>("promotionId"));
        colIssueDate.setCellValueFactory(new PropertyValueFactory<>("issueDate"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        ObservableList<Invoice> invoices = FXCollections.observableList(Invoice_BUS.getAllInvoice());
        tblInvoice.setItems(invoices);
        tblInvoice.setOnMouseClicked(event -> {
            if (tblInvoice.getSelectionModel()!=null){
                Invoice invoice = tblInvoice.getSelectionModel().getSelectedItem();
                if (invoice != null){
                    lblInvoiceId.setText(invoice.getInvoiceId());
                    txtInvoiceEmployeeId.setText(invoice.getEmployeeId());
                    txtInvoiceCustomerId.setText(invoice.getCustomerId());
                    txtInvoicePromotionId.setText(invoice.getPromotionId());
                    txtInvoiceIssueDate.setText(invoice.getIssueDate().toString());
                    txtInvoiceDiscount.setText(invoice.getDiscount().toString());
                    txtInvoiceTotal.setText(invoice.getTotal().toString());
                    loadTableInvoiceDetail(invoice);
                }
            }
        });
        tblInvoice.refresh();
    }
    public void loadTableInvoiceDetail(Invoice invoice){
        colProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        ObservableList<InvoiceDetail> invoiceDetails = FXCollections.observableList(InvoiceDetail_BUS.getInvoiceDetailByInvoiceId(invoice.getInvoiceId()));
        tblInvoiceDetail.setItems(invoiceDetails);
        tblInvoiceDetail.refresh();

    }

}
