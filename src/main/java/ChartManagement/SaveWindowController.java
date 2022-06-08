package Menu;

import MainMenu.MainMenu;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

import javafx.scene.control.TextField;
import javafx.scene.control.Label;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveWindowController {

    ChartWindow chartWindow;

    @FXML
    private TextField name;

    @FXML
    private Label error;



    public void handleGoBackButtonPressed(ActionEvent event){
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void handleSaveButtonPressed(ActionEvent event){
        String pathToSave = name.getText();
        if(pathToSave == "" || pathToSave.contains(" ")){
            error.setText("Wrong input");
            return;
        }
        File savedFolder = new File(MainMenu.pathToWorkingDirectory + "/Saved");
        File[] savedContents = savedFolder.listFiles();
        if(savedContents!=null) {
            for (File f : savedContents) {
                if (f.getName().equals(pathToSave)) {
                    error.setText("Saved chart with that name already exist");
                    return;
                }
            }
        }
        try {
            File newFile = new File(MainMenu.pathToWorkingDirectory + "/Saved/" + pathToSave);
            newFile.createNewFile();
            FileWriter fileWriter = new FileWriter(newFile);
            //System.out.println(chartWindow.toSave);
            //System.out.println(chartWindow.barChart.getStyle());
            fileWriter.write(chartWindow.barChart.getStyle() + "\n");
            fileWriter.write(chartWindow.toSave.toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            error.setText("Something went wrong");
            return;
        }
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
