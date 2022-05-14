package Menu;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChartSetUpWindow {

    public static void display(Stage stage){
        // load the .fxml file created in Scene Builder
        try {
            Parent root = FXMLLoader.load(ChartSetUpWindow.class.getResource("ChartSetUpWindow.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}