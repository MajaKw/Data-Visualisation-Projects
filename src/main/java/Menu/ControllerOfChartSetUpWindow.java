package Menu;

import MainMenu.MainMenu;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class ControllerOfChartSetUpWindow {


    ArrayList<String> xColumns;
    ArrayList<String> yColumns;

    @FXML
    ComboBox<String> xAxisInputField;
    @FXML
    ComboBox<String> yAxisInputField;

    public static List<String> getColumnNamesMatching(String searchWords, List<String> listOfStrings) {

        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split(" "));

        return listOfStrings.stream().filter(input -> searchWordsArray.stream().allMatch(
                word -> input.toLowerCase().contains(word.toLowerCase()))).collect(Collectors.toList()
        );
    }


    @FXML
    public void initialize() {
        xColumns = new ArrayList<>(); yColumns = new ArrayList<>();

        // using data location
        // searching for available xColumns and yColumns
        var filePaths = new ArrayList<String>(UsefulFunctions.getAllFilePaths());
        fillXYColumns(xColumns, yColumns, filePaths);

        xAxisInputField.getItems().addAll(xColumns);
        yAxisInputField.getItems().addAll(yColumns);
    }

    public static void fillXYColumns(Collection<String> xColumns, Collection<String> yColumns, ArrayList<String> filePaths) {
        for(var path : filePaths) {
            xColumns.add(path + '|' + UsefulFunctions.getColumnName(path, 0));
            int i = 1;
            String s = UsefulFunctions.getColumnName(path, i);
            while(s != null) {
                yColumns.add(path + '|' + s);
                i++;
                s = UsefulFunctions.getColumnName(path, i);
            }
        }
    }

    public void xAxisTypingIn(KeyEvent k) {
        xAxisInputField.getItems().clear();
        xAxisInputField.getItems().addAll(getColumnNamesMatching(xAxisInputField.getEditor().getText(), xColumns));
    }

    public void yAxisTypingIn(KeyEvent k) {
        yAxisInputField.getItems().clear();
        yAxisInputField.getItems().addAll(getColumnNamesMatching(yAxisInputField.getEditor().getText(), yColumns));
    }

    public void createChart(ActionEvent e) {
        //create new window containing chart with specified data
        ChartWindow chartWindow = new ChartWindow();
        chartWindow.showChartWindow(xAxisInputField.getEditor().getText(), yAxisInputField.getEditor().getText());
    }

    public void backToMainMenu(ActionEvent e) {
        MainMenu mainMenu = new MainMenu();
        Stage stage = (Stage)((Node) e.getSource()).getScene().getWindow();
        mainMenu.show(stage);
    }
}
