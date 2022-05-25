package MainMenu;

import Menu.ChartWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.util.Objects;

public class OpenProject {

    @FXML
    TextField loadLabel;

    @FXML
    Label error;


    void show(Stage stage) {
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(("OpenProject.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
        }


        Scene scene = new Scene(root, 600, 400);
        if (Settings.isDarkMode) {
            scene.getStylesheets().add("DarkMode.css");
        } else {
            scene.getStylesheets().add("LightMode.css");
        }
        stage.setScene(scene);
        stage.show();
    }

    public void handleToMainMenuButtonPressed(ActionEvent event) {
        MainMenu mainMenu = new MainMenu();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        mainMenu.show(stage);
    }

    public void handleLoadButtonPressed(ActionEvent event) {
        String pathToLoad = loadLabel.getText();
        if (pathToLoad.equals("") || pathToLoad.contains(" ")) {
            error.setText("Wrong input");
            return;
        }
        File savedFolder = new File("src/main/resources/Saved/");
        File[] savedContents = savedFolder.listFiles();
        if (savedContents == null) {
            error.setText("There is no chart saved");
            return;
        }
        for (File f : savedContents) {
            System.out.println(f.getName());
            if (f.getName().equals( pathToLoad)) {
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(f));
                    String line = reader.readLine();
                    String[] start = line.split(";");
                    ChartWindow.showChartWindow(start[0],start[1],null);
                } catch (Exception e) {
                    error.setText("Something went wrong");
                    return;
                }

            }
        }
    }
}
