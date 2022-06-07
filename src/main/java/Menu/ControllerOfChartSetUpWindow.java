package Menu;

import MainMenu.MainMenu;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class ControllerOfChartSetUpWindow {
    @FXML
    private TextField xAxisInputField;
    @FXML
    private TextField yAxisInputField;

    ArrayList<String> xColumns;
    ArrayList<String> yColumns;

    @FXML
    private ListView<String> xColumnsListView;
    @FXML
    private ListView<String> yColumnsListView;

    public static List<String> searchList(String searchWords, List<String> listOfStrings) {

        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split(" "));

        return listOfStrings.stream().filter(input -> searchWordsArray.stream().allMatch(
                word -> input.toLowerCase().contains(word.toLowerCase()))).collect(Collectors.toList()
        );
    }


    @FXML
    public void initialize() {
        xColumns = new ArrayList<>(); yColumns = new ArrayList<>();
        xColumnsListView.setVisible(false);
        xColumnsListView.setManaged(false);
        yColumnsListView.setVisible(false);
        yColumnsListView.setManaged(false);
        xAxisInputField.focusedProperty().addListener((obs, oldVal, newVal) ->
        {
            xColumnsListView.setVisible(newVal);
            xColumnsListView.setManaged(newVal);
        });
        yAxisInputField.focusedProperty().addListener((obs, oldVal, newVal) ->
        {
            yColumnsListView.setVisible(newVal);
            yColumnsListView.setManaged(newVal);
        });
        xColumnsListView.focusedProperty().addListener((obs, oldVal, newVal) ->
        {
            xColumnsListView.setVisible(newVal);
            xColumnsListView.setManaged(newVal);
        });
        yColumnsListView.focusedProperty().addListener((obs, oldVal, newVal) ->
        {
            yColumnsListView.setVisible(newVal);
            yColumnsListView.setManaged(newVal);
        });
        xColumnsListView.setOnMouseClicked(event -> {
            String File = xColumnsListView.getSelectionModel().getSelectedItem();
            xAxisInputField.setText(File);
            xColumnsListView.getSelectionModel().clearSelection();
            xColumnsListView.getFocusModel().focus(-1);
            xColumnsListView.setVisible(false);
            xColumnsListView.setManaged(false);
        });
        yColumnsListView.setOnMouseClicked(event -> {
            String File = yColumnsListView.getSelectionModel().getSelectedItem();
            yAxisInputField.setText(File);
            yColumnsListView.getSelectionModel().clearSelection();
            yColumnsListView.getFocusModel().focus(-1);
            yColumnsListView.setVisible(false);
            yColumnsListView.setManaged(false);
        });

        // using data location
        // searching for available xColumns and yColumns
        var filePaths = new ArrayList<String>(UsefulFunctions.getAllFilePaths());
        fillXYColumns(xColumns, yColumns, filePaths);

        xColumnsListView.getItems().addAll(xColumns);
        yColumnsListView.getItems().addAll(yColumns);
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
    @FXML
    void search(KeyEvent evt) {
        xColumnsListView.getItems().clear();
        xColumnsListView.getItems().addAll(searchList(xAxisInputField.getText(),xColumns));
    }
    @FXML
    void search2(KeyEvent evt) {
        yColumnsListView.getItems().clear();
        yColumnsListView.getItems().addAll(searchList(yAxisInputField.getText(),yColumns));
    }

    public void createChart(ActionEvent e) {
        //create new window containing chart with specified data
        ChartWindow chartWindow = new ChartWindow();
        chartWindow.showChartWindow(xAxisInputField.getText(), yAxisInputField.getText());
    }

    public void backToMainMenu(ActionEvent e) {
        MainMenu mainMenu = new MainMenu();
        Stage stage = (Stage)((Node) e.getSource()).getScene().getWindow();
        mainMenu.show(stage);
    }
}
