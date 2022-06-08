package ChartManagement;

import MainMenu.MainMenu;
import MainMenu.Settings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class UsefulFunctions {
    // returning all elements of the specified type contained in the scene graph
    public static <T> List<T> loopOverSceneGraph(Parent parent, Class<T> type) {
        List<T> elements = new LinkedList<>();
        for(Node node : parent.getChildrenUnmodifiable()) {
            if(node instanceof Pane) elements.addAll(loopOverSceneGraph((Pane) node, type));
            if(type.isAssignableFrom(node.getClass())) elements.add((T) node);
        }
        return Collections.unmodifiableList(elements);
    }

    public static int getColumnIndex(String path, String columnName) {
        //using data location
        String file = MainMenu.pathToWorkingDirectory + "/Uploaded/"+path;
        int index = -1;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            String[] namesAndUnits = line.split(";");
            for(int i=0; i<namesAndUnits.length; i++) {
                if(namesAndUnits[i].split("\\|")[0].equals(columnName)) { index = i; break; }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return index-1;
    }

    public static String getColumnName(String path, int idx) {
        // using data location
        String file = MainMenu.pathToWorkingDirectory + "/Uploaded/"+path;
        String columnName = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            String[] namesAndUnits = line.split(";");
            columnName = namesAndUnits[idx].split("\\|")[0];
        } catch(Exception e) {
            if(e.getClass() != ArrayIndexOutOfBoundsException.class) e.printStackTrace();
        }
        return columnName;
    }

    public static String getColumnUnit(String path, int idx) {
        String file = MainMenu.pathToWorkingDirectory + "/Uploaded/"+path;
        String columnUnit = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            String[] namesAndUnits = line.split(";");
            columnUnit = namesAndUnits[idx].split("\\|")[1];
        } catch(Exception e) {
            if(e.getClass() != ArrayIndexOutOfBoundsException.class) e.printStackTrace();
        }
        return columnUnit;
    }

    public static ArrayList<String> getAllFilePaths() {
        ArrayList<String> filePaths = new ArrayList<>();
        try {
            Files.find(Path.of(MainMenu.pathToWorkingDirectory + "/Uploaded/"),
                    Integer.MAX_VALUE,
                    (filePath, fileAtrr) -> fileAtrr.isRegularFile()
            ).forEach((s) -> {
                StringBuilder sb = new StringBuilder(s.toString());
                if(sb.substring(sb.length()-4, sb.length()).equals(".csv")) {
//                    System.out.println(sb);
                    String st = sb.substring(sb.indexOf("Uploaded/")+"Uploaded/".length(), sb.length());
                    filePaths.add(st);
                }
            });
        } catch(Exception e) {
            e.printStackTrace();
        }

        return filePaths;
    }

    public static void showErrorWindow(String errorMessage) {
        Parent root = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ChartSetUpWindow.class.getResource("ErrorWindow.fxml"));
            root = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Label errorLabel = loopOverSceneGraph(root, Label.class).get(0);
        errorLabel.setText(errorMessage);
        errorLabel.setTextFill(Color.RED);
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        if(Settings.isDarkMode){
            scene.getStylesheets().add("DarkMode.css");
        }
        else {
            scene.getStylesheets().add("LightMode.css");
        }
        stage.setScene(scene);
        stage.setTitle("Error!");
        stage.setResizable(false);
        stage.show();
    }
}
