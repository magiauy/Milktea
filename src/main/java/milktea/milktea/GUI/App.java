package milktea.milktea.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import milktea.milktea.BUS.Connect_BUS;
import milktea.milktea.Util.ValidationUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class App extends Application {
    @SuppressWarnings("exports")
    @Override
    public void start(Stage stage) throws IOException {
        if (Connect_BUS.loadConfig()) {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("LoginGUI.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("MilkTea_Store!");
            stage.setScene(scene);
            stage.getIcons().add(new Image("img/logo.png"));


            stage.show();
        }else {
            if (ValidationUtil.showConfirmAlert("Truy cập cơ sở dữ liệu thất bại ! Bạn có muốn cấu hình lại không ?")) {
                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("Connection.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                stage.setTitle("MilkTea_Store!");
                stage.setScene(scene);
                stage.getIcons().add(new Image("img/logo.png"));
                stage.show();
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
