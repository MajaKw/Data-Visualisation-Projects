package MainMenu;

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
        show(stage);
    }

    void show(Stage stage){
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainMenu.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(root,600,400);
        stage.setScene(scene);
        stage.show();
    }

    public void handleNewProjectButtonPressed(ActionEvent event){
        Menu.Menu menu = new Menu.Menu();
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        try {
            menu.start(stage);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    public void handleOpenProjectButtonPressed(ActionEvent event){
        OpenProject openProject = new OpenProject();
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        openProject.show(stage);
    }

    public void handleImportDataButtonPressed(ActionEvent event){
        ImportData importData = new ImportData();
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        importData.show(stage);
    }

    public void handleSettingsButtonPressed(ActionEvent event){
        AboutProject aboutProject = new AboutProject();
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        aboutProject.show(stage);
    }

    public void handleAboutProjectButtonPressed(ActionEvent event){
        AboutProject aboutProject = new AboutProject();
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        aboutProject.show(stage);
    }
}
