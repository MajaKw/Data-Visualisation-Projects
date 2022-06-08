package ChartManagement;

import MainMenu.Settings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SaveWindow {

    private ChartWindow chartWindow;

    SaveWindow(ChartWindow chartWindow) {
        this.chartWindow = chartWindow;
    }

    void display(Stage stage) {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(("SaveWindow.fxml")));
            root = loader.load();
            SaveWindowController controller =  loader.getController();
            controller.chartWindow=chartWindow;
        } catch (IOException e) {
            e.printStackTrace();
        }


        Scene scene = new Scene(root, 373, 168);
        if (Settings.isDarkMode) {
            scene.getStylesheets().add("DarkMode.css");
        } else {
            scene.getStylesheets().add("LightMode.css");
        }
        stage.setResizable(false);
        stage.setScene(scene);
    }
}
