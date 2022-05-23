package MainMenu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ImportData {

    void show(Stage stage){
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(("ImportData.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
        }


        Scene scene = new Scene(root,600,400);
        stage.setScene(scene);
        stage.show();
    }

    public void handleToMainMenuButtonPressed(ActionEvent event){
        MainMenu mainMenu = new MainMenu();
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        mainMenu.show(stage);
    }

}
