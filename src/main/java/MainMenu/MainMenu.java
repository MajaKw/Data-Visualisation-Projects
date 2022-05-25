package MainMenu;

import Menu.ChartSetUpWindow;
import Menu.DiagramWindow;
import javafx.application.Application;
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

public class MainMenu extends Application {

    public Button toAboutProjectButton;
    public Button toOpenProjectButton;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setMinHeight(425);
        stage.setMinWidth(600);
        show(stage);
    }

    public void show(Stage stage){
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainMenu.fxml")));
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

    public void handleNewProjectButtonPressed(ActionEvent event){
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        ChartSetUpWindow.display(stage);
    }


    public void handleOpenProjectButtonPressed(ActionEvent event){
        OpenProject openProject = new OpenProject();
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        openProject.show(stage);
    }

    public void handleImportDataButtonPressed(ActionEvent event){
            try {
                new DiagramWindow().display("Upload window");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
    }

    public void handleSettingsButtonPressed(ActionEvent event){
        Settings settings = new Settings();
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        settings.show(stage);
    }

    public void handleAboutProjectButtonPressed(ActionEvent event){
        AboutProject aboutProject = new AboutProject();
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        aboutProject.show(stage);
    }
}
