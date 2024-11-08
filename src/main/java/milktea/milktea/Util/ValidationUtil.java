package milktea.milktea.Util;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import lombok.NonNull;

import java.util.HashMap;

public class ValidationUtil {

    public static boolean validateCustomerFields(String id, String firstName, String lastName, String phone, boolean isGenderSelected) {
        boolean valid = true;
        boolean alert = false;

        if (id.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || !isGenderSelected) {
            showErrorAlert("Please fill all the fields");
            valid = false;
            alert = true;
        }

        if (!firstName.matches("^[^\\s\\d\\W][^\\d\\W]*$")) {
            if (!alert) {
                showErrorAlert("FirstName must not contain number or special character");
                valid = false;
                alert = true;
            }
        }
        if (!lastName.matches("^[^\\s\\d\\W][^\\d\\W]*$")) {
            if (!alert) {
                showErrorAlert("LastName must not contain number or special character");
                alert = true;
                valid = false;
            }
        }
        if (!phone.matches("^0.*")) {
            if (!alert) {
                showErrorAlert("PhoneNumber must start with 0");
                alert = true;
                valid = false;
            }
        }
        if (!phone.matches("[0-9]+")) {
            if (!alert) {
                showErrorAlert("PhoneNumber must be a number");
                alert = true;
                valid = false;
            }
        }
        if (phone.length() != 10) {
            if (!alert) {
                showErrorAlert("Phone must have 10 digits");
                alert = true;
                valid = false;
            }
        }
        System.out.println(valid);
        return valid;
    }

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

    public static boolean isEmpty(@NonNull TextField... params){
        for (TextField param : params) {
            if (param.getText().isEmpty()) {
                showErrorAlert("Vui lòng điền đầy đủ thông tin");
                param.requestFocus();
                return true;
            }
        }
        return false;
    }


    public static boolean isInValidChar(HashMap<TextField, String> textFieldInfo, @NonNull TextField... params){
        for (TextField param : params) {
            if (!param.getText().matches("^[a-zA-Z\\s]+$")) {
                showErrorAlert(textFieldInfo.get(param) + " không được chứa số hoặc ký tự đặc biệt");
                param.requestFocus();
                return true;
            }
        }
        return false;
    }

      public static boolean isInvalidSearch(@NonNull TextField textfield){
        if (!textfield.getText().matches("[\\p{L}\\p{M}\\p{N}\\s]*$")) {
            showErrorAlert("Không được chứa ký tự đặc biệt");
            textfield.requestFocus();
            return true;
        }
        return false;
    }

    public static boolean isNotPhoneNumber(HashMap<TextField, String> textFieldInfo, @NonNull TextField... params){
        for (TextField param : params) {
            if (!param.getText().matches("^0\\d{9}$")) {
                showErrorAlert(textFieldInfo.get(param) + " phải bắt đầu bằng số 0 và có 10 chữ số");
                param.requestFocus();
                return true;
            }
        }
        return false;
    }

    public static boolean isFirstCharNotSpace(HashMap<TextField, String> textFieldInfo, @NonNull TextField... params){
        for (TextField param : params) {
            if (param.getText().charAt(0) == ' ') {
                showErrorAlert(textFieldInfo.get(param) + " không được bắt đầu bằng khoảng trắng");
                param.requestFocus();
                return true;
            }
        }
        return false;
    }

    public static boolean isFirstCharNotSpace(@NonNull TextField textfield){
        if (textfield.getText().charAt(0) == ' ') {
            showErrorAlert("Không được bắt đầu bằng khoảng trắng");
            textfield.requestFocus();
            return true;
        }
        return false;
    }

    public static boolean isNotPrice(TextField textField){
        if (!textField.getText().matches("^[0-9]+(\\.[0-9]{1,2})?$")) {
            showErrorAlert("Giá tiền không hợp lệ");
            textField.requestFocus();
            return true;
        }
        return false;
    }

}