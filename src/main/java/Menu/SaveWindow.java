package Menu;

import MainMenu.Settings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SaveWindow {
    void display(Stage stage){
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(("SaveWindow.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
        }


        Scene scene = new Scene(root,373,168);
        if(Settings.isDarkMode){
            scene.getStylesheets().add("DarkMode.css");
        }
        else {
            scene.getStylesheets().add("LightMode.css");
        }
        stage.setResizable(false);
        stage.setScene(scene);
    }
}
