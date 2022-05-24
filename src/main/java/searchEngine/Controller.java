package searchEngine;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Controller implements Initializable {
    public String filePath;
    ArrayList<String> words;
    Object obj;
    JSONObject jo;

    @FXML
    private TextField searchBar;

    @FXML
    private ListView<String> listView;

    public Controller() throws IOException, ParseException {
        System.out.println("---------------creation----------------");
        filePath = "src/main/resources/categories.json";

        words = new ArrayList<>();
        this.obj = new JSONParser().parse(new FileReader(filePath));
        this.jo = (JSONObject) obj;
    }
    public void setPath(String filePath) throws IOException, ParseException {
        this.filePath = filePath;

    }

    @FXML
    void search(KeyEvent evt) {
        listView.getItems().clear();
        listView.getItems().addAll(searchList(searchBar.getText(),words));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchBar.focusedProperty().addListener((obs, oldVal, newVal) ->
                {
                    listView.setVisible(newVal);
                    listView.setManaged(newVal);
                });
        listView.focusedProperty().addListener((obs, oldVal, newVal) ->
        {
            listView.setVisible(newVal);
            listView.setManaged(newVal);
        });
        listView.setOnMouseClicked(event -> {
            if (event.getClickCount()==2){
                String File = listView.getSelectionModel().getSelectedItem();
                searchBar.setText(File);
                listView.getSelectionModel().clearSelection();
                listView.getFocusModel().focus(-1);
                listView.setVisible(false);
                listView.setManaged(false);
            }
        });;
        words.clear();

        for (Object o : jo.keySet()) {
            JSONArray cattegory = (JSONArray) jo.get(o);
            for (int i = 0; i<cattegory.size(); i++){
                String obj = (String) cattegory.get(i);
                words.add(obj);
            }
        }

        listView.getItems().addAll(words);
    }

    private List<String> searchList(String searchWords, List<String> listOfStrings) {

        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split(" "));

        return listOfStrings.stream().filter(input -> {
            return searchWordsArray.stream().allMatch(word ->
                    input.toLowerCase().contains(word.toLowerCase()));
        }).collect(Collectors.toList());
    }
}
/*
<!--<?xml version="1.0" encoding="UTF-8"?>-->

<!--<?import java.lang.*?>-->
<!--<?import java.util.*?>-->
<!--<?import javafx.scene.*?>-->
<!--<?import javafx.scene.control.*?>-->
<!--<?import javafx.scene.layout.*?>-->

<!--<AnchorPane xmlns="http://javafx.com/javafx"-->
<!--            xmlns:fx="http://javafx.com/fxml"-->
<!--            fx:controller="searchEngine.Search"-->
<!--            prefHeight="400.0" prefWidth="600.0">-->

<!--</AnchorPane>-->
*/