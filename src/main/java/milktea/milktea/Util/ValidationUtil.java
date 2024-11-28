package milktea.milktea.Util;

import javafx.scene.control.*;

import lombok.NonNull;

import java.time.LocalDate;
import java.util.HashMap;


public class ValidationUtil {


    public static void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showInfoAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static boolean isEmpty(@NonNull TextField... params) {
        for (TextField param : params) {
            if (param.getText().isEmpty()) {
                showErrorAlert("Vui lòng điền đầy đủ thông tin");
                param.requestFocus();
                return true;
            }
        }
        return false;
    }


    public static boolean isInValidChar(HashMap<TextField, String> textFieldInfo, @NonNull TextField... params) {
        for (TextField param : params) {
            if (!param.getText().matches("^[\\p{L}\\s]+$")) {
                showErrorAlert(textFieldInfo.get(param) + " không được chứa số hoặc ký tự đặc biệt");
                param.requestFocus();
                return true;
            }
        }
        return false;
    }

    public static boolean isNegativeNumber(String key ,@NonNull TextField textField) {
        if (Double.parseDouble(textField.getText()) < 0) {
            showErrorAlert(key + " không thể âm");
            textField.requestFocus();
            return true;
        }
        return false;
    }
    public static boolean isInValidChar(String key, @NonNull TextField textField) {
        if (!textField.getText().matches("^[\\p{L}\\s]+$")) {
            showErrorAlert(key + " không được chứa số hoặc ký tự đặc biệt");
            textField.requestFocus();
            return true;
        }
        return false;
    }

    public static boolean isInvalidSearch(@NonNull TextField textfield) {
        if (!textfield.getText().matches("[\\p{L}\\p{M}\\p{N}\\s]*$")) {
            showErrorAlert("Không được chứa ký tự đặc biệt");
            textfield.requestFocus();
            return true;
        }
        return false;
    }
    public static boolean isInvalidSearch(String key,@NonNull TextField textfield) {
        if (!textfield.getText().matches("[\\p{L}\\p{M}\\p{N}\\s]*$")) {
            showErrorAlert(key+" không được chứa ký tự đặc biệt");
            textfield.requestFocus();
            return true;
        }
        return false;
    }

    public static boolean isNotPhoneNumber(HashMap<TextField, String> textFieldInfo, @NonNull TextField... params) {
        for (TextField param : params) {
            if (!param.getText().matches("^0\\d{9}$")) {
                showErrorAlert(textFieldInfo.get(param) + " phải bắt đầu bằng số 0 và có 10 chữ số");
                param.requestFocus();
                return true;
            }
        }
        return false;
    }

    public static boolean isFirstCharNotSpace(HashMap<TextField, String> textFieldInfo, @NonNull TextField... params) {
        for (TextField param : params) {
            if (param.getText().charAt(0) == ' ') {
                showErrorAlert(textFieldInfo.get(param) + " không được bắt đầu bằng khoảng trắng");
                param.requestFocus();
                return true;
            }
        }
        return false;
    }

    public static boolean isFirstCharNotSpace(@NonNull TextField textfield) {
        if (textfield.getText().charAt(0) == ' ') {
            showErrorAlert("Không được bắt đầu bằng khoảng trắng");
            textfield.requestFocus();
            return true;
        }
        return false;
    }
    public static boolean isFirstCharNotSpace(String key,@NonNull TextField textfield) {
        if (textfield.getText().charAt(0) == ' ') {
            showErrorAlert(key+ " không được bắt đầu bằng khoảng trắng");
            textfield.requestFocus();
            return true;
        }
        return false;
    }

    public static boolean isNotPrice(TextField textField) {
        if (!textField.getText().matches("^[0-9]+(\\.[0-9]{1,2})?$")) {
            showErrorAlert("Giá tiền không hợp lệ");
            textField.requestFocus();
            return true;
        }
        return false;
    }
    public static boolean isNotPrice(TextField... params) {
        for (TextField param : params) {
            try {
                Float.parseFloat(param.getText());
            } catch (NumberFormatException e) {
                showErrorAlert("Giá tiền không hợp lệ");
                param.requestFocus();
                return true;
            }
        }
        return false;
    }

    public static boolean isEmptyComboBox(@NonNull ComboBox<?>... params) {
        for (ComboBox<?> param : params) {
            if (param.getValue() == null) {
                showErrorAlert("Vui lòng chọn " + param.getPromptText());
                param.requestFocus();
                return true;
            }
        }
        return false;
    }

    public static boolean isNegativeFloat(Float value) {
        if (value < 0) {
            showErrorAlert("Số lượng không thể âm");
            return false;
        }
        return true;
    }

    public static boolean isFloat(@NonNull TextField textField) {
        try {
            Float.parseFloat(textField.getText());
            return true;
        } catch (NumberFormatException e) {
            showErrorAlert("Số lượng không hợp lệ");
            return false;
        }
    }

    public static boolean isInt(@NonNull TextField textField) {
        try {
            Integer.parseInt(textField.getText());
            return true;
        } catch (NumberFormatException e) {
            showErrorAlert("Số lượng không hợp lệ");
            return false;
        }
    }

    public static boolean showConfirmAlert(String s) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(s);
        return alert.showAndWait().map(buttonType -> buttonType == ButtonType.OK).orElse(false);
    }

    public static boolean isValidDateRange(LocalDate startDate, LocalDate endDate){
        if (startDate.isAfter(endDate)){
            showErrorAlert("Ngày bắt đầu phải trước ngày kết thúc");
            return false;
        }
        //EndDate không được quá tháng hiện tại
        if (endDate.isAfter(LocalDate.now())){
            showErrorAlert("Bạn đang du hành về tương lai à?");
            return false;
        }

        return true;
    }


    public static boolean isValidTotalRange(TextField txtSearchInvoiceMinTotal, TextField txtSearchInvoiceMaxTotal) {
        if (Float.parseFloat(txtSearchInvoiceMinTotal.getText()) > Float.parseFloat(txtSearchInvoiceMaxTotal.getText())) {
            showErrorAlert("Tổng tiền nhỏ nhất phải nhỏ hơn tổng tiền lớn nhất");
            return false;
        }
        return true;
    }

    public static boolean isEmptyDp(DatePicker dtpGoodsReceiptStartDate,String key) {
        if (dtpGoodsReceiptStartDate.getValue() == null){
            showErrorAlert("Vui lòng chọn ngày "+key);
            return true;
        }
        return false;
    }

    public static boolean isNotEmail(HashMap<TextField, String> map, TextField txtEmail) {
        if (!txtEmail.getText().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            showErrorAlert(map.get(txtEmail) + " không hợp lệ");
            txtEmail.requestFocus();
            return true;
        }
        return false;
    }


}