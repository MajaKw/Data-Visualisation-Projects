package MainMenu;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.prefs.Preferences;

public class ChooseWorkingDirectory {
    public static void getWorkingDirectory(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(new Stage());

        if(selectedDirectory == null){
            return;
        }
        File saved = new File(selectedDirectory.getAbsolutePath() + "/Saved");
        File uploaded = new File(selectedDirectory.getAbsolutePath() + "/Uploaded");

        if(saved.exists() || uploaded.exists()){
            if(!showAlert()){
                return;
            }
        }
        if(!saved.exists()){
            saved.mkdir();
        }
        if(!uploaded.exists()){
            uploaded.mkdir();
        }
        MainMenu.pathToWorkingDirectory= selectedDirectory.getAbsolutePath();
        Preferences pref = Preferences.userNodeForPackage(MainMenu.class);
        pref.put(MainMenu.PREF_NAME, MainMenu.pathToWorkingDirectory);
    }

    private static boolean showAlert(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Duplicate");
        alert.setHeaderText("Selected folder contains Saved/Uploaded folders");
        alert.setContentText("If they weren't created by this application, there is no guarantee that application will work correctly");
        Optional<ButtonType> option = alert.showAndWait();
        return ButtonType.OK.equals(option.get());
    }

    public void handleChooseButtonPressed(ActionEvent event){
        getWorkingDirectory();
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void show(Stage stage){
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(("ChooseWorkingDirectory.fxml"))));
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
