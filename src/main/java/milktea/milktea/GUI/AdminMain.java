package milktea.milktea.GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import java.io.IOException;


public class AdminMain {
    @FXML
    private Pane mainPane;
    @FXML
    private Button btnEmployee;
    @FXML
    private Button btnStatistic;
    @FXML
    private Label userNameLabel;

    @FXML
    public void initialize() {
        action();
    }
 public void logout(){
        try {
            Stage stage = (Stage) userNameLabel.getScene().getWindow();
            stage.close();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("LoginGUI.fxml"));

            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("MilkTea_Store!");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void action(){
        btnEmployee.setOnAction(event -> loadFXML("Employee.fxml"));
        btnStatistic.setOnAction(event -> loadFXML("Statistic.fxml"));
    }

        public void loadFXML(String fxmlFile) {
    try {
        // Create a new FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        // Set the URL for the FXMLLoader to point to the new FXML file
        loader.setLocation(getClass().getResource(fxmlFile));
        System.out.println("Load FXML: " + fxmlFile);
        // Load the FXML content
        Pane newContent = loader.load();
        // Clear the main pane and add the new content
        mainPane.getChildren().clear();
        mainPane.getChildren().add(newContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
