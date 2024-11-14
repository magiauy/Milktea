package milktea.milktea.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import milktea.milktea.BUS.Connect_BUS;

import java.io.IOException;

public class App extends Application {
    @SuppressWarnings("exports")
    @Override
    public void start(Stage stage) throws IOException {
        if (Connect_BUS.loadConfig()) {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("LoginGUI.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("MilkTea_Store!");
            stage.setScene(scene);
            stage.show();
        }else {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("Connection.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("MilkTea_Store!");
            stage.setScene(scene);
            stage.show();
        }
    }

    public static void main(String[] args) {
        launch();
    }



}
