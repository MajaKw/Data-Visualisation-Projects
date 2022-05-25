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
import java.util.List;
import java.util.stream.Collectors;


public class ControllerOfChartSetUpWindow {
    // TODO: correct window resizing (so that buttons and fields stay centered)
    @FXML
    private ToggleButton button3D;
    @FXML
    private ToggleButton button2D;
    @FXML
    private TextField xAxisInputField;
    @FXML
    private TextField yAxisInputField;
    @FXML
    private TextField zAxisInputField;
    @FXML
    private Label zAxisInputLabel;
    @FXML
    private Button backToMainMenuButton;

    public String filePath;
    ArrayList<String> words;
    Object obj;
    JSONObject jo;

    @FXML
    private ListView<String> listView;
    @FXML
    private ListView<String> listView2;
    @FXML
    private ListView<String> listView3;
    private List<String> searchList(String searchWords, List<String> listOfStrings) {

        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split(" "));

        return listOfStrings.stream().filter(input -> {
            return searchWordsArray.stream().allMatch(word ->
                    input.toLowerCase().contains(word.toLowerCase()));
        }).collect(Collectors.toList());
    }


    @FXML
    public void initialize() throws IOException, ParseException {
        button2D.setSelected(true);
        zAxisInputLabel.setVisible(false);
        System.out.println("---------------creation----------------");
        filePath = "src/main/resources/categories.json";

        words = new ArrayList<>();
        this.obj = new JSONParser().parse(new FileReader(filePath));
        this.jo = (JSONObject) obj;
        listView.setVisible(false);
        listView.setManaged(false);
        listView2.setVisible(false);
        listView2.setManaged(false);
        listView3.setVisible(false);
        listView3.setManaged(false);
        xAxisInputField.focusedProperty().addListener((obs, oldVal, newVal) ->
        {
            listView.setVisible(newVal);
            listView.setManaged(newVal);
        });
        yAxisInputField.focusedProperty().addListener((obs, oldVal, newVal) ->
        {
            listView2.setVisible(newVal);
            listView2.setManaged(newVal);
        });
        zAxisInputField.focusedProperty().addListener((obs, oldVal, newVal) ->
        {
            listView3.setVisible(newVal);
            listView3.setManaged(newVal);
        });
        listView.focusedProperty().addListener((obs, oldVal, newVal) ->
        {
            listView.setVisible(newVal);
            listView.setManaged(newVal);
        });
        listView2.focusedProperty().addListener((obs, oldVal, newVal) ->
        {
            listView2.setVisible(newVal);
            listView2.setManaged(newVal);
        });
        listView3.focusedProperty().addListener((obs, oldVal, newVal) ->
        {
            listView3.setVisible(newVal);
            listView3.setManaged(newVal);
        });
        listView.setOnMouseClicked(event -> {
            if (event.getClickCount()==2){
                String File = listView.getSelectionModel().getSelectedItem();
                xAxisInputField.setText(File);
                listView.getSelectionModel().clearSelection();
                listView.getFocusModel().focus(-1);
                listView.setVisible(false);
                listView.setManaged(false);
            }
        });
        listView2.setOnMouseClicked(event -> {
            if (event.getClickCount()==2){
                String File = listView2.getSelectionModel().getSelectedItem();
                yAxisInputField.setText(File);
                listView2.getSelectionModel().clearSelection();
                listView2.getFocusModel().focus(-1);
                listView2.setVisible(false);
                listView2.setManaged(false);
            }
        });
        listView3.setOnMouseClicked(event -> {
            if (event.getClickCount()==2){
                String File = listView3.getSelectionModel().getSelectedItem();
                zAxisInputField.setText(File);
                listView3.getSelectionModel().clearSelection();
                listView3.getFocusModel().focus(-1);
                listView3.setVisible(false);
                listView3.setManaged(false);
            }
        });
        words.clear();

        for (Object o : jo.keySet()) {
            JSONArray cattegory = (JSONArray) jo.get(o);
            for (int i = 0; i<cattegory.size(); i++){
                String obj = (String) cattegory.get(i);
                words.add(obj);
            }
        }

        listView.getItems().addAll(words);
        listView2.getItems().addAll(words);
        listView3.getItems().addAll(words);
    }
    @FXML
    void search(KeyEvent evt) {
        listView.getItems().clear();
        listView.getItems().addAll(searchList(xAxisInputField.getText(),words));
    }
    @FXML
    void search2(KeyEvent evt) {
        listView2.getItems().clear();
        listView2.getItems().addAll(searchList(yAxisInputField.getText(),words));
    }
    @FXML
    void search3(KeyEvent evt) {
        listView3.getItems().clear();
        listView3.getItems().addAll(searchList(zAxisInputField.getText(),words));
    }
    public void click2D(ActionEvent e) {
        button2D.setSelected(true);
        button3D.setSelected(false);
        zAxisInputField.setVisible(false);
        zAxisInputLabel.setVisible(false);
    }
    public void click3D(ActionEvent e) {
        button2D.setSelected(false);
        button3D.setSelected(true);
        zAxisInputField.setVisible(true);
        zAxisInputLabel.setVisible(true);
    }

    public void createChart(ActionEvent e) {
        //create new window containing chart with specified data
        ChartWindow chartWindow = new ChartWindow();
        if(zAxisInputField.isVisible()) chartWindow.showChartWindow(xAxisInputField.getText(), yAxisInputField.getText(), zAxisInputField.getText());
        else chartWindow.showChartWindow(xAxisInputField.getText(), yAxisInputField.getText(), null);
    }

    public void backToMainMenu(ActionEvent e) {
        MainMenu mainMenu = new MainMenu();
        Stage stage = (Stage)((Node) e.getSource()).getScene().getWindow();
        mainMenu.show(stage);
    }
}
