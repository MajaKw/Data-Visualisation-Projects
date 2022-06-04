package MainMenu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.prefs.Preferences;

public class Settings {
    public static boolean isDarkMode = true;

    @FXML
    Button changeButton;


    void show(Stage stage){
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(("Settings.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(root,600,400);
        if(Settings.isDarkMode){
            scene.getStylesheets().add("DarkMode.css");
        }
        else {
            scene.getStylesheets().add("LightMode.css");
        }
        stage.setScene(scene);
        stage.show();
    }

    public void handleToMainMenuButtonPressed(ActionEvent event){
        MainMenu mainMenu = new MainMenu();
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        mainMenu.show(stage);
    }
    public void handleChangeButtonPressed(ActionEvent event){
        isDarkMode=!isDarkMode;
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        show(stage);
    }

    public void handleChangeWorkingDirectoryButtonPressed(ActionEvent event){
        Stage newStage = new Stage();
        newStage.initModality(Modality.APPLICATION_MODAL);
        ChooseWorkingDirectory chooseWorkingDirectory = new ChooseWorkingDirectory();
        chooseWorkingDirectory.show(newStage);
        newStage.showAndWait();
    }



}
